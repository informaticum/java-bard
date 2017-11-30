package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import java.util.Collection;
import java.util.Locale;

public abstract interface Code {

    public abstract int getIndent();

    public default Code setIndent(final int indent) {
        return this.indent(indent - this.getIndent());
    }

    public abstract Code indent(final int diff);

    public default Code indent() {
        return this.indent(+1);
    }

    public default Code unindent() {
        return this.indent(-1);
    }

    public abstract Code indentNext(final int diff);

    public default Code indentNext() {
        return this.indentNext(+1);
    }

    public default Code unindentNext() {
        return this.indentNext(-1);
    }

    public abstract Code add(final String format, final Object... args)
    throws IllegalArgumentException;

    public abstract Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException;

    public abstract Code add(final Code code)
    throws IllegalArgumentException;

    public default Code addAll(final Collection<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        Code code = this;
        for (final Code c : codes) {
            code = code.add(c);
        }
        return code;
    }

    public default Code addAll(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        Code code = this;
        for (final Code c : codes) {
            code = code.add(c);
        }
        return code;
    }

}
