package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.nonNull;
import java.util.Locale;
import de.informaticum.javabard.api.Code;

public abstract class AbstractCode
implements Code {

    @Override
    public Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return this.add(code(format, args));
    }

    @Override
    public Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return this.add(code(locale, format, args));
    }

    @Override
    public Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return combine(this, code.indent(this.getIndent()));
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

    public static Code combine(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder().add(codes).build();
    }

    public static Code combine(final Code... codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder(codes).build();
    }

    public static Code combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder(code).add(codes).build();
    }

    public static Code combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder(code).add(codes).build();
    }

}
