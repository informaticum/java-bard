package de.informaticum.javabard.api;

import java.util.function.Supplier;

public abstract interface TypeDeclaration
extends Supplier<Code> {

    public abstract TypeDeclaration setName(final String name);

    @Override
    public abstract String toString();

}
