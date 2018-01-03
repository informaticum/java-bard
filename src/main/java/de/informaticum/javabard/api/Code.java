package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.util.Arrays.asList;
import java.util.IllegalFormatException;
import java.util.Locale;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code {

    public abstract int getIndent();

    public default Code setIndent(final int indent) {
        return this.indentBy(max(0, indent) - this.getIndent());
    }

    public abstract Code indentBy(final int diff);

    public default Code indent() {
        return this.indentBy(+1);
    }

    public default Code unindent() {
        return this.indentBy(-1);
    }

    public abstract Code add(final String format, final Object... args)
    throws IllegalArgumentException;

    public abstract Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException;

    public abstract Code add(final Code code)
    throws IllegalArgumentException;

    public default Code addAll(final Iterable<? extends Code> codes)
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
        return this.addAll(asList(codes));
    }

    @Override
    public abstract String toString()
    throws IllegalFormatException;

    // fabric methods

    static Code emptyCode() {
        return new MultiCode.Builder().get();
    }

    static Code code(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).get();
    }

    static Code code(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).setLocale(locale).get();
    }

    static Code combine(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder().add(codes).get();
    }

    static Code combine(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder(codes).get();
    }

    static Code combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

    static Code combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

}
