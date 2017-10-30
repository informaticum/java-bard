package de.informaticum.javabard.api;

import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;

public interface JavaStringFormattable
extends Formattable {

    public static final String QUOTE = "\"";

    public static final String ESCAPED_QUOTE = "\\" + QUOTE;

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String argument = valueOf(this).replace(QUOTE, ESCAPED_QUOTE);
        formatter.format("%s%s%s", QUOTE, argument, QUOTE);
    }

}
