package de.informaticum.javabard.api;

import java.util.Locale;

public abstract interface CodeBlock {

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

    public abstract String toString(final Locale locale);

}
