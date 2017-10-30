package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.typeName;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.Formattable;
import java.util.Map.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ClassNameFormattableTests {

    @Parameters(name = "{0}({1})")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] { { "%s", byte.class, "byte" }, //
                                       { "%s", byte[].class, "byte[]" }, //
                                       { "%s", Character.class, "java.lang.Character" }, //
                                       { "%s", Character[].class, "java.lang.Character[]" }, //
                                       { "%s", Entry.class, "java.util.Map.Entry" }, //
                                       { "%s", Entry[].class, "java.util.Map.Entry[]" }, //
                                       { "%-23s", byte.class, "byte                   " }, //
                                       { "%-23s", byte[].class, "byte[]                 " }, //
                                       { "%-23s", Character.class, "java.lang.Character    " }, //
                                       { "%-23s", Character[].class, "java.lang.Character[]  " }, //
                                       { "%-23s", Entry.class, "java.util.Map.Entry    " }, //
                                       { "%-23s", Entry[].class, "java.util.Map.Entry[]  " }, //
                                       { "%S", byte.class, "byte" }, //
                                       { "%S", byte[].class, "byte[]" }, //
                                       { "%S", Character.class, "java.lang.Character" }, //
                                       { "%S", Character[].class, "java.lang.Character[]" }, //
                                       { "%S", Entry.class, "java.util.Map.Entry" }, //
                                       { "%S", Entry[].class, "java.util.Map.Entry[]" }, //
                                       { "%-23S", byte.class, "byte                   " }, //
                                       { "%-23S", byte[].class, "byte[]                 " }, //
                                       { "%-23S", Character.class, "java.lang.Character    " }, //
                                       { "%-23S", Character[].class, "java.lang.Character[]  " }, //
                                       { "%-23S", Entry.class, "java.util.Map.Entry    " }, //
                                       { "%-23S", Entry[].class, "java.util.Map.Entry[]  " }, //
        });
    }

    @Parameter(0)
    public String format;

    @Parameter(1)
    public Class<?> argument;

    @Parameter(2)
    public String result;

    @Test
    public void testStringifiedClass()
    throws Exception {
        final Formattable f = typeName(this.argument);
        final String s = String.format(this.format, f);
        assertThat(s, hasToString(this.result));
    }

}
