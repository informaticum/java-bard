package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.nonEmptyIdentifier;
import java.util.Locale;
import java.util.function.Supplier;
import javax.lang.model.element.ElementKind;
import de.informaticum.javabard.impl.SingleCode;

public class TypeDeclaration
implements Supplier<Code> {

    public static enum Kind {

        ANNOTATION(ElementKind.ANNOTATION_TYPE, "@interface"),

        INTERFACE(ElementKind.INTERFACE),

        CLASS(ElementKind.CLASS),

        ENUM(ElementKind.ENUM),

        ;

        private String name;

        private Kind(final ElementKind kind) {
            this(kind, kind.name().toLowerCase(Locale.US));
        }

        private Kind(final ElementKind kind, final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    private String name = null;

    public TypeDeclaration setName(final String name)
    throws IllegalArgumentException {
        this.name = nonEmptyIdentifier(name);
        return this;
    }

    public TypeDeclaration(final String name)
    throws IllegalArgumentException {
        this.setName(name);
    }

    @Override
    public Code get() {
        return SingleCode.code("class %s {", this.name) //
                         .add("}");
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
