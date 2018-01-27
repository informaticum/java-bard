package de.informaticum.javabard.api;

import de.informaticum.javabard.impl.WrapperCode.AbstractWrapperCode;

public class PlainWrapperCode
extends AbstractWrapperCode {

    private final Code code;

    protected PlainWrapperCode(final Code code, final Code prefix, final String infixFormat, final Code suffix) {
        super(prefix, infixFormat, suffix);
        this.code = code;
    }

    protected PlainWrapperCode(final Code code, final AbstractWrapperCode blueprint) {
        super(blueprint);
        this.code = code;
    }

    @Override
    public Code getCode() {
        return this.code;
    }

    @Override
    public Code indentBy(final int diff) {
        return new PlainWrapperCode(this.code.indentBy(diff), this);
    }

}