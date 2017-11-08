package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.util.Locale;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract class AbstractCode
implements Code {

    public static Code code(final String format, final Object... args) {
        return new SingleCode(requireNonNull(format), requireNonNull(args));
    }

    @Override
    public Code add(final String format, final Object... args) {
        return this.add(code(requireNonNull(format), requireNonNull(args)).indent(this.getIndent()));
    }

    @Override
    public Code add(final Code code) {
        return new MultiCode(this, requireNonNull(code));
    }

    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

}
