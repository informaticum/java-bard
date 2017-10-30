package de.informaticum.javabard.api;

import static java.util.FormattableFlags.LEFT_JUSTIFY;
import java.util.Formattable;
import java.util.Formatter;
import java.util.function.Supplier;

public interface ClassNameEmitter
extends Formattable, Supplier<Class<?>> {

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String format = ((flags & LEFT_JUSTIFY) == LEFT_JUSTIFY) ? "%-" + width + "s" : "%s";
        final String argument = this.get().getCanonicalName();
        formatter.format(format, argument);
    }

}
