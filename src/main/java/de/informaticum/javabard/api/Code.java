package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import java.util.Collection;
import java.util.Locale;
import de.informaticum.javabard.api.deprecated.Indentable;

public abstract interface Code
extends Indentable<Code> {

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
