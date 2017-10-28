package de.informaticum.javabard.api;

public enum Formattables {
    ;

    public static LiteralFormattable literal(final Object anything) {
        return new LiteralFormattable(anything);
    }

    public static StringFormattable string(final Object anything) {
        return new StringFormattable(anything);
    }

}
