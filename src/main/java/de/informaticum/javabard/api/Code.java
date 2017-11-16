package de.informaticum.javabard.api;

import static de.informaticum.javabard.impl.MultiCode.Builder.combine;
import static de.informaticum.javabard.impl.SingleCode.Builder.code;
import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import java.util.Collection;
import java.util.Locale;
import de.informaticum.javabard.api.deprecated.Indentable;

public abstract interface Code
extends Indentable<Code> {

    public default Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        final Code code = code(format, args).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        final Code code = code(locale, format, args).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Collection<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return combine(this, codes);
    }

    public default Code add(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return combine(this, codes);
    }

}
