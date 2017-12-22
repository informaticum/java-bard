package de.informaticum.javabard.api;

import static de.informaticum.javabard.impl.AbstractCode.combine;
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

    private final String pakkage;

    private final EnumSet<Modifier> modifiers = noneOf(Modifier.class);

    private final ElementKind kind;

    private final String name;

    public TypeDeclaration(final Builder blueprint) {
        this.pakkage = blueprint.pakkage;
        this.name = blueprint.name;
        this.kind = blueprint.kind;
        this.modifiers.addAll(blueprint.modifiers);
    }

    public Builder asBuilder() {
        return new Builder(this.name).in(this.pakkage).as(this.kind).with(this.modifiers);
    }

    @Override
    public Code get() {
        Code code = combine();
        if (!isNull(this.pakkage) && !this.pakkage.isEmpty()) {
            code = code.add("package %s;", this.pakkage);
            code = code.add("");
        }
        final String m = this.modifiers.toString().replace("[]", "").replace("[", "").replace(",", "").replace("]", " ");
        final String k = this.kind.name().toLowerCase(Locale.US).replace("annotation_type", "@interface");
        code = code.add("%s%s %s {", m, k, this.name);
        code = code.add("}");
        return code;
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

    public static class Builder
    implements Supplier<TypeDeclaration> {

        private String pakkage;

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

        public Builder in(final Package pakkage) {
            return this.in(nonNull(pakkage.getName()));
        }

        public Builder in(final String pakkage) {
            this.pakkage = nonNull(pakkage);
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
