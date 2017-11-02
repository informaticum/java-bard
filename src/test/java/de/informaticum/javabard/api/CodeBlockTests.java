package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.CodeBlock.code;
import static de.informaticum.javabard.api.FormattableEmitters.t;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import org.junit.Test;

public class CodeBlockTests {

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

}
