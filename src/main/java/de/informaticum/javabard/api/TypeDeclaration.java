package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.nonEmptyIdentifier;
import java.util.function.Supplier;
import de.informaticum.javabard.impl.SingleCode;

public class TypeDeclaration
implements Supplier<Code> {

    private String name = null;

    public TypeDeclaration(final String name)
    throws IllegalArgumentException {
        this.setName(name);
    }

    @Override
    public Code get() {
        return SingleCode.code("class %s {", this.name) //
                         .add("}");
    }

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
        return new TypeDeclaration(name);
    }

}
