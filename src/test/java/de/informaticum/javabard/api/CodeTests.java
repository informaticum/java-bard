package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.i;
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
    public static Iterable<BiFunction<String, Object[], Code>> data() {
        final BiFunction<String, Object[], Code> scf = (s, a) -> code(s, a);
        final BiFunction<String, Object[], Code> scb = (s, a) -> new SingleCode.Builder(s, a).build();
        final BiFunction<String, Object[], Code> mcf = (s, a) -> combine(code(s, a));
        final BiFunction<String, Object[], Code> mcb = (s, a) -> new MultiCode.Builder(code(s, a)).build();
        return asList(scf, scb, mcf, mcb);
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
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code code2 = code.indent().unindent();
        assertThat(code2.getIndent(), equalTo(0));
        assertThat(code2, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testUnindentIndent()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code code2 = code.unindent().indent();
        assertThat(code2.getIndent(), equalTo(1));
        assertThat(code2, hasToString(format("    final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedType()
    throws Exception {
        final Code code = this.make("final %s bs = null;", t(BitSet.class));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedArguments()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineCode()
    throws Exception {
        final Code code = this.make("if (true) {") //
                              .add("final java.util.BitSet bs = null;") //
                              .add("}");
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentationAndUnindentation()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final Code codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent, hasToString(code.toString()));
    }

    @Test
    public void testAddAfterIndent_addStringFormat()
    throws Exception {
        final Code code = this.make("Object o1 = null;").indent().add("Object o2 = null;");
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCode()
    throws Exception {
        final Code first = this.make("Object o1 = null;");
        final Code code = first.indent().add(this.make("Object o2 = null;"));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCollectionOfCode()
    throws Exception {
        final Code first = this.make("Object o1 = null;");
        final List<Code> codes = asList(this.make("Object o2 = null;"), this.make("Object o3 = null;"));
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addArrayOfCode()
    throws Exception {
        final Code first = this.make("Object o1 = null;");
        final Code[] codes = new Code[] { this.make("Object o2 = null;"), this.make("Object o3 = null;") };
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testIndentedSubCode()
    throws Exception {
        final Code code = this.make("if (true) {").add(this.make("final java.util.BitSet bs = null;").indent()).add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testDirectIndentation()
    throws Exception {
        final Code code = this.make("if (true) {").add(this.make("%sfinal java.util.BitSet bs = null;", i())).add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testSpecificIndentation()
    throws Exception {
        final Code code = this.make("if (true) {").add(this.make("%sfinal java.util.BitSet bs = null;", i(2))).add("}");
        assertThat(code, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n            final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentationOfAppendedCode()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final Code codeAdded = codeIndent.add("final %s bs2 = %s;", t(BitSet.class), null);
        assertThat(codeAdded, hasToString(format("    final java.util.BitSet bs = null;%n    final java.util.BitSet bs2 = null;%n")));
    }

    @Test
    public void testAlternativeIndentationCharacter()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null).indent();
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));

        System.setProperty(INDENT_CHARS_PROPERTY, "\t");
        assertThat(code, hasToString(format("\tfinal java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineIndentation()
    throws Exception {
        final Code code = this.make("final %s o = (n==null) ?%n%s.of(n) :%n%s.empty();", t(Optional.class), t(Optional.class), t(Optional.class)).indent();
        assertThat(code,
                   hasToString(format("    final java.util.Optional o = (n==null) ?%n    java.util.Optional.of(n) :%n    java.util.Optional.empty();%n")));
    }

    @Test
    public void testIndexedArguments()
    throws Exception {
        final Code code = this.make("final %1$s o = (n==null) ? %1$s.of(n) : %1$s.empty();", t(Optional.class));
        assertThat(code, hasToString(format("final java.util.Optional o = (n==null) ? java.util.Optional.of(n) : java.util.Optional.empty();%n")));
    }

    @Test
    public void testIndentationOfEmptyMultiCode()
    throws Exception {
        final Code codeBlock = new MultiCode.Builder().build();
        assertEquals(0, codeBlock.getIndent());
    }

    @Test
    public void testDeferredFormatting()
    throws Exception {
        final Holder<Boolean> hasBeenUsed = new Holder<>(FALSE);
        final Code code = this.make("%s", (Formattable) (formatter, flags, width, precision) -> {
            hasBeenUsed.value = TRUE;
            formatter.format("Hello world!");
        });
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

}
