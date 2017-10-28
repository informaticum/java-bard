package de.informaticum.javabard.api;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import java.util.Formattable;
import org.junit.Test;

public class LiteralFormattableTests {

    @Test
    public void testStringifiedNull()
    throws Exception {
        final Formattable f = new LiteralFormattable(null);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("null"));

    }

    @Test
    public void testStringifiedHelloWorld()
    throws Exception {
        final Formattable f = new LiteralFormattable("Hello world!");
        final String s = String.format("%s", f);
        assertThat(s, hasToString("Hello world!"));

    }

    @Test
    public void testStringifiedInt()
    throws Exception {
        final Formattable f = new LiteralFormattable(14);
        final String s = String.format("%s", f);
        assertThat(s, hasToString("14"));

    }

}
