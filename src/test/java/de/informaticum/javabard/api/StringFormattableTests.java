package de.informaticum.javabard.api;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.Formattable;
import org.junit.Test;

public class StringFormattableTests {

    @Test
    public void testStringifiedNull()
    throws Exception {
        final Formattable f = new StringFormattable(null);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"null\""));

    }

    @Test
    public void testStringifiedHelloWorld()
    throws Exception {
        final Formattable f = new StringFormattable("Hello world!");
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"Hello world!\""));

    }

    @Test
    public void testStringifiedInches()
    throws Exception {
        final Formattable f = new StringFormattable("6\" sandwich");
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"6\\\" sandwich\""));

    }

    @Test
    public void testStringifiedInt()
    throws Exception {
        final Formattable f = new StringFormattable(14);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("\"14\""));

    }

}
