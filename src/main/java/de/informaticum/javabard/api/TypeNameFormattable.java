package de.informaticum.javabard.api;

import static java.util.FormattableFlags.LEFT_JUSTIFY;
import java.util.Formattable;
import java.util.Formatter;

public interface TypeNameFormattable
extends Formattable {

    public abstract String getTypeName();

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String format = ((flags & LEFT_JUSTIFY) == LEFT_JUSTIFY) ? "%-" + width + "s" : "%s";
        final String argument = this.getTypeName();
        formatter.format(format, argument);
    }

}
