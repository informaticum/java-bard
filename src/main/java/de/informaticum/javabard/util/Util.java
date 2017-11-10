package de.informaticum.javabard.util;

import static java.util.Arrays.stream;
import java.util.List;
import java.util.function.Supplier;

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

    @SafeVarargs
    public static final <T> T[] allNonNull(final T... args)
    throws IllegalArgumentException {
        if ((args == null) || stream(args).anyMatch(a -> a == null)) {
            throw new IllegalArgumentException();
        } else {
            return args;
        }
    }

    public static final <T, C extends Collection<T>> C allNonNull(final C args)
    throws IllegalArgumentException {
        if ((args == null) || args.stream().anyMatch(a -> a == null)) {
            throw new IllegalArgumentException();
        } else {
            return args;
        }
    }

    public static final <S extends Supplier<?>> S nonNullSupply(final S arg)
    throws IllegalArgumentException {
        if ((arg == null) || (arg.get() == null)) {
            throw new IllegalArgumentException();
        } else {
            return arg;
        }
    }

    @SafeVarargs
    public static final <S extends Supplier<?>> S[] allNonNullSupply(final S... args)
    throws IllegalArgumentException {
        if ((args == null) || stream(args).anyMatch(a -> a == null) || stream(args).anyMatch(a -> a.get() == null)) {
            throw new IllegalArgumentException();
        } else {
            return args;
        }
    }

}
