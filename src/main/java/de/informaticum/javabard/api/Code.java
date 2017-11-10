package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.util.Arrays.asList;
import static java.util.stream.Stream.concat;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;
import de.informaticum.javabard.api.deprecated.Indentable;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code
extends Indentable<Code> {

    public default Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        final Code code = new SingleCode(format, args).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        final Code code = new SingleCode(locale, format, args).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Collection<Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        final Code[] combined = concat(Stream.of(this), codes.stream()).toArray(Code[]::new);
        return new MultiCode(combined);
    }

    public default Code add(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return this.add(asList(codes));
    }

}
