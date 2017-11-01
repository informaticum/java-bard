package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.t;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import de.informaticum.javabard.impl.SingleCodeBlock;
import org.junit.Test;

public class CodeBlockTests {

    @Test
    public void testName1()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("final java.util.BitSet bs = null;");
        assertThat(code, hasToString("final java.util.BitSet bs = null;"));
    }

    @Test
    public void testName2()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("final %s bs = null;", t(BitSet.class));
        assertThat(code, hasToString("final java.util.BitSet bs = null;"));
    }

    @Test
    public void testName3()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString("final java.util.BitSet bs = null;"));
    }

    @Test
    public void testName4()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("if (true) {") //
                                              .add("final java.util.BitSet bs = null;") //
                                              .add("}");
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
    }

    @Test
    public void testName5()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code, hasToString("final java.util.BitSet bs = null;"));
        final CodeBlock codeIndent = code.indent();
        assertThat(codeIndent, hasToString("    final java.util.BitSet bs = null;"));
        final CodeBlock codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent, hasToString("final java.util.BitSet bs = null;"));
    }

    @Test
    public void testName6()
    throws Exception {
        final CodeBlock code = SingleCodeBlock.of("if (true) {") //
                                              .add(SingleCodeBlock.of("final java.util.BitSet bs = null;").indent()) //
                                              .add("}");
        assertThat(code, hasToString(format("if (true) {%n    final java.util.BitSet bs = null;%n}%n")));
    }

}
