package de.informaticum.javabard.impl;

import static java.lang.Integer.compare;
import static java.lang.Math.max;

public final class IndentionMarkerCode
extends SingleCode
implements Comparable<IndentionMarkerCode> {

    public IndentionMarkerCode() {
        this(0);
    }

    private static final String EMPTY = "";

    private IndentionMarkerCode(final int indent) {
        super(indent, () -> EMPTY);
    }

    @Override
    public final IndentionMarkerCode indentBy(final int diff) {
        final int i = max(0, this.getIndent() + diff); // negative indent (a.k.a. unindent) must be capped
        return new IndentionMarkerCode(i);
    }

    @Override
    public final String toString() {
        return EMPTY;
    }

    @Override
    public int compareTo(final IndentionMarkerCode that) {
        return compare(this.getIndent(), that.getIndent());
    }

}
