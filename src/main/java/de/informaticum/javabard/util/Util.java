package de.informaticum.javabard.util;

import static java.util.Arrays.stream;
import static java.util.stream.StreamSupport.stream;
import static javax.lang.model.SourceVersion.isIdentifier;
import static javax.lang.model.SourceVersion.isKeyword;

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

    public static final <T extends CharSequence> T nonEmpty(final T arg)
    throws IllegalArgumentException {
        nonNull(arg);
        if (arg.length() == 0) {
            throw new IllegalArgumentException();
        } else {
            return arg;
        }
    }

    public static final <C extends CharSequence> C nonEmptyIdentifier(final C arg)
    throws IllegalArgumentException {
        nonEmpty(arg);
        if (!isIdentifier(arg)) {
            throw new IllegalArgumentException();
        } else if (isKeyword(arg)) {
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

}
