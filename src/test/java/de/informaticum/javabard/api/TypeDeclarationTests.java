package de.informaticum.javabard.api;

import static de.informaticum.javabard.impl.SimpleTypeDeclaration.declare;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.NATIVE;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.STRICTFP;
import static javax.lang.model.element.Modifier.SYNCHRONIZED;
import static javax.lang.model.element.Modifier.TRANSIENT;
import static javax.lang.model.element.Modifier.VOLATILE;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.List;
import java.util.function.Function;
import de.informaticum.javabard.impl.SimpleTypeDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TypeDeclarationTests {

    @Parameters
    public static List<Function<String, TypeDeclaration>> codeFactories() {
        final Function<String, TypeDeclaration> dec = (n) -> declare(n);
        final Function<String, TypeDeclaration> con = (n) -> new SimpleTypeDeclaration(n);
        final Function<String, TypeDeclaration> set = (n) -> new SimpleTypeDeclaration(n).setName(n);
        return asList(dec, con, set);
    }

    @Parameter(0)
    public Function<String, TypeDeclaration> factory;

    private TypeDeclaration make(final String name) {
        return this.factory.apply(name);
    }

    @Test
    public void testSimpleClassDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make("FooBar");
        assertThat(def, hasToString(format("class FooBar {%n}%n")));
    }

    @Test
    public void acceptNameStartingWithUnderline()
    throws Exception {
        this.make("_Foobar");
    }

    @Test
    public void acceptNameStartingWithDollar()
    throws Exception {
        this.make("$Foobar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_abstract()
    throws Exception {
        this.make(ABSTRACT.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_assert()
    throws Exception {
        this.make("assert");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_boolean()
    throws Exception {
        this.make(boolean.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_break()
    throws Exception {
        this.make("break");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_byte()
    throws Exception {
        this.make(byte.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_case()
    throws Exception {
        this.make("case");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_catch()
    throws Exception {
        this.make("catch");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_char()
    throws Exception {
        this.make(char.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_class()
    throws Exception {
        this.make("class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_const()
    throws Exception {
        this.make("const");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_continue()
    throws Exception {
        this.make("continue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_default()
    throws Exception {
        this.make(DEFAULT.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_do()
    throws Exception {
        this.make("do");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_double()
    throws Exception {
        this.make(double.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_else()
    throws Exception {
        this.make("else");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_enum()
    throws Exception {
        this.make("enum");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_extends()
    throws Exception {
        this.make("extends");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_final()
    throws Exception {
        this.make(FINAL.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_finally()
    throws Exception {
        this.make("finally");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_float()
    throws Exception {
        this.make(float.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_for()
    throws Exception {
        this.make("for");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_goto()
    throws Exception {
        this.make("goto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_if()
    throws Exception {
        this.make("if");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_implements()
    throws Exception {
        this.make("implements");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_import()
    throws Exception {
        this.make("import");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_instanceof()
    throws Exception {
        this.make("instanceof");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_int()
    throws Exception {
        this.make(int.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_interface()
    throws Exception {
        this.make("interface");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_long()
    throws Exception {
        this.make(long.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_native()
    throws Exception {
        this.make(NATIVE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_new()
    throws Exception {
        this.make("new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_package()
    throws Exception {
        this.make("package");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_private()
    throws Exception {
        this.make(PRIVATE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_protected()
    throws Exception {
        this.make(PROTECTED.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_public()
    throws Exception {
        this.make(PUBLIC.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_return()
    throws Exception {
        this.make("return");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_short()
    throws Exception {
        this.make(short.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_static()
    throws Exception {
        this.make(STATIC.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_strictfp()
    throws Exception {
        this.make(STRICTFP.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_super()
    throws Exception {
        this.make("super");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_switch()
    throws Exception {
        this.make("switch");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_synchronized()
    throws Exception {
        this.make(SYNCHRONIZED.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_this()
    throws Exception {
        this.make("this");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_throw()
    throws Exception {
        this.make("throw");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_throws()
    throws Exception {
        this.make("throws");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_transient()
    throws Exception {
        this.make(TRANSIENT.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_try()
    throws Exception {
        this.make("try");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_void()
    throws Exception {
        this.make(void.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_volatile()
    throws Exception {
        this.make(VOLATILE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_while()
    throws Exception {
        this.make("while");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_false()
    throws Exception {
        this.make(FALSE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_null()
    throws Exception {
        this.make("null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_true()
    throws Exception {
        this.make(TRUE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineEnclosedName()
    throws Exception {
        this.make("Foo.Bar");
    }

}
