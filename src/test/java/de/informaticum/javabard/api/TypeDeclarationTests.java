package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.TypeDeclaration.declare;
import static java.lang.String.format;
import static javax.lang.model.element.ElementKind.ANNOTATION_TYPE;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TypeDeclarationTests {

    @Test
    public void testSimpleAnnotationDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(ANNOTATION_TYPE).with(PUBLIC).get();
        assertThat(def, hasToString(format("public abstract @interface Foobar {%n}%n")));
    }

    @Test
    public void testPureInterfaceDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(INTERFACE).get();
        assertThat(def, hasToString(format("abstract interface Foobar {%n}%n")));
    }

    @Test
    public void testPureClassDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(CLASS).get();
        assertThat(def, hasToString(format("class Foobar {%n}%n")));
    }

    @Test
    public void testSimpleInterfaceDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(INTERFACE).with(PROTECTED, STATIC, ABSTRACT).get();
        assertThat(def, hasToString(format("protected abstract static interface Foobar {%n}%n")));
    }

    @Test
    public void testSimpleClassDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(CLASS).with(PRIVATE, ABSTRACT).get();
        assertThat(def, hasToString(format("private abstract class Foobar {%n}%n")));
    }

    @Test
    public void testSimpleEnumDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").as(ENUM).with(PUBLIC).get();
        assertThat(def, hasToString(format("public enum Foobar {%n}%n")));
    }

    @Test
    public void testPackagedAnnotationDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").in(TypeDeclarationTests.class.getPackage()).as(ANNOTATION_TYPE).with(PUBLIC).get();
        assertThat(def, hasToString(format("package de.informaticum.javabard.api;%n%npublic abstract @interface Foobar {%n}%n")));
    }

    @Test
    public void testPackagedInterfaceDeclaration()
    throws Exception {
        final TypeDeclaration def = declare("Foobar").in("de.informaticum.javabard.api").as(INTERFACE).with(PROTECTED, STATIC, ABSTRACT).get();
        assertThat(def, hasToString(format("package de.informaticum.javabard.api;%n%nprotected abstract static interface Foobar {%n}%n")));
    }

}
