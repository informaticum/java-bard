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
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import java.util.Formattable;
import java.util.List;
import java.util.Optional;
import javax.xml.ws.Holder;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CodeTests {

    @Before
    @After
    public void clearIndentProperty() {
        System.clearProperty(INDENT_CHARS_PROPERTY);
    }

    @Test
    public void testPlainFormatStringWithoutArguments()
    throws Exception {
        final Code code = code("final java.util.BitSet bs = null;");
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedType()
    throws Exception {
        final Code code = code("final %s bs = null;", t(BitSet.class));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testFormatStringWithFormattedArguments()
    throws Exception {
        final Code code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineCode()
    throws Exception {
        final Code code = code("if (true) {") //
                                              .add("final java.util.BitSet bs = null;") //
                                              .add("}");
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentationAndUnindentation()
    throws Exception {
        final Code code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final Code codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent, hasToString(code.toString()));
    }

    @Test
    public void testAddAfterIndent_addStringFormat()
    throws Exception {
        final Code code = code("Object o1 = null;").indent().add("Object o2 = null;");
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCode_SingleCode()
    throws Exception {
        final SingleCode first = code("Object o1 = null;");
        final Code code = first.indent().add(code("Object o2 = null;"));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCollectionOfCode_SingleCode()
    throws Exception {
        final SingleCode first = code("Object o1 = null;");
        final List<SingleCode> codes = asList(code("Object o2 = null;"), code("Object o3 = null;"));
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addArrayOfCode_SingleCode()
    throws Exception {
        final SingleCode first = code("Object o1 = null;");
        final Code[] codes = new Code[] { code("Object o2 = null;"), code("Object o3 = null;") };
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCode_MultiCode()
    throws Exception {
        final MultiCode first = combine(code("Object o1 = null;"));
        final Code code = first.indent().add(code("Object o2 = null;"));
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addCollectionOfCode_MultiCode()
    throws Exception {
        final MultiCode first = combine(code("Object o1 = null;"));
        final List<SingleCode> codes = asList(code("Object o2 = null;"), code("Object o3 = null;"));
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testAddAfterIndent_addArrayOfCode_MultiCode()
    throws Exception {
        final MultiCode first = combine(code("Object o1 = null;"));
        final Code[] codes = new Code[] { code("Object o2 = null;"), code("Object o3 = null;") };
        final Code code = first.indent().addAll(codes);
        assertThat(code, hasToString(format("    Object o1 = null;%n    Object o2 = null;%n    Object o3 = null;%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("Object o1 = null;%nObject o2 = null;%nObject o3 = null;%n")));
    }

    @Test
    public void testIndentedSubCode()
    throws Exception {
        final Code code = code("if (true) {").add(code("final java.util.BitSet bs = null;").indent()).add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testDirectIndentation()
    throws Exception {
        final Code code = code("if (true) {").add(code("%sfinal java.util.BitSet bs = null;", i())).add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testSpecificIndentation()
    throws Exception {
        final Code code = code("if (true) {").add(code("%sfinal java.util.BitSet bs = null;", i(2))).add("}");
        assertThat(code, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n            final java.util.BitSet bs = null;%n    }%n")));
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n        final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testIndentationOfAppendedCode()
    throws Exception {
        final Code code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final Code codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final Code codeAdded = codeIndent.add("final %s bs2 = %s;", t(BitSet.class), null);
        assertThat(codeAdded, hasToString(format("    final java.util.BitSet bs = null;%n    final java.util.BitSet bs2 = null;%n")));
    }

    @Test
    public void testAlternativeIndentationCharacter()
    throws Exception {
        final Code code = code("final %s bs = %s;", t(BitSet.class), null).indent();
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));

        System.setProperty(INDENT_CHARS_PROPERTY, "\t");
        assertThat(code, hasToString(format("\tfinal java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testMultilineIndentation()
    throws Exception {
        final Code code = code("final %s o = (n==null) ?%n%s.of(n) :%n%s.empty();", t(Optional.class), t(Optional.class), t(Optional.class)).indent();
        assertThat(code,
                   hasToString(format("    final java.util.Optional o = (n==null) ?%n    java.util.Optional.of(n) :%n    java.util.Optional.empty();%n")));
    }

    @Test
    public void testIndexedArguments()
    throws Exception {
        final Code code = code("final %1$s o = (n==null) ? %1$s.of(n) : %1$s.empty();", t(Optional.class));
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
        final Code code = code("%s", (Formattable) (formatter, flags, width, precision) -> {
            hasBeenUsed.value = TRUE;
            formatter.format("Hello world!");
        });
        assertEquals(FALSE, hasBeenUsed.value);
        code.toString();
        assertEquals(TRUE, hasBeenUsed.value);
    }

}
