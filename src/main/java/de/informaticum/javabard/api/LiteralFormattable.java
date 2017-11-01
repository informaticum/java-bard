package de.informaticum.javabard.api;

import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;

public interface LiteralFormattable
extends Formattable {

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        formatter.format(valueOf(this));
    }

}
