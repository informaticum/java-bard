package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Code.code;
import static de.informaticum.javabard.api.FormattableEmitters.s;
import static de.informaticum.javabard.impl.IndentEmitter.INDENT_CHARS_PROPERTY;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.function.BiFunction;
import de.informaticum.javabard.impl.WrapperCode.AbstractWrapperCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CommentTests {

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
    public void testCommentStrings()
    throws Exception {
        final Code code = this.make("String s = %s;", s("Hello world!"));
        assertThat(code.asComment(), hasToString(format("// String s = \"Hello world!\";%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n * String s = \"Hello world!\";%n */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n * String s = \"Hello world!\";%n */%n")));
    }

    public static class PlainWrapperCode
    extends AbstractWrapperCode {

        private final Code code;

        protected PlainWrapperCode(final Code code, final Code prefix, final String infixFormat, final Code suffix) {
            super(prefix, infixFormat, suffix);
            this.code = code;
        }

        protected PlainWrapperCode(final Code code, final AbstractWrapperCode blueprint) {
            super(blueprint);
            this.code = code;
        }

        @Override
        public Code getCode() {
            return this.code;
        }

        @Override
        public Code indentBy(final int diff) {
            return new PlainWrapperCode(this.code.indentBy(diff), this);
        }

    }

    @Test
    public void testIndentedCode()
    throws Exception {
        final Code code = this.make("String s = %s;", s("Hello world!")).indent();
        assertThat(code.getIndent(), equalTo(1));
        final Code prefix = code("/**");
        final String infixFormat = " * %s";
        final Code suffix = code(" */");
        final PlainWrapperCode wc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(wc.getIndent(), equalTo(1));
        assertThat(wc, hasToString(format("    /**%n     * String s = \"Hello world!\";%n     */%n")));
    }

    @Test
    public void testIndentedPrefix()
    throws Exception {
        final Code code = this.make("String s = %s;", s("Hello world!"));
        assertThat(code.getIndent(), equalTo(0));
        final Code prefix = code("/**").indent();
        final String infixFormat = " * %s";
        final Code suffix = code(" */");
        final PlainWrapperCode wc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(wc.getIndent(), equalTo(0));
        assertThat(wc, hasToString(format("    /**%n * String s = \"Hello world!\";%n */%n")));
    }

    @Test
    public void testIndentedSuffix()
    throws Exception {
        final Code code = this.make("String s = %s;", s("Hello world!"));
        assertThat(code.getIndent(), equalTo(0));
        final Code prefix = code("/**");
        final String infixFormat = " * %s";
        final Code suffix = code(" */").indent();
        final PlainWrapperCode wc = new PlainWrapperCode(code, prefix, infixFormat, suffix);
        assertThat(wc.getIndent(), equalTo(0));
        assertThat(wc, hasToString(format("/**%n * String s = \"Hello world!\";%n     */%n")));
    }
}
