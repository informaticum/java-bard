package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Util.nonNull;
import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import java.util.function.Supplier;
import java.util.stream.Stream;
import de.informaticum.javabard.api.deprecated.Indentable;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code
extends Indentable<Code> {

    // fabric methods

    public static Code code(final String format, final Object... args)
    throws IllegalArgumentException {
        return new SingleCode(nonNull(format), nonNull(args));
    }

    // instance's methods

    public default Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        final Code code = code(nonNull(format), nonNull(args)).indent(this.getIndent());
        return this.add(code);
    }

    public default Code add(final Code... codes)
    throws IllegalArgumentException {
        final Code[] combined = concat(Stream.of(this), stream(nonNull(codes)).map(Util::nonNull)).toArray(Code[]::new);
        return new MultiCode(combined);
    }

    public default Code add(final Supplier<? extends Code>... codes)
    throws IllegalArgumentException {
        final Code[] resolved = stream(nonNull(codes)).map(Util::nonNull).map(Supplier::get).map(Util::nonNull).toArray(Code[]::new);
        return this.add(resolved);
    }

}
