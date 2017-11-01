package de.informaticum.javabard.api;

public abstract interface CodeBlock {

    public static final String DEFAULT_INDENT = "    ";

    public abstract CodeBlock add(final String format, final Object... args);

    public abstract CodeBlock add(final CodeBlock code);

    public abstract CodeBlock indent();

    public abstract CodeBlock unindent();

    @Override
    public abstract String toString();

}
