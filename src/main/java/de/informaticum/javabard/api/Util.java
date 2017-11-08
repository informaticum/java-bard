package de.informaticum.javabard.api;

public enum Util {
    ;

    public static final <T> T nonNull(final T arg)
    throws IllegalArgumentException {
        if (arg == null) {
            throw new IllegalArgumentException();
        } else {
            return arg;
        }
    }

}
