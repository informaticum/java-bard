package de.informaticum.javabard.backlog;

import static de.informaticum.javabard.impl.JavaStringEmitter.ESCAPED_QUOTE;
import static de.informaticum.javabard.impl.JavaStringEmitter.QUOTE;
import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;

public interface JavaStringFormattable
extends Formattable {

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String argument = valueOf(this).replace(QUOTE, ESCAPED_QUOTE);
        formatter.format("%s%s%s", QUOTE, argument, QUOTE);
    }

}
