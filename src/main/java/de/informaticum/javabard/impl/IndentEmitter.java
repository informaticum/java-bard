package de.informaticum.javabard.impl;

import static de.informaticum.javabard.api.CodeBlock.DEFAULT_INDENT_CHARS;
import static java.lang.String.join;
import static java.util.Collections.nCopies;
import java.util.Formattable;
import java.util.Formatter;
import java.util.function.IntSupplier;

@FunctionalInterface
public abstract interface IndentEmitter
extends Formattable, IntSupplier {

    @Override
    public default void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        final String argument = join("", nCopies(this.getAsInt(), DEFAULT_INDENT_CHARS));
        formatter.format("%s", argument);
    }

}
