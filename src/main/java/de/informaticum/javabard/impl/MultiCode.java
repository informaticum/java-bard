package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import de.informaticum.javabard.api.Code;

public class MultiCode
extends AbstractCodeSequence {

    private final List<Code> codes;

    protected MultiCode(final Stream<? extends Code> codes)
    throws IllegalArgumentException {
        assert codes != null;
        this.codes = codes.collect(collectingAndThen(toList(), Collections::unmodifiableList));
        assert this.codes.size() > 0;
        assert this.codes.stream().filter(Builder.ZERO_INDENT_MARKER_CODE::equals).count() <= 1;
        assert this.codes.stream().noneMatch(CodeSequence.class::isInstance);
    }

    @Override
    public MultiCode indentBy(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new MultiCode(this.codes.stream().map(c -> c.indentBy(d)));
    }

    @Override
    public List<? extends Code> getCodes() {
        return this.codes;
    }

    public static final class Builder
    implements Supplier<Code> {

        private static final Code ZERO_INDENT_MARKER_CODE = new IndentionMarkerCode(0);

        private final List<Code> codes = new ArrayList<>();

        public Builder(final Code... codes) {
            allNonNull(codes);
            this.add(codes);
        }

        public final Builder add(final Code code)
        throws IllegalArgumentException {
            nonNull(code);
            if (ZERO_INDENT_MARKER_CODE.equals(code)) {
                // excrescent
            } else if (code instanceof CodeSequence) {
                ((CodeSequence) code).getCodes().forEach(this::add);
            } else {
                this.codes.add(code);
            }
            return this;
        }

        public final Builder add(final Iterable<? extends Code> codes)
        throws IllegalArgumentException {
            allNonNull(codes);
            codes.forEach(this::add);
            return this;
        }

        public final Builder add(final Code... codes)
        throws IllegalArgumentException {
            allNonNull(codes);
            return this.add(asList(codes));
        }

        @Override
        public final Code get() {
            if (this.codes.isEmpty()) {
                return ZERO_INDENT_MARKER_CODE;
            } else if (this.codes.size() == 1) {
                return this.codes.get(0);
            } else {
                return new MultiCode(this.codes.stream());
            }
        }

    }

}
