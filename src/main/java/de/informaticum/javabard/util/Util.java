package de.informaticum.javabard.util;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;
import java.util.function.IntFunction;
import java.util.stream.Stream;

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

    public static final <T> T[] allNonNull(final T... args)
    throws IllegalArgumentException {
        if ((args == null) || stream(args).anyMatch(a -> a == null)) {
            throw new IllegalArgumentException();
        } else {
            return args;
        }
    }

    public static final <T, I extends Iterable<T>> I allNonNull(final I args)
    throws IllegalArgumentException {
        if ((args == null) || stream(args.spliterator(), false).anyMatch(a -> a == null)) {
            throw new IllegalArgumentException();
        } else {
            return args;
        }
    }

    @Deprecated
    public static final <T> T[] collect(final T first, final T[] others, final IntFunction<T[]> generator) {
        return concat(Stream.of(first), stream(others)).toArray(generator);
    }

}
