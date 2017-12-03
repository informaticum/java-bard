package de.informaticum.javabard.api;

import static de.informaticum.javabard.impl.SimpleTypeDeclaration.declare;
import static java.lang.String.format;
import static java.util.Arrays.asList;
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

}
