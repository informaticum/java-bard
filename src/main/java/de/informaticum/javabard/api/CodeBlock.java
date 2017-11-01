package de.informaticum.javabard.api;

public abstract interface CodeBlock {

    public abstract CodeBlock add(final String format, final Object... args);

    @Override
    public abstract String toString();

}
