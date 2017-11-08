package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import de.informaticum.javabard.api.Code;

public class MultiCode
implements Code {

    private final List<Code> codes = new ArrayList<>();

    public MultiCode() {
    }

    public MultiCode(final Code... codes) {
        requireNonNull(codes);
        for (final Code code : codes) {
            requireNonNull(code);
            if (code instanceof MultiCode) {
                this.codes.addAll(((MultiCode) code).codes);
            } else {
                this.codes.add(code);
            }
        }
    }

    public MultiCode(final Supplier<? extends Code>... codes) {
        this(stream(requireNonNull(codes)).map(Objects::requireNonNull).map(Supplier::get).map(Objects::requireNonNull).toArray(Code[]::new));
    }

    @Override
    public Code indent(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new MultiCode(this.codes.stream().map(c -> c.indent(d)).toArray(Code[]::new));
    }

    @Override
    public int getIndent() {
        return this.codes.stream().mapToInt(Code::getIndent).min().orElse(0);
    }

    @Override
    public String toString() {
        return this.codes.stream().map(Code::toString).collect(joining());
    }

}
