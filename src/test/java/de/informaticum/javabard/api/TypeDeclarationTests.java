package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.TypeDeclaration.declare;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.CLASS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.PACKAGE;
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
import static javax.lang.model.type.TypeKind.BOOLEAN;
import static javax.lang.model.type.TypeKind.BYTE;
import static javax.lang.model.type.TypeKind.CHAR;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.FLOAT;
import static javax.lang.model.type.TypeKind.INT;
import static javax.lang.model.type.TypeKind.LONG;
import static javax.lang.model.type.TypeKind.NULL;
import static javax.lang.model.type.TypeKind.SHORT;
import static javax.lang.model.type.TypeKind.VOID;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import de.informaticum.javabard.api.TypeDeclaration.Kind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TypeDeclarationTests {

    @Parameters
    public static List<BiFunction<Kind, String, TypeDeclaration>> codeFactories() {
        final BiFunction<Kind, String, TypeDeclaration> dec = (k, n) -> declare(k, n);
        final BiFunction<Kind, String, TypeDeclaration> con = (k, n) -> new TypeDeclaration(k, n);
        final BiFunction<Kind, String, TypeDeclaration> set = (k, n) -> new TypeDeclaration(k, n).setKind(k).setName(n);
        return asList(dec, con, set);
    }

    @Parameter(0)
    public BiFunction<Kind, String, TypeDeclaration> factory;

    private TypeDeclaration make(final Kind kind, final String name) {
        return this.factory.apply(kind, name);
    }

    private TypeDeclaration make(final Kind kind, final Enum<?> enumm) {
        return this.make(kind, enumm.name().toLowerCase(Locale.US));
    }

    @Test
    public void testSimpleClassDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(CLASS, "FooBar");
        assertThat(def, hasToString(format("class FooBar {%n}%n")));
    }

    @Test
    public void acceptNameStartingWithUnderline()
    throws Exception {
        this.make(CLASS, "_Foobar");
    }

    @Test
    public void acceptNameStartingWithDollar()
    throws Exception {
        this.make(CLASS, "$Foobar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_abstract()
    throws Exception {
        this.make(CLASS, ABSTRACT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_assert()
    throws Exception {
        this.make(CLASS, "assert");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_boolean()
    throws Exception {
        this.make(CLASS, BOOLEAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_break()
    throws Exception {
        this.make(CLASS, "break");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_byte()
    throws Exception {
        this.make(CLASS, BYTE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_case()
    throws Exception {
        this.make(CLASS, "case");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_catch()
    throws Exception {
        this.make(CLASS, "catch");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_char()
    throws Exception {
        this.make(CLASS, CHAR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_class()
    throws Exception {
        this.make(CLASS, CLASS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_const()
    throws Exception {
        this.make(CLASS, "const");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_continue()
    throws Exception {
        this.make(CLASS, "continue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_default()
    throws Exception {
        this.make(CLASS, DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_do()
    throws Exception {
        this.make(CLASS, "do");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_double()
    throws Exception {
        this.make(CLASS, DOUBLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_else()
    throws Exception {
        this.make(CLASS, "else");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_enum()
    throws Exception {
        this.make(CLASS, ENUM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_extends()
    throws Exception {
        this.make(CLASS, "extends");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_final()
    throws Exception {
        this.make(CLASS, FINAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_finally()
    throws Exception {
        this.make(CLASS, "finally");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_float()
    throws Exception {
        this.make(CLASS, FLOAT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_for()
    throws Exception {
        this.make(CLASS, "for");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_goto()
    throws Exception {
        this.make(CLASS, "goto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_if()
    throws Exception {
        this.make(CLASS, "if");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_implements()
    throws Exception {
        this.make(CLASS, "implements");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_import()
    throws Exception {
        this.make(CLASS, "import");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_instanceof()
    throws Exception {
        this.make(CLASS, "instanceof");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_int()
    throws Exception {
        this.make(CLASS, INT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_interface()
    throws Exception {
        this.make(CLASS, INTERFACE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_long()
    throws Exception {
        this.make(CLASS, LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_native()
    throws Exception {
        this.make(CLASS, NATIVE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_new()
    throws Exception {
        this.make(CLASS, "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_package()
    throws Exception {
        this.make(CLASS, PACKAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_private()
    throws Exception {
        this.make(CLASS, PRIVATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_protected()
    throws Exception {
        this.make(CLASS, PROTECTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_public()
    throws Exception {
        this.make(CLASS, PUBLIC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_return()
    throws Exception {
        this.make(CLASS, "return");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_short()
    throws Exception {
        this.make(CLASS, SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_static()
    throws Exception {
        this.make(CLASS, STATIC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_strictfp()
    throws Exception {
        this.make(CLASS, STRICTFP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_super()
    throws Exception {
        this.make(CLASS, "super");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_switch()
    throws Exception {
        this.make(CLASS, "switch");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_synchronized()
    throws Exception {
        this.make(CLASS, SYNCHRONIZED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_this()
    throws Exception {
        this.make(CLASS, "this");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_throw()
    throws Exception {
        this.make(CLASS, "throw");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_throws()
    throws Exception {
        this.make(CLASS, "throws");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_transient()
    throws Exception {
        this.make(CLASS, TRANSIENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_try()
    throws Exception {
        this.make(CLASS, "try");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_void()
    throws Exception {
        this.make(CLASS, VOID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_volatile()
    throws Exception {
        this.make(CLASS, VOLATILE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineKeyword_while()
    throws Exception {
        this.make(CLASS, "while");
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_false()
    throws Exception {
        this.make(CLASS, FALSE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_null()
    throws Exception {
        this.make(CLASS, NULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineLiteral_true()
    throws Exception {
        this.make(CLASS, TRUE.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineEnclosedName()
    throws Exception {
        this.make(CLASS, "Foo.Bar");
    }

}
