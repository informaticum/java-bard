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
        final SingleCode code = new SingleCode.Builder(format, args).build();
        return this.add(code);
    }

    @Override
    public Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        final SingleCode code = new SingleCode.Builder(format, args).setLocale(locale).build();
        return this.add(code);
    }

    @Override
    public Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        final int indent = this.getIndent();
        return new MultiCode.Builder().add(this).add(code.indent(indent)).build();
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
        return new MultiCode.Builder(codes).build();
    }

    public static MultiCode combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder(code).add(codes).build();
    }

    public static MultiCode combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        return new MultiCode.Builder(code).add(codes).build();
    }

}
