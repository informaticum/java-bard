package de.informaticum.javabard.api;

import java.util.Formattable;

public enum FormattableEmitters {
    ;

    public static Formattable javaString(final Object anything) {
        return (JavaStringEmitter) () -> anything;
    }

    public static Formattable s(final Object anything) {
        return javaString(anything);
    }

}
