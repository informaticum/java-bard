package de.informaticum.javabard.impl;

import static de.informaticum.javabard.impl.MultiCode.combine;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.function.Supplier;
import de.informaticum.javabard.api.Code;

public class AgglutinateCode
extends CodeSequence {

    private final Code before;

    private final Code current;

    @Override
    public List<? extends Code> getCodes() {
        return asList(this.before, this.current);
    }

    public AgglutinateCode(final Code before, final Code current) {
        assert before != null;
        assert current != null;
        this.before = before;
        this.current = current;
    }

    @Override
    public int getIndent() {
        return min(this.before.getIndent(), this.current.getIndent());
    }

    @Override
    public AgglutinateCode indent(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new AgglutinateCode(this.before.indent(d), this.current.indent(d));
    }

    @Override
    public Code indentNext(final int diff) {
        final int d = max(diff, -this.getIndent());
        final AgglutinateCode current = (d < diff) ? this.indent(diff - d) : this;
        final int nextIndent = current.getIndent() + diff;
        return new AgglutinateCode.Builder(combine(current.before, current.current), nextIndent).get();
    }

    @Override
    public Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return new AgglutinateCode(this.before, this.current.add(code));
    }

    @Override
    public String toString() {
        return this.before.toString() + this.current.toString();
    }

    public static final class Builder
    implements Supplier<Code> {

        private final Code before;

        private final Code current;

        public Builder(final Code before, final int nextIndent) {
            this.before = before;
            this.current = combine().setIndent(before.getIndent() + nextIndent);
        }

        @Override
        public Code get() {
            return new AgglutinateCode(this.before, this.current);
        }

    }
}
