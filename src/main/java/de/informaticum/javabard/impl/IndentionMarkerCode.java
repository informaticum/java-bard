package de.informaticum.javabard.impl;

import static java.lang.Math.max;

public final class IndentionMarkerCode
extends SingleCode {

    public IndentionMarkerCode() {
        this(0);
    }

    private static final String EMPTY = "";

    private IndentionMarkerCode(final int indent) {
        super(indent, () -> EMPTY);
    }

    @Override
    public IndentionMarkerCode indentBy(final int diff) {
        final int i = max(0, this.getIndent() + diff); // negative indent (a.k.a. unindent) must be capped
        return new IndentionMarkerCode(i);
    }

    @Override
    public String toString() {
        return EMPTY;
    }

}
