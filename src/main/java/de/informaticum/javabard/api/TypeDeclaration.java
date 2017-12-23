package de.informaticum.javabard.api;

import static de.informaticum.javabard.impl.AbstractCode.emptyCode;
import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.EnumSet.noneOf;
import static java.util.Objects.isNull;
import static javax.lang.model.element.ElementKind.ANNOTATION_TYPE;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.ABSTRACT;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public class TypeDeclaration
implements Supplier<Code> {

    private final String pakkage;

    private final Set<Modifier> modifiers = noneOf(Modifier.class);

    private static final Function<Collection<Modifier>, String> MODIFIER_NAME = c -> c.toString() //
                                                                                      .replace("[]", "") //
                                                                                      .replace("[", "") //
                                                                                      .replace(",", "") //
                                                                                      .replace("]", " ");

    private final ElementKind kind;

    private final String name;

    public TypeDeclaration(final Builder blueprint) {
        this.pakkage = blueprint.pakkage;
        this.modifiers.addAll(blueprint.modifiers);
        this.kind = blueprint.kind;
        this.name = blueprint.name;
    }

    public Builder asBuilder() {
        return new Builder(this.name).in(this.pakkage).as(this.kind).with(this.modifiers);
    }

    @Override
    public Code get() {
        Code code = emptyCode();
        if (!isNull(this.pakkage) && !this.pakkage.isEmpty()) {
            code = code.add("package %s;", this.pakkage);
            code = code.add("");
        }
        final String m = MODIFIER_NAME.apply(this.modifiers);
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

        private static final Map<ElementKind, Set<Modifier>> IMPLICIT_MODIFIERS = new EnumMap<>(ElementKind.class);
        static {
            IMPLICIT_MODIFIERS.put(ANNOTATION_TYPE, singleton(ABSTRACT));
            IMPLICIT_MODIFIERS.put(INTERFACE, singleton(ABSTRACT));
        }

        public Builder as(final ElementKind kind) {
            this.kind = nonNull(kind);
            return this.with(IMPLICIT_MODIFIERS.getOrDefault(kind, emptySet()));
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
