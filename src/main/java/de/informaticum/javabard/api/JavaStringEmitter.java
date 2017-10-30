package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.JavaStringFormattable.ESCAPED_QUOTE;
import static de.informaticum.javabard.api.JavaStringFormattable.QUOTE;
import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;
import java.util.function.Supplier;

public interface JavaStringEmitter
extends Formattable, Supplier<Object> {

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String argument = valueOf(this.get()).replace(QUOTE, ESCAPED_QUOTE);
        formatter.format("%s%s%s", QUOTE, argument, QUOTE);
    }

}
