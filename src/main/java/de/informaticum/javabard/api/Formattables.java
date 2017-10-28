package de.informaticum.javabard.api;

import java.util.Formattable;
import de.informaticum.javabard.impl.LiteralFormattable;
import de.informaticum.javabard.impl.StringFormattable;

public enum Formattables {
    ;

    public static Formattable literal(final Object anything) {
        return new LiteralFormattable(anything);
    }

    public static Formattable string(final Object anything) {
        return new StringFormattable(anything);
    }

}
