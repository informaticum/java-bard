package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.util.Locale;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code {

    public static Code code(final String format, final Object... args) {
        return new SingleCode(requireNonNull(format), requireNonNull(args));
    }

    public default Code add(final String format, final Object... args) {
        final Code code = code(requireNonNull(format), requireNonNull(args)).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Code code) {
        return new MultiCode(this, requireNonNull(code));
    }

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
