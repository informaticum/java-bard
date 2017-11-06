package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.AbstractCodeBlock.code;
import static de.informaticum.javabard.api.FormattableEmitters.t;
import static de.informaticum.javabard.impl.IndentEmitter.INDENT_CHARS_PROPERTY;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class CodeBlockTests {

    @Before
    public void clearIndentProperty() {
        System.clearProperty(INDENT_CHARS_PROPERTY);
    }

    @Test
    public void testName1()
    throws Exception {
        final CodeBlock code = code("final java.util.BitSet bs = null;");
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testName2()
    throws Exception {
        final CodeBlock code = code("final %s bs = null;", t(BitSet.class));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testName3()
    throws Exception {
        final CodeBlock code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testName4()
    throws Exception {
        final CodeBlock code = code("if (true) {") //
                                                   .add("final java.util.BitSet bs = null;") //
                                                   .add("}");
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testName5()
    throws Exception {
        final CodeBlock code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final CodeBlock codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final CodeBlock codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent, hasToString(format("final java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testName6()
    throws Exception {
        final CodeBlock code = code("if (true) {").add(code("final java.util.BitSet bs = null;").indent()).add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
        final CodeBlock codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    if (true) {%n        final java.util.BitSet bs = null;%n    }%n")));
        final CodeBlock codeUnindent = code.unindent();
        assertThat(codeUnindent, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testName7()
    throws Exception {
        final CodeBlock code = code("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        final CodeBlock codeIndent = code.indent();
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        final CodeBlock codeAdded = codeIndent.add("final %s bs2 = %s;", t(BitSet.class), null);
        assertThat(codeAdded, hasToString(format("    final java.util.BitSet bs = null;%n    final java.util.BitSet bs2 = null;%n")));
    }

    @Test
    public void testName8()
    throws Exception {
        final CodeBlock code = code("final %s bs = %s;", t(BitSet.class), null).indent();
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));

        System.setProperty(INDENT_CHARS_PROPERTY, "\t");
        assertThat(code, hasToString(format("\tfinal java.util.BitSet bs = null;%n")));
    }

    @Test
    public void testName9()
    throws Exception {
        final CodeBlock code = code("final %s o = (n==null) ?%n%s.of(n) :%n%s.empty();", t(Optional.class), t(Optional.class), t(Optional.class)).indent();
        assertThat(code, hasToString(format("    final java.util.Optional o = (n==null) ?%n    java.util.Optional.of(n) :%n    java.util.Optional.empty();%n")));
    }

    @Test
    public void testName10()
    throws Exception {
        final CodeBlock code = code("final %1$s o = (n==null) ? %1$s.of(n) : %1$s.empty();", t(Optional.class));
        assertThat(code, hasToString(format("final java.util.Optional o = (n==null) ? java.util.Optional.of(n) : java.util.Optional.empty();%n")));
    }

}
