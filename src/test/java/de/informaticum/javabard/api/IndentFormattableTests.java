package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.indentation;
import static java.lang.String.format;
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
public class IndentFormattableTests {

    @Parameters(name = "{0}({1})")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] { { "%s", 0, "" }, //
                                       { "%s", 1, "    " }, //
                                       { "%s", 2, "        " }, //
                                       { "%s", 4, "                " }, //
        });
    }

    @Parameter(0)
    public String format;

    @Parameter(1)
    public int depth;

    @Parameter(2)
    public String expected;

    @Test
    public void testStringifiedIndent()
    throws Exception {
        final Formattable emitter = indentation(this.depth);
        final String actual = format(this.format, emitter);
        assertThat(actual, hasToString(this.expected));
    }

}
