package de.informaticum.javabard.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.concat;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import de.informaticum.javabard.api.deprecated.Indentable;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code
extends Indentable<Code> {

    // fabric methods

    public static Code code(final String format, final Object... args) {
        return new SingleCode(requireNonNull(format), requireNonNull(args));
    }

    // instance's methods

    public default Code add(final String format, final Object... args) {
        final Code code = code(requireNonNull(format), requireNonNull(args)).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Code... codes) {
        final Code[] combined = concat(Stream.of(this), stream(requireNonNull(codes)).map(Objects::requireNonNull)).toArray(Code[]::new);
        return new MultiCode(combined);
    }

    public default Code add(final Supplier<? extends Code>... codes) {
        final Code[] resolved = stream(requireNonNull(codes)).map(Objects::requireNonNull).map(Supplier::get).map(Objects::requireNonNull).toArray(Code[]::new);
        return this.add(resolved);
    }

}
