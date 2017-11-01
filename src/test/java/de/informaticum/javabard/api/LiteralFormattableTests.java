package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.FormattableEmitters.literal;
import static java.lang.String.valueOf;
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
public class LiteralFormattableTests {

    @Parameters(name = "{0}({1})")
    public static Iterable<Object[]> data() {
        return asList(new Object[][] { { "%s", null }, //
                                       { "%s", "Hello world!" }, //
                                       { "%s", "6\" sandwich" }, //
                                       { "%s", (int) 14 }, //
        });
    }

    @Parameter(0)
    public String format;

    @Parameter(1)
    public Object argument;

    @Test
    public void testFormattedJavaString()
    throws Exception {
        final Formattable formattable = new LiteralFormattable() {
            public String toString() { return valueOf(LiteralFormattableTests.this.argument); }
        };
        final String actual = String.format(this.format, formattable);
        assertThat(actual, hasToString(valueOf(argument)));
    }

    @Test
    public void testEmittedJavaString()
    throws Exception {
        final Formattable emitter = literal(this.argument);
        final String actual = String.format(this.format, emitter);
        assertThat(actual, hasToString(valueOf(argument)));
    }

}
