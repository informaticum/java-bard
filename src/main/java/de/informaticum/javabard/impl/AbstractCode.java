package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import java.util.Locale;
import de.informaticum.javabard.api.Code;

public abstract class AbstractCode
implements Code {

    @Override
    public Code indentNext(final int diff) {
        final int d = max(diff, -this.getIndent());
        final Code current = (d < diff) ? this.indent(diff - d) : this;
        final int nextIndent = current.getIndent() + diff;
        return new StackCode.Builder(current, nextIndent).get();
    }

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
        return new MultiCode.Builder(this, code.indent(this.getIndent())).get();
    }

}
