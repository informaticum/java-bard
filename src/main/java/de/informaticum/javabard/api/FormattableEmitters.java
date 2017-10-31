package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.lang.reflect.Type;
import java.util.Formattable;

public enum FormattableEmitters {
    ;

    public static Formattable javaString(final Object anything) {
        return (JavaStringEmitter) () -> anything;
    }

    public static Formattable s(final Object anything) {
        return javaString(anything);
    }

    public static Formattable typeName(final Type type) {
        requireNonNull(type);
        return (type instanceof Class<?>) ? typeName((Class<?>) type) : (TypeNameEmitter) () -> type;
    }

    public static Formattable t(final Type type) {
        requireNonNull(type);
        return typeName(type);
    }

    public static Formattable typeName(final Class<?> type) {
        requireNonNull(type);
        return (ClassNameEmitter) () -> type;
    }

    public static Formattable t(final Class<?> type) {
        requireNonNull(type);
        return typeName(type);
    }

}
