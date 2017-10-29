package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Formattables.string;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.Formattable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StringFormattableTests {

    @Parameters(name = "{0}({1})")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] { { "%s", null, "\"null\"" }, //
                                       { "%s", "Hello world!", "\"Hello world!\"" }, //
                                       { "%s", "6\" sandwich", "\"6\\\" sandwich\"" }, //
                                       { "%s", (int) 14, "\"14\"" }, //
        });
    }

    @Parameter(0)
    public String format;

    @Parameter(1)
    public Object argument;

    @Parameter(2)
    public String result;

    @Test
    public void testStringifiedString()
    throws Exception {
        final Formattable f = string(this.argument);
        final String s = String.format(this.format, f);
        assertThat(s, hasToString(this.result));
    }

}
