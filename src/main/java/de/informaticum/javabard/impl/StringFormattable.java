package de.informaticum.javabard.impl;

import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;

public class StringFormattable
implements Formattable {

    private final Object anything;

    public StringFormattable(final Object anything) {
        this.anything = anything;
    }

    public static final char QUOTE = '"';

    private static final String UNQUOTED = valueOf(QUOTE);

    public static final char ESCAPE = '\\';

    private static final String QUOTED = valueOf(ESCAPE) + UNQUOTED;

    @Override
    public void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        formatter.format("%s%s%s", QUOTE, valueOf(this.anything).replace(UNQUOTED, QUOTED), QUOTE);
    }

}
