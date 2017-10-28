package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Formattables.string;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.Formattable;
import org.junit.Test;

public class StringFormattableTests {

    @Test
    public void testStringifiedNull()
    throws Exception {
        final Formattable f = string(null);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"null\""));

    }

    @Test
    public void testStringifiedHelloWorld()
    throws Exception {
        final Formattable f = string("Hello world!");
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"Hello world!\""));

    }

    @Test
    public void testStringifiedInches()
    throws Exception {
        final Formattable f = string("6\" sandwich");
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"6\\\" sandwich\""));

    }

    @Test
    public void testStringifiedInt()
    throws Exception {
        final Formattable f = string(14);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"14\""));

    }

}
