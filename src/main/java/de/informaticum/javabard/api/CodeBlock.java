package de.informaticum.javabard.api;

public abstract interface CodeBlock {

    public abstract CodeBlock add(final String format, final Object... args);

    public abstract CodeBlock add(final CodeBlock code);

    public static final String DEFAULT_INDENT_CHARS = "    ";

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

}
