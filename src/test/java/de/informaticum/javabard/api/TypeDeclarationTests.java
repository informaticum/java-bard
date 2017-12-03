package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.TypeDeclaration.declare;
import static de.informaticum.javabard.api.TypeDeclaration.Kind.CLASS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
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
    public void testSimpleClassDeclaration()
    throws Exception {
        final TypeDeclaration def = this.make(CLASS, "FooBar");
        assertThat(def, hasToString(format("class FooBar {%n}%n")));
    }

}
