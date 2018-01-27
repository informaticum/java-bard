package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Code.code;
import static de.informaticum.javabard.impl.IndentEmitter.INDENT_CHARS_PROPERTY;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.function.BiFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WrapperCodeTests {

    @Parameters
    public static Iterable<BiFunction<String, Object[], Code>> codeFactories() {
        return CodeTests.codeFactories();
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
    public void testPlainToString()
    throws Exception {
        final Code code = this.make("Object o = null;");
        assertThat(code.getIndent(), equalTo(0));

        final Code prefix = code("// Some plain prefix comment");
        final String infixFormat = "// %s";
        final Code suffix = code("// Some plain suffix comment");

        final PlainWrapperCode pwc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(pwc.getIndent(), equalTo(0));
        assertThat(pwc, hasToString(format("// Some plain prefix comment%n// Object o = null;%n// Some plain suffix comment%n")));
    }

    @Test
    public void testIndentedPrefix()
    throws Exception {
        final Code code = this.make("Object o = null;");
        assertThat(code.getIndent(), equalTo(0));

        final Code prefix = code("// Some indented prefix comment").indent();
        final String infixFormat = "// %s";
        final Code suffix = code("// Some plain suffix comment");

        final PlainWrapperCode pwc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(pwc.getIndent(), equalTo(0));
        assertThat(pwc, hasToString(format("    // Some indented prefix comment%n// Object o = null;%n// Some plain suffix comment%n")));
    }

    @Test
    public void testIndentedCode()
    throws Exception {
        final Code code = this.make("Object o = null;").indent();
        assertThat(code.getIndent(), equalTo(1));

        final Code prefix = code("// Some plain prefix comment");
        final String infixFormat = "// %s";
        final Code suffix = code("// Some plain suffix comment");

        final PlainWrapperCode pwc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(pwc.getIndent(), equalTo(1));
        assertThat(pwc, hasToString(format("    // Some plain prefix comment%n    // Object o = null;%n    // Some plain suffix comment%n")));
    }

    @Test
    public void testIndentedSuffix()
    throws Exception {
        final Code code = this.make("Object o = null;");
        assertThat(code.getIndent(), equalTo(0));

        final Code prefix = code("// Some plain prefix comment");
        final String infixFormat = "// %s";
        final Code suffix = code("// Some indented suffix comment").indent();

        final PlainWrapperCode pwc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(pwc.getIndent(), equalTo(0));
        assertThat(pwc, hasToString(format("// Some plain prefix comment%n// Object o = null;%n    // Some indented suffix comment%n")));
    }

    @Test
    public void testIndentedPrefixAndCode()
    throws Exception {
        final Code code = this.make("Object o = null;").indent();
        assertThat(code.getIndent(), equalTo(1));

        final Code prefix = code("// Some indented prefix comment").indent();
        final String infixFormat = "// %s";
        final Code suffix = code("// Some plain suffix comment");

        final PlainWrapperCode pwc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(pwc.getIndent(), equalTo(1));
        assertThat(pwc, hasToString(format("        // Some indented prefix comment%n    // Object o = null;%n    // Some plain suffix comment%n")));
    }

}
