package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import de.informaticum.javabard.impl.SingleCodeBlock;

public abstract interface CodeBlock {

    public static final String DEFAULT_INDENT = "    ";

    public abstract CodeBlock add(final String format, final Object... args);

    public abstract CodeBlock add(final CodeBlock code);

    public abstract CodeBlock indent(int diff);

    public default CodeBlock indent() {
        return this.indent(+1);
    }

    public default CodeBlock unindent() {
        return this.indent(-1);
    }

    public abstract int getIndent();

    @Override
    public abstract String toString();

    public static CodeBlock of(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

    public static CodeBlock code(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

}
