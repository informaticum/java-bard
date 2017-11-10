package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.allNonNull;
import static java.lang.Math.max;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import de.informaticum.javabard.api.Code;

public class MultiCode
implements Code {

    private final List<Code> codes;

    private MultiCode(final Stream<? extends Code> codes)
    throws IllegalArgumentException {
        assert codes != null;
        allNonNull(codes);
        this.codes = codes.flatMap(c -> (c instanceof MultiCode) ? ((MultiCode) c).codes.stream() : Stream.of(c)) //
                          .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public MultiCode(final Collection<? extends Code> codes)
    throws IllegalArgumentException {
        this(allNonNull(codes).stream());
    }

    public MultiCode(final Code... codes)
    throws IllegalArgumentException {
        this(stream(allNonNull(codes)));
    }

    @Override
    public Code indent(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new MultiCode(this.codes.stream().map(c -> c.indent(d)));
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
