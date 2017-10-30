package de.informaticum.javabard.api;

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
        return (type instanceof Class<?>) ? typeName((Class<?>) type) : (TypeNameEmitter) () -> type;
    }

    public static Formattable t(final Type type) {
        return typeName(type);
    }

    public static Formattable typeName(final Class<?> type) {
        return (ClassNameEmitter) () -> type;
    }

    public static Formattable t(final Class<?> type) {
        return typeName(type);
    }

}
