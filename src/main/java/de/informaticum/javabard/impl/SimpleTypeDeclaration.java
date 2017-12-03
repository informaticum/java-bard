package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.nonEmptyIdentifier;
import de.informaticum.javabard.api.Code;
import de.informaticum.javabard.api.TypeDeclaration;

public class SimpleTypeDeclaration
implements TypeDeclaration {

    private String name = null;

    public SimpleTypeDeclaration(final String name)
    throws IllegalArgumentException {
        this.setName(name);
    }

    @Override
    public Code get() {
        return SingleCode.code("class %s {", this.name) //
                         .add("}");
    }

    @Override
    public TypeDeclaration setName(final String name)
    throws IllegalArgumentException {
        this.name = nonEmptyIdentifier(name);
        return this;
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

    public static final TypeDeclaration declare(final String name)
    throws IllegalArgumentException {
        return new SimpleTypeDeclaration(name);
    }

}
