package de.informaticum.javabard.impl;

import static java.lang.String.join;
import static java.lang.System.getProperty;
import static java.util.Collections.nCopies;
import java.util.Formattable;
import java.util.Formatter;
import java.util.function.IntSupplier;

@FunctionalInterface
public abstract interface IndentEmitter
extends Formattable, IntSupplier {

    public static final String INDENT_CHARS_PROPERTY = "javabard.indent";

    public static final String DEFAULT_INDENT_CHARS = "    ";

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final int level = this.getAsInt();
        final String chars = getProperty(INDENT_CHARS_PROPERTY, DEFAULT_INDENT_CHARS);
        final String argument = join("", nCopies(level, chars));
        formatter.format("%s", argument);
    }

}
