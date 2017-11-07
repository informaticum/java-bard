package de.informaticum.javabard.api;

import java.util.Locale;

public abstract interface Code {

    public abstract Code add(final String format, final Object... args);

    public abstract Code add(final Code code);

    public abstract Code indent(final int diff);

    public default Code indent() {
        return this.indent(+1);
    }

    public default Code unindent() {
        return this.indent(-1);
    }

    public abstract int getIndent();

    @Override
    public abstract String toString();

    public abstract String toString(final Locale locale);

}
