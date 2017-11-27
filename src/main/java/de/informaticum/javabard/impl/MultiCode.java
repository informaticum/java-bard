package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import de.informaticum.javabard.api.Code;

public class MultiCode
extends AbstractCode {

    private final List<Code> codes;

    private MultiCode(final Stream<? extends Code> codes)
    throws IllegalArgumentException {
        assert codes != null;
        this.codes = codes.collect(collectingAndThen(toList(), Collections::unmodifiableList));
        assert this.codes.size() > 0;
        assert this.codes.stream().noneMatch(MultiCode.class::isInstance);
    }

    @Override
    public Code indent(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new MultiCode(this.codes.stream().map(c -> c.indent(d)));
    }

    @Override
    public int getIndent() {
        return this.codes.stream().mapToInt(Code::getIndent).min().getAsInt();
    }

    @Override
    public String toString() {
        return this.codes.stream().map(Code::toString).collect(joining());
    }

    public static final class Builder {

        private final List<Code> codes;

        public Builder(final Code... codes) {
            allNonNull(codes);
            this.codes = new ArrayList<>();
            this.add(codes);
        }

        public final Builder add(final Code code)
        throws IllegalArgumentException {
            nonNull(code);
            if (code instanceof MultiCode) {
                (((MultiCode) code).codes).forEach(this::add);
            } else {
                this.codes.add(code);
            }
            return this;
        }

        public final Builder add(final Iterable<? extends Code> codes)
        throws IllegalArgumentException {
            codes.forEach(this::add);
            return this;
        }

        public final Builder add(final Code... codes)
        throws IllegalArgumentException {
            return this.add(asList(codes));
        }

        public final Code build() {
            if (this.codes.isEmpty()) {
                this.codes.add(new EmptyCodeIndentMemory(0));
            }
            return new MultiCode(this.codes.stream());
        }

    }

    private static final class EmptyCodeIndentMemory
    extends SingleCode {

        private EmptyCodeIndentMemory(final int indent) {
            super(indent, ""::toString);
        }

        @Override
        public Code indent(final int diff) {
            return new EmptyCodeIndentMemory(max(0, this.getIndent() + diff));
        }

        @Override
        public Code add(final Code code)
        throws IllegalArgumentException {
            nonNull(code);
            return code.indent(this.getIndent());
        }

    }

}
