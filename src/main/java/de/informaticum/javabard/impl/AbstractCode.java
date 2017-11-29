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
        return this.add(new SingleCode.Builder(format, args).build());
    }

    @Override
    public Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return this.add(new SingleCode.Builder(format, args).setLocale(locale).build());
    }

    @Override
    public Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return new MultiCode.Builder(this, code.indent(this.getIndent())).build();
    }

}
