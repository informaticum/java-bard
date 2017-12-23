package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.allNonNull;
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
        return this.add(new SingleCode.Builder(format, args).get());
    }

    @Override
    public Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return this.add(new SingleCode.Builder(format, args).setLocale(locale).get());
    }

    @Override
    public Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return new MultiCode.Builder(this, code.indentBy(this.getIndent())).get();
    }

    // fabric methods

    public static Code code(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).get();
    }

    public static Code code(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).setLocale(locale).get();
    }

    public static Code combine(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder().add(codes).get();
    }

    public static Code combine(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder(codes).get();
    }

    public static Code combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

    public static Code combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

    public static final Code emptyCode() {
        return new MultiCode.Builder().get();
    }

}
