package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import de.informaticum.javabard.api.Code;

public final class IndentionMarkerCode
extends SingleCode {

    public IndentionMarkerCode() {
        this(0);
    }

    private IndentionMarkerCode(final int indent) {
        super(indent, ""::toString);
    }

    @Override
    public IndentionMarkerCode indentBy(final int diff) {
        final int i = max(0, this.getIndent() + diff); // negative indent (a.k.a. unindent) must be capped
        return new IndentionMarkerCode(i);
    }

    @Override
    public String toString() {
        return "";
    }

}
