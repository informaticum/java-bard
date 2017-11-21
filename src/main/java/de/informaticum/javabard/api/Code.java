package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import java.util.Collection;
import java.util.Locale;
import de.informaticum.javabard.api.deprecated.Indentable;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code
extends Indentable<Code> {

    public default Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        final Code code = new SingleCode.Builder(format, args).build().indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        final Code code = new SingleCode.Builder(format, args).setLocale(locale).build().indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return new MultiCode.Builder().add(this).add(code).build();
    }

    public default Code addAll(final Collection<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder().add(this).add(codes).build();
    }

    public default Code addAll(final Code... codes)
    throws IllegalArgumentException {
        nonNull(codes);
        return new MultiCode.Builder().add(this).add(codes).build();
    }

    /* Fabric methods */

    public static SingleCode code(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).build();
    }

    public static SingleCode code(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).setLocale(locale).build();
    }

    public static MultiCode combine(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder().add(codes).build();
    }

    public static MultiCode combine(final Code... codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder().add(codes).build();
    }

    public static MultiCode combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder().add(code).add(codes).build();
    }

    public static MultiCode combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder().add(code).add(codes).build();
    }

}
