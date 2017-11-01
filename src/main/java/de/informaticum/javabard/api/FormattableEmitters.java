package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.lang.reflect.Type;
import java.util.Formattable;
import de.informaticum.javabard.impl.ClassNameEmitter;
import de.informaticum.javabard.impl.IndentEmitter;
import de.informaticum.javabard.impl.JavaStringEmitter;
import de.informaticum.javabard.impl.TypeNameEmitter;

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

    public static Formattable indentation() {
        return indentation(0);
    }

    public static Formattable indentation(final int times) {
        return (IndentEmitter) () -> times;
    }

    public static Formattable i() {
        return indentation();
    }

    public static Formattable i(final int times) {
        return indentation(times);
    }

}
