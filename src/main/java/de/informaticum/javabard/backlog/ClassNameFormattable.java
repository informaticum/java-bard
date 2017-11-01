package de.informaticum.javabard.backlog;

import static java.util.FormattableFlags.LEFT_JUSTIFY;
import java.util.Formattable;
import java.util.Formatter;

public interface ClassNameFormattable
extends Formattable {

    public abstract String getCanonicalName();

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String format = ((flags & LEFT_JUSTIFY) == LEFT_JUSTIFY) ? "%-" + width + "s" : "%s";
        final String argument = this.getCanonicalName();
        formatter.format(format, argument);
    }

}
