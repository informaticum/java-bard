package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.nonEmptyIdentifier;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.util.stream.Collectors.joining;
import java.util.EnumSet;
import java.util.Locale;
import java.util.function.Supplier;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import de.informaticum.javabard.impl.SingleCode;

public class TypeDeclaration
implements Supplier<Code> {

    private final EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);

    public TypeDeclaration addModifier(final Modifier... modifiers) {
        this.modifiers.addAll(asList(modifiers));
        return this;
    }

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
            assert kind != null;
            assert name != null;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    private Kind kind = null;

    public TypeDeclaration setKind(final Kind kind) {
        this.kind = nonNull(kind);
        return this;
    }

    private String name = null;

    public TypeDeclaration setName(final String name)
    throws IllegalArgumentException {
        this.name = nonEmptyIdentifier(name);
        return this;
    }

    public TypeDeclaration(final Kind kind, final String name)
    throws IllegalArgumentException {
        this.setKind(kind);
        this.setName(name);
    }

    @Override
    public Code get() {
        String mods = this.modifiers.stream().map(Modifier::toString).collect(joining(" "));
        mods += mods.isEmpty() ? "" : " ";
        return SingleCode.code("%s%s %s {", mods, this.kind, this.name) //
                         .add("}");
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

    public static final TypeDeclaration declare(final Kind kind, final String name)
    throws IllegalArgumentException {
        return new TypeDeclaration(kind, name);
    }

}
