package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Code.code;
import static de.informaticum.javabard.api.Code.combine;
import static de.informaticum.javabard.api.Code.emptyCode;
import static de.informaticum.javabard.api.FormattableEmitters.i;
import static de.informaticum.javabard.api.FormattableEmitters.indent;
import static de.informaticum.javabard.api.FormattableEmitters.t;
import static de.informaticum.javabard.impl.IndentEmitter.INDENT_CHARS_PROPERTY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Locale.getDefault;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import java.util.BitSet;
import java.util.Collection;
import java.util.Formattable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
import javax.xml.ws.Holder;
import de.informaticum.javabard.impl.IndentionMarkerCode;
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
        final BiFunction<String, Object[], Code> code           = (s, a) -> code(s, a);
        final BiFunction<String, Object[], Code> codeLocale     = (s, a) -> code(Locale.getDefault(), s, a);
        final BiFunction<String, Object[], Code> rePositioned   = (s, a) -> code(s, a).indent().unindent();
        final BiFunction<String, Object[], Code> single         = (s, a) -> new SingleCode.Builder(s, a).get();
        final BiFunction<String, Object[], Code> nonIndented    = (s, a) -> new SingleCode.Builder(s, a).setIndent(0).get();
        final BiFunction<String, Object[], Code> resetIndented  = (s, a) -> new SingleCode.Builder(s, a).setIndent(2).get().indentBy(-2);
        final BiFunction<String, Object[], Code> skipped        = (s, a) -> new SingleCode.Builder(s, a).get().indentBy(-1);
        final BiFunction<String, Object[], Code> nonLocalised   = (s, a) -> new SingleCode.Builder(s, a).setLocale(empty()).get();
        final BiFunction<String, Object[], Code> localised      = (s, a) -> new SingleCode.Builder(s, a).setLocale(getDefault()).get();
        final BiFunction<String, Object[], Code> deLocalised    = (s, a) -> new SingleCode.Builder(s, a).setLocale(getDefault()).setLocale(empty()).get();
        final BiFunction<String, Object[], Code> comboCode      = (s, a) -> combine(code(s, a));
        final BiFunction<String, Object[], Code> comboSingle    = (s, a) -> combine(new SingleCode.Builder(s, a).get());
        final BiFunction<String, Object[], Code> comboAdd       = (s, a) -> emptyCode().add(s, a);
        final BiFunction<String, Object[], Code> comboAddLocale = (s, a) -> emptyCode().add(Locale.getDefault(), s, a);
        final BiFunction<String, Object[], Code> comboAddCode   = (s, a) -> emptyCode().add(code(s, a));
        final BiFunction<String, Object[], Code> comboAddSingle = (s, a) -> emptyCode().add(new SingleCode.Builder(s, a).get());
        final BiFunction<String, Object[], Code> multiCode      = (s, a) -> new MultiCode.Builder(code(s, a)).get();
        final BiFunction<String, Object[], Code> multiSingle    = (s, a) -> new MultiCode.Builder(new SingleCode.Builder(s, a).get()).get();
        final BiFunction<String, Object[], Code> multiAdd       = (s, a) -> new MultiCode.Builder().get().add(s, a);
        final BiFunction<String, Object[], Code> multiAddLocale = (s, a) -> new MultiCode.Builder().get().add(Locale.getDefault(), s, a);
        final BiFunction<String, Object[], Code> multiAddCode   = (s, a) -> new MultiCode.Builder().get().add(code(s, a));
        final BiFunction<String, Object[], Code> multiAddSingle = (s, a) -> new MultiCode.Builder().get().add(new SingleCode.Builder(s, a).get());
        final BiFunction<String, Object[], Code> chainAdd       = (s, a) -> emptyCode().addAll().add(s, a);
        final BiFunction<String, Object[], Code> chainAddLocale = (s, a) -> emptyCode().addAll().add(Locale.getDefault(), s, a);
        final BiFunction<String, Object[], Code> chainAddCode   = (s, a) -> emptyCode().addAll().add(code(s, a));
        final BiFunction<String, Object[], Code> chainAddSingle = (s, a) -> emptyCode().addAll().add(new SingleCode.Builder(s, a).get());
        final BiFunction<String, Object[], Code> preReset       = (s, a) -> emptyCode().indentBy(2).indentBy(-2).add(s, a);
        final BiFunction<String, Object[], Code> postReset      = (s, a) -> emptyCode().indentBy(2).add(s, a).indentBy(-2);
        final BiFunction<String, Object[], Code> preOverreset   = (s, a) -> emptyCode().indentBy(2).indentBy(-2).indentBy(-14).add(s, a);
        final BiFunction<String, Object[], Code> postOverreset  = (s, a) -> emptyCode().indentBy(2).add(s, a).indentBy(-2).indentBy(-14);
        return asList(code, codeLocale, single, rePositioned, //
                      nonIndented, resetIndented, skipped, //
                      nonLocalised, localised, deLocalised, //
                      comboCode, comboSingle, //
                      comboAdd, comboAddLocale, comboAddCode, comboAddSingle, //
                      multiCode, multiSingle, //
                      multiAdd, multiAddLocale, multiAddCode, multiAddSingle, //
                      chainAdd, chainAddLocale, chainAddCode, chainAddSingle, //
                      preReset, postReset, preOverreset, postOverreset);
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
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " */%n")));
    }

    @Test
    public void testNoNegativeIndent_relative()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code codeUnindent = code.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(codeUnindent.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(codeUnindent.asBlockComment(), hasToString(format("/*%n" + //
                                                                         " * final java.util.BitSet bs = null;%n" + //
                                                                         " */%n")));
        assertThat(codeUnindent.asJavadoc(), hasToString(format("/**%n" + //
                                                                    " * final java.util.BitSet bs = null;%n" + //
                                                                    " */%n")));
    }

    @Test
    public void testNoNegativeIndent_absolute()
    throws Exception {
        final Code code = this.make("final java.util.BitSet bs = null;");
        final Code codeUnindent = code.setIndent(-1);
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(codeUnindent.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(codeUnindent.asBlockComment(), hasToString(format("/*%n" + //
                                                                         " * final java.util.BitSet bs = null;%n" + //
                                                                         " */%n")));
        assertThat(codeUnindent.asJavadoc(), hasToString(format("/**%n" + //
                                                                    " * final java.util.BitSet bs = null;%n" + //
                                                                    " */%n")));
    }

    @Test
    public void testIndentUnindent()
    throws Exception {
        final Code origin = this.make("final java.util.BitSet bs = null;");
        final Code code = origin.indent().unindent();
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " */%n")));
    }

    @Test
    public void testUnindentIndent()
    throws Exception {
        final Code origin = this.make("final java.util.BitSet bs = null;");
        final Code code = origin.unindent().indent();
        assertThat(code.getIndent(), equalTo(1));
        assertThat(code, hasToString(format("    final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("    // final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("    /*%n" + //
                                                                 "     * final java.util.BitSet bs = null;%n" + //
                                                                 "     */%n")));
        assertThat(code.asJavadoc(), hasToString(format("    /**%n" + //
                                                            "     * final java.util.BitSet bs = null;%n" + //
                                                            "     */%n")));
    }

    @Test
    public void testFormatStringWithFormattedType()
    throws Exception {
        final Code code = this.make("final %s bs = null;", t(BitSet.class));
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " */%n")));
    }

    @Test
    public void testFormatStringWithFormattedArguments()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " */%n")));
    }

    @Test
    public void testMultilineCode()
    throws Exception {
        final Code code = this.make("if (true) {") //
                              .add("final java.util.BitSet bs = null;") //
                              .add("}");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("if (true) {%nfinal java.util.BitSet bs = null;%n}%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// if (true) {%n" + //
                                                            "// final java.util.BitSet bs = null;%n" + //
                                                            "// }%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * if (true) {%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " * }%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * if (true) {%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " * }%n" + //
                                                            " */%n")));
    }

    @Test
    public void testIndentAndUnindent()
    throws Exception {
        final Code code = this.make("final %s bs = %s;", t(BitSet.class), null);
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(code.asComment(), hasToString(format("// final java.util.BitSet bs = null;%n")));
        assertThat(code.asBlockComment(), hasToString(format("/*%n" + //
                                                                 " * final java.util.BitSet bs = null;%n" + //
                                                                 " */%n")));
        assertThat(code.asJavadoc(), hasToString(format("/**%n" + //
                                                            " * final java.util.BitSet bs = null;%n" + //
                                                            " */%n")));

        final Code codeIndent = code.indent();
        assertThat(codeIndent.getIndent(), equalTo(1));
        assertThat(codeIndent, hasToString(format("    final java.util.BitSet bs = null;%n")));
        // test comment formattings:
        assertThat(codeIndent.asComment(), hasToString(format("    // final java.util.BitSet bs = null;%n")));
        assertThat(codeIndent.asBlockComment(), hasToString(format("    /*%n" + //
                                                                       "     * final java.util.BitSet bs = null;%n" + //
                                                                       "     */%n")));
        assertThat(codeIndent.asJavadoc(), hasToString(format("    /**%n" + //
                                                                  "     * final java.util.BitSet bs = null;%n" + //
                                                                  "     */%n")));

        final Code codeUnindent = codeIndent.unindent();
        assertThat(codeUnindent.getIndent(), equalTo(0));
        assertThat(codeUnindent, hasToString(code.toString()));
        // test comment formattings:
        assertThat(codeUnindent.asComment(), hasToString(code.asComment().toString()));
        assertThat(codeUnindent.asBlockComment(), hasToString(code.asBlockComment().toString()));
        assertThat(codeUnindent.asJavadoc(), hasToString(code.asJavadoc().toString()));
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
        final Code empty = emptyCode();
        assertThat(empty.getIndent(), equalTo(0));
        assertThat(empty, hasToString(""));

        final Code indented = empty.indentBy(2);
        assertThat(indented.getIndent(), equalTo(2));
        assertThat(indented, hasToString(""));

        final Code added = indented.add(this.make("//foobar"));
        assertThat(added.getIndent(), equalTo(2));
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
        assertThat(hasBeenUsed.value, equalTo(FALSE));
        code.toString();
        assertThat(hasBeenUsed.value, equalTo(TRUE));
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
        this.make(format, this.make("foo"), this.make("bar"));
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
        code.add(format, this.make("foo"), this.make("bar"));
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
        final Code emptyIndented = emptyCode().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code added = emptyIndented.add(this.make("//whatever"));
        assertThat(added.getIndent(), equalTo(i));
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
        final Code emptyIndented = emptyCode().indentBy(i);
        assert emptyIndented.getIndent() == i : format("%s != %s", emptyIndented.getIndent(), i);
        assert emptyIndented.toString().equals("");

        final Code emptyCascade = emptyIndented.add(emptyIndented);
        assert emptyCascade.getIndent() == i : format("%s != %s", emptyCascade.getIndent(), i);
        assert emptyCascade.toString().equals("");

        final Code added = emptyCascade.add(this.make("//whatever"));
        assertThat(added.getIndent(), equalTo(i));
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
        final Code emptyIndented = emptyCode().indentBy(i);
        assert emptyIndented.getIndent() == i;
        assert emptyIndented.toString().equals("");

        final Code combined = combine(emptyIndented, this.make("//whatever"));
        assertThat(combined.getIndent(), equalTo(0));
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
        final Code emptyIndented = emptyCode().indentBy(i);
        assert emptyIndented.getIndent() == i : format("%s != %s", emptyIndented.getIndent(), i);
        assert emptyIndented.toString().equals("");

        final Code emptyCascade = emptyIndented.add(emptyIndented);
        assert emptyCascade.getIndent() == i : format("%s != %s", emptyCascade.getIndent(), i);
        assert emptyCascade.toString().equals("");

        final Code combined = combine(emptyCascade, this.make("//whatever"));
        assertThat(combined.getIndent(), equalTo(0));
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
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("//whatever%n")));

        final Code emptyIndented = emptyCode().indentBy(i);

        final Code added = code.add(emptyIndented);
        assertThat(added.getIndent(), equalTo(0));
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

    @Test
    public void testEmptyLines()
    throws Exception {
        final Code empty = this.make("");
        assertThat(empty, hasToString(format("%n")));
        final Code single = combine(empty);
        assertThat(single, hasToString(format("%n")));
        final Code twin = combine(empty, empty);
        assertThat(twin, hasToString(format("%n%n")));
    }

    @Test
    public void testSpace()
    throws Exception {
        final Code space = this.make(" ");
        assertThat(space, hasToString(format(" %n")));
    }

    @Test
    public void testZeroIndent()
    throws Exception {
        final Code nothing = this.make("%s", i(0));
        assertThat(nothing, hasToString(format("%n")));
    }

    @Test
    public void testOneIndent()
    throws Exception {
        final Code nothing = this.make("%s", i(1));
        assertThat(nothing, hasToString(format("    %n")));
    }

    @Test
    public void testInfixEmptyLines()
    throws Exception {
        final Code code = this.make("//whatever").add("").add("//whatelse");
        assertThat(code.getIndent(), equalTo(0));
        assertThat(code, hasToString(format("//whatever%n%n//whatelse%n")));
    }

    @Test
    public void testMultipleIndentionMarkersCombinations()
    throws Exception {
        final Code e1 = emptyCode();
        assertThat(e1.getIndent(), equalTo(0));
        assertThat(e1, hasToString(""));
        assertThat(e1, instanceOf(IndentionMarkerCode.class));

        final Code e2 = emptyCode();
        assertThat(e2.getIndent(), equalTo(0));
        assertThat(e2, hasToString(""));
        assertThat(e2, instanceOf(IndentionMarkerCode.class));

        final Code added = e1.add(e2);
        assertThat(added.getIndent(), equalTo(0));
        assertThat(added, hasToString(""));
        assertThat(added, instanceOf(IndentionMarkerCode.class));

        final Code combo = combine(e1, e2);
        assertThat(combo.getIndent(), equalTo(0));
        assertThat(combo, hasToString(""));
        assertThat(combo, instanceOf(IndentionMarkerCode.class));

        final Code indented = added.indent();
        assertThat(indented.getIndent(), equalTo(1));
        assertThat(indented, hasToString(""));
        assertThat(indented, instanceOf(IndentionMarkerCode.class));

        final Code comboAndIndented = added.add(indented);
        assertThat(comboAndIndented.getIndent(), equalTo(0));
        assertThat(comboAndIndented, hasToString(""));
        assertThat(comboAndIndented, instanceOf(IndentionMarkerCode.class));

        final Code indentedAndCombo = indented.add(added);
        assertThat(indentedAndCombo.getIndent(), equalTo(1));
        assertThat(indentedAndCombo, hasToString(""));
        assertThat(indentedAndCombo, instanceOf(IndentionMarkerCode.class));

        final Code mashup = combine(e1, e2, added, combo, indented, comboAndIndented, indentedAndCombo);
        assertThat(mashup.getIndent(), equalTo(0));
        assertThat(mashup, hasToString(""));
        assertThat(mashup, instanceOf(IndentionMarkerCode.class));
    }

    @Test
    public void testEmptyCodeIndentAdd()
    throws Exception {
        final Code added = emptyCode().indentBy(2).add("//foobar");
        assertThat(added.getIndent(), equalTo(2));
        assertThat(added, hasToString(format("        //foobar%n")));
    }

    @Test
    public void testAddingIndentZeroCode()
    throws Exception {
        final Code code = this.make("Object obj = null;").indentBy(2);
        assertThat(code.getIndent(), equalTo(2));
        assertThat(code, hasToString(format("        Object obj = null;%n")));

        final Code additional = this.make("// some single comment").setIndent(0);
        final Code added = code.add(additional);
        assertThat(added.getIndent(), equalTo(2));
        assertThat(added.getIndent(), equalTo(code.getIndent()));
        assertThat(added, hasToString(format("        Object obj = null;%n        // some single comment%n")));
    }

    @Test
    public void testAddingIndentOneCode()
    throws Exception {
        final Code code = this.make("Object obj = null;").indentBy(2);
        assertThat(code.getIndent(), equalTo(2));
        assertThat(code, hasToString(format("        Object obj = null;%n")));

        final Code additional = this.make("// some single comment").setIndent(1);
        final Code added = code.add(additional);
        assertThat(added.getIndent(), equalTo(2));
        assertThat(added.getIndent(), equalTo(code.getIndent()));
        assertThat(added, hasToString(format("        Object obj = null;%n            // some single comment%n")));
    }

    @Test
    public void testCombineIndentZeroCode()
    throws Exception {
        final Code code = this.make("Object obj = null;").indentBy(2);
        assertThat(code.getIndent(), equalTo(2));
        assertThat(code, hasToString(format("        Object obj = null;%n")));

        final Code additional = this.make("// some single comment").setIndent(0);
        final Code combo = combine(code, additional);
        assertThat(combo.getIndent(), equalTo(0));
        assertThat(combo.getIndent(), equalTo(additional.getIndent()));
        assertThat(combo, hasToString(format("        Object obj = null;%n// some single comment%n")));
    }

    @Test
    public void testCombineIndentOneCode()
    throws Exception {
        final Code code = this.make("Object obj = null;").indentBy(2);
        assertThat(code.getIndent(), equalTo(2));
        assertThat(code, hasToString(format("        Object obj = null;%n")));

        final Code additional = this.make("// some single comment").setIndent(1);
        final Code combo = combine(code, additional);
        assertThat(combo.getIndent(), equalTo(1));
        assertThat(combo.getIndent(), equalTo(additional.getIndent()));
        assertThat(combo, hasToString(format("        Object obj = null;%n    // some single comment%n")));
    }

}
