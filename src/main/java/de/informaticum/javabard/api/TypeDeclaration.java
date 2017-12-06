package de.informaticum.javabard.api;

import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.util.Arrays.asList;
import static java.util.EnumSet.noneOf;
import static java.util.stream.Collectors.joining;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import de.informaticum.javabard.impl.AbstractCode;

public class TypeDeclaration
implements Supplier<Code> {

    private final EnumSet<Modifier> modifiers = noneOf(Modifier.class);

    private final ElementKind kind;

    private final String name;

    public TypeDeclaration(final Builder blueprint) {
        this.name = blueprint.name;
        this.kind = blueprint.kind;
        this.modifiers.addAll(blueprint.modifiers);
    }

    public Builder asBuilder() {
        return new Builder(this.name).as(this.kind).with(this.modifiers);
    }

    private static final Function<? super Enum<?>, ? extends String> asKeyword = m -> m.name().toLowerCase(Locale.US);

    @Override
    public Code get() {
        String m = this.modifiers.stream().map(asKeyword).collect(joining(" "));
        m += m.isEmpty() ? "" : " ";
        final String k = asKeyword.apply(this.kind).replace("annotation_type", "@interface");
        return AbstractCode.code("%s%s %s {", m, k, this.name).add("}");
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

    public static class Builder
    implements Supplier<TypeDeclaration> {

        private final EnumSet<Modifier> modifiers = noneOf(Modifier.class);

        private ElementKind kind;

        private String name;

        public Builder(final String name) {
            this.name = nonNull(name);
        }

        public Builder name(final String name) {
            this.name = nonNull(name);
            return this;
        }

        public Builder as(final ElementKind kind) {
            this.kind = nonNull(kind);
            return this;
        }

        public Builder with(final Collection<? extends Modifier> modifiers) {
            this.modifiers.addAll(allNonNull(modifiers));
            return this;
        }

        public Builder with(final Modifier... modifiers) {
            return this.with(asList(allNonNull(modifiers)));
        }

        @Override
        public TypeDeclaration get() {
            return new TypeDeclaration(this);
        }

    }

    public static final Builder declare(final String name) {
        return new Builder(nonNull(name));
    }

}
