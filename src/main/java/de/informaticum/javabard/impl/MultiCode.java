package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.informaticum.javabard.api.Code;

public class MultiCode
extends AbstractCode {

    private final List<Code> codes = new ArrayList<>();

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
    public String toString(final Locale locale) {
        requireNonNull(locale);
        return this.codes.stream().map(c -> c.toString(locale)).collect(joining());
    }

}
