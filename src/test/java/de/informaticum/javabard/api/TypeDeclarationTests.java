package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.TypeDeclaration.declare;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.ANNOTATION;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.CLASS;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.ENUM;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.INTERFACE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.List;
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

    @Test
    public void testSimpleAnnotationDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(ANNOTATION, "FooBar").addModifier(PUBLIC);
        assertThat(def, hasToString(format("public @interface FooBar {%n}%n")));
    }

    @Test
    public void testSimpleInterfaceDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(INTERFACE, "FooBar").addModifier(PROTECTED, STATIC, ABSTRACT);
        assertThat(def, hasToString(format("protected abstract static interface FooBar {%n}%n")));
    }

    @Test
    public void testSimpleClassDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(CLASS, "FooBar").addModifier(PRIVATE, ABSTRACT);
        assertThat(def, hasToString(format("private abstract class FooBar {%n}%n")));
    }

    @Test
    public void testSimpleEnumDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(ENUM, "FooBar").addModifier(PUBLIC);
        assertThat(def, hasToString(format("public enum FooBar {%n}%n")));
    }

}
