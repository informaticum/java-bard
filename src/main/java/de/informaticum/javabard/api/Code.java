package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code
extends Indentable<Code>, LocalisableToString {

    public static Code code(final String format, final Object... args) {
        return new SingleCode(requireNonNull(format), requireNonNull(args));
    }

    public default Code add(final String format, final Object... args) {
        final Code code = code(requireNonNull(format), requireNonNull(args)).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Code code) {
        return new MultiCode(this, requireNonNull(code));
    }

    public default Code add(final Supplier<? extends Code> code) {
        return new MultiCode(this, requireNonNull(requireNonNull(code).get()));
    }

}
