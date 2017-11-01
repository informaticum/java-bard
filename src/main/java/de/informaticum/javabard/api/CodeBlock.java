package de.informaticum.javabard.api;

import de.informaticum.javabard.impl.SingleCodeBlock;

public abstract interface CodeBlock {

    public static final String DEFAULT_INDENT = "    ";

    public abstract CodeBlock add(final String format, final Object... args);

    public abstract CodeBlock add(final CodeBlock code);

    public abstract CodeBlock indent();

    public abstract CodeBlock unindent();

    @Override
    public abstract String toString();

    public static CodeBlock of(final String format, final Object... args) {
        return SingleCodeBlock.of(format, args);
    }

    public static CodeBlock code(final String format, final Object... args) {
        return SingleCodeBlock.of(format, args);
    }

}
