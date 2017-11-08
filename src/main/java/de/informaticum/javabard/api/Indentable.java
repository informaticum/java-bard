package de.informaticum.javabard.api;

public abstract interface Indentable<T> {

    public abstract T indent(final int diff);

    public default T indent() {
        return this.indent(+1);
    }

    public default T unindent() {
        return this.indent(-1);
    }

    public abstract int getIndent();

}
