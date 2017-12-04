package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.i;
import static de.informaticum.javabard.api.FormattableEmitters.indent;
import static de.informaticum.javabard.api.FormattableEmitters.t;
import static de.informaticum.javabard.impl.AbstractCode.code;
import static de.informaticum.javabard.impl.AbstractCode.combine;
import static de.informaticum.javabard.impl.IndentEmitter.INDENT_CHARS_PROPERTY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import java.util.Collection;
import java.util.Formattable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import javax.xml.ws.Holder;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CodeTests {

    @Parameters
    public static Iterable<BiFunction<String, Object[], Code>> codeFactories() {
        final BiFunction<String, Object[], Code> scf = (s, a) -> code(s, a);
        final BiFunction<String, Object[], Code> scb = (s, a) -> new SingleCode.Builder(s, a).get();
        final BiFunction<String, Object[], Code> sca = (s, a) -> new SingleCode.Builder("").get().add(s, a);
        final BiFunction<String, Object[], Code> mcf = (s, a) -> combine(code(s, a));
        final BiFunction<String, Object[], Code> mcb = (s, a) -> new MultiCode.Builder(code(s, a)).get();
        final BiFunction<String, Object[], Code> mca = (s, a) -> new MultiCode.Builder().get().add(s, a);
        return asList(scf, scb, sca, mcf, mcb, mca);
    }

    @Parameter(0)
    public BiFunction<String, Object[], Code> factory;

    private Code make(final String format, final Object... args) {
        return this.factory.apply(format, args);
    }

    @Before
    @After
    public void clearIndentProperty() {
        System.clearProperty(INDENT_CHARS_PROPERTY);
    }

    @Test
    public void testPlainFormatStringWithoutArguments()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testNoNegativeIndent_relative()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testNoNegativeIndent_absolute()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code codeUnindent = code.setIndent(-1);
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testIndentUnindent()
    throws Exception {
        final Code origin = this.make("final java.util.BitSet bs = null;");
        final Code code = origin.indent().unindent();
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testUnindentIndent()
    throws Exception {
        final Code origin = this.make("final java.util.BitSet bs = null;");
        final Code code = origin.unindent().indent();
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedType()
    throws Exception {
        final Code code = this.make("final %s bs = null;", t(BitSet.class));
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedArguments()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineCode()
    throws Exception {
        final Code code = this.make("if (true) {") //
                              .add("final java.util.BitSet bs = null;") //
                              .add("}");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentAndUnindent()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));

        final Code codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(code.toString()));
    }

    @Test
    public void testAddAfterIndent_addStringFormat()
    throws Exception {
        final String additional = "Object o2 = null;";
        final Code code = this.make("Object o1 = null;").indent().add(additional);
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCode()
    throws Exception {
        final Code additional = this.make("Object o2 = null;");
        final Code code = this.make("Object o1 = null;").indent().add(additional);
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCollectionOfCode()
    throws Exception {
        final List<Code> additionals = asList(this.make("Object o2 = null;"), this.make("Object o3 = null;"));
        final Code code = this.make("Object o1 = null;").indent().addAll(additionals);
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addArrayOfCode()
    throws Exception {
        final Code[] additionals = new Code[] { this.make("Object o2 = null;"), this.make("Object o3 = null;") };
        final Code code = this.make("Object o1 = null;").indent().addAll(additionals);
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testIndentedSubCode()
    throws Exception {
        final Code additional = this.make("final java.util.BitSet bs = null;").indent();
        final Code code = this.make("if (true) {").add(additional).add("}");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testDirectIndent()
    throws Exception {
        final Code additional = this.make("%sfinal java.util.BitSet bs = null;", i());
        final Code code = this.make("if (true) {").add(additional).add("}");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testSpecificIndent()
    throws Exception {
        final Code additional = this.make("%sfinal java.util.BitSet bs = null;", i(2));
        final Code code = this.make("if (true) {").add(additional).add("}");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    if (true) {%n            final java.util.BitSet bs = null;%n    }%n")));

        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentOfAppendedCode()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));

        final Code codeAdded = codeIndent.add("final %s bs2 = %s;", t(BitSet.class), null);
        assertThat(codeAdded.getIndent(), equalTo(1));
        assertThat(codeAdded, hasToString(format("    final java.util.BitSet bs = null;%n    final java.util.BitSet bs2 = null;%n")));
    }

    @Test
    public void testAlternativeIndentCharacter()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null).indent();
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));

        System.setProperty(INDENT_CHARS_PROPERTY, "\t");
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("\tfinal java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineIndent()
    throws Exception {
        final Code code = this.make("final %s o = (n==null) ?%n%s.of(n) :%n%s.empty();", t(Optional.class), t(Optional.class), t(Optional.class)).indent();
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code,
                   hasToString(format("    final java.util.Optional o = (n==null) ?%n    java.util.Optional.of(n) :%n    java.util.Optional.empty();%n")));
    }

    @Test
    public void testIndexedArguments()
    throws Exception {
        final Code code = this.make("final %1$s o = (n==null) ? %1$s.of(n) : %1$s.empty();", t(Optional.class));
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.Optional o = (n==null) ? java.util.Optional.of(n) : java.util.Optional.empty();%n")));
    }

    @Test
    public void testIndentOfEmptyMultiCode()
    throws Exception {
        final Code empty = combine();
        assertEquals(0, empty.getIndent());
        assertThat(empty, hasToString(""));

        final Code indented = empty.indentBy(2);
        assertEquals(2, indented.getIndent());
        assertThat(indented, hasToString(""));

        final Code added = indented.add(this.make("//foobar"));
        assertEquals(2, added.getIndent());
        assertThat(added, hasToString(format("        //foobar%n")));
    }

    @Test
    public void testDeferredFormatting()
    throws Exception {
        final Holder<Boolean> hasBeenUsed = new Holder<>(FALSE);
        final Formattable verboseFormattable = (formatter, flags, width, precision) -> {
            hasBeenUsed.value = TRUE;
            formatter.format("Hello world!");
        };
        final Code code = this.make("%s", verboseFormattable);
        assertEquals(FALSE, hasBeenUsed.value);
        code.toString();
        assertEquals(TRUE, hasBeenUsed.value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectNullFormat()
    throws Exception {
        final String format = null;
        this.make(format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectNullFormatWithValidArgs()
    throws Exception {
        final String format = null;
        this.make(format, code("foo"), code("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectNullArgs()
    throws Exception {
        final String format = "// whatever";
        final Object[] args = null;
        this.make(format, args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddNullFormat()
    throws Exception {
        final Code code = this.make("//whatever");
        final String format = null;
        code.add(format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddNullFormatWithValidArgs()
    throws Exception {
        final Code code = this.make("//whatever");
        final String format = null;
        code.add(format, code("foo"), code("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddNullArgs()
    throws Exception {
        final Code code = this.make("//whatever");
        final String format = "// whatever";
        final Object[] args = null;
        code.add(format, args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddAllNullWithinArray()
    throws Exception {
        final Code code = this.make("//whatever");
        final Code c = null;
        code.addAll(c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddAllNullArray()
    throws Exception {
        final Code code = this.make("//whatever");
        final Code[] cs = null;
        code.addAll(cs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddAllNullWithinCollection()
    throws Exception {
        final Code code = this.make("//whatever");
        final Code c = null;
        code.addAll(asList(c));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectAddAllNullCollection()
    throws Exception {
        final Code code = this.make("//whatever");
        final Collection<Code> cs = null;
        code.addAll(cs);
    }

    private void testAddToEmptyIndented(final int i)
    throws Exception {
        final Code emptyIndented = combine().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code added = emptyIndented.add(this.make("//whatever"));
        assertEquals(i, added.getIndent());
        assertThat(added, hasToString(format("%s//whatever%n", indent(i))));
    }

    @Test
    public void testAddToEmptyIndented_0_1_2_4()
    throws Exception {
        this.testAddToEmptyIndented(0);
        this.testAddToEmptyIndented(1);
        this.testAddToEmptyIndented(2);
        this.testAddToEmptyIndented(4);
    }

    private void testAddToEmptyIndentedCascade(final int i)
    throws Exception {
        final Code emptyIndented = combine().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code emptyCascade = emptyIndented.add(emptyIndented);
        assert emptyCascade.getIndent() == i;
        assert emptyCascade.toString().equals("");

        final Code added = emptyCascade.add(this.make("//whatever"));
        assertEquals(i, added.getIndent());
        assertThat(added, hasToString(format("%s//whatever%n", indent(i))));
    }

    @Test
    public void testAddToEmptyIndentedCascade_0_1_2_4()
    throws Exception {
        this.testAddToEmptyIndentedCascade(0);
        this.testAddToEmptyIndentedCascade(1);
        this.testAddToEmptyIndentedCascade(2);
        this.testAddToEmptyIndentedCascade(4);
    }

    private void testCombineWithEmptyIndented(final int i)
    throws Exception {
        final Code emptyIndented = combine().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code combined = combine(emptyIndented, this.make("//whatever"));
        assertEquals(0, combined.getIndent());
        assertThat(combined, hasToString(format("//whatever%n")));
    }

    @Test
    public void testCombineWithIndented_0_1_2_4()
    throws Exception {
        this.testCombineWithEmptyIndented(0);
        this.testCombineWithEmptyIndented(1);
        this.testCombineWithEmptyIndented(2);
        this.testCombineWithEmptyIndented(4);
    }

    private void testCombineWithEmptyIndentedCascade(final int i)
    throws Exception {
        final Code emptyIndented = combine().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code emptyCascade = emptyIndented.add(emptyIndented);
        assert emptyCascade.getIndent() == i;
        assert emptyCascade.toString().equals("");

        final Code combined = combine(emptyCascade, this.make("//whatever"));
        assertEquals(0, combined.getIndent());
        assertThat(combined, hasToString(format("//whatever%n")));
    }

    @Test
    public void testCombineWithEmptyIndentedCascade_0_1_2_4()
    throws Exception {
        this.testCombineWithEmptyIndentedCascade(0);
        this.testCombineWithEmptyIndentedCascade(1);
        this.testCombineWithEmptyIndentedCascade(2);
        this.testCombineWithEmptyIndentedCascade(4);
    }

    private void testAddWithEmptyIndented(final int i)
    throws Exception {
        final Code code = this.make("//whatever");
        assertEquals(0, code.getIndent());
        assertThat(code, hasToString(format("//whatever%n")));

        final Code emptyIndented = combine().indentBy(i);

        final Code added = code.add(emptyIndented);
        assertEquals(0, added.getIndent());
        assertThat(added, hasToString(format("//whatever%n")));
    }

    @Test
    public void testAddWithEmptyIndented_0_1_2_4()
    throws Exception {
        this.testAddWithEmptyIndented(0);
        this.testAddWithEmptyIndented(1);
        this.testAddWithEmptyIndented(2);
        this.testAddWithEmptyIndented(4);
    }

}
