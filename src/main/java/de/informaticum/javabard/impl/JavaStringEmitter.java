package de.informaticum.javabard.impl;

import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;
import java.util.function.Supplier;

@FunctionalInterface
public abstract interface JavaStringEmitter
extends Formattable, Supplier<Object> {

    public static final String QUOTE = "\"";

    public static final String ESCAPED_QUOTE = "\\" + QUOTE;

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String argument = valueOf(this.get()).replace(JavaStringEmitter.QUOTE, JavaStringEmitter.ESCAPED_QUOTE);
        formatter.format("%s%s%s", JavaStringEmitter.QUOTE, argument, JavaStringEmitter.QUOTE);
    }

}
