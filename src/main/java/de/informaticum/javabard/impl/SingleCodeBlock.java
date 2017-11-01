package de.informaticum.javabard.impl;

import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import de.informaticum.javabard.api.CodeBlock;
import de.informaticum.javabard.api.IndentEmitter;

public class SingleCodeBlock
implements CodeBlock {

    private final Entry<String, Object[]> code;

    private SingleCodeBlock(final Entry<String, Object[]> code) {
        this.code = code;
    }

    private static final IndentEmitter NO_INDENT = () -> 0;

    public SingleCodeBlock(final String format, final Object... args) {
        this(new SimpleImmutableEntry<>("%s" + format, prepend(NO_INDENT, args)));
    }

    private static final Object[] prepend(final Object element, final Object[] from) {
        final Object[] into = new Object[from.length + 1];
        into[0] = element;
        arraycopy(from, 0, into, 1, from.length);
        return into;
    }

    public static final CodeBlock of(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return new MultiCodeBlock(this, new SingleCodeBlock(requireNonNull(format), requireNonNull(args)));
    }

    @Override
    public CodeBlock add(final CodeBlock code) {
        return new MultiCodeBlock(this, code);
    }

    @Override
    public CodeBlock indent() {
        return new SingleCodeBlock(new SimpleImmutableEntry<>(this.code.getKey(), alter(this.code.getValue(), +1)));
    }

    @Override
    public CodeBlock unindent() {
        return new SingleCodeBlock(new SimpleImmutableEntry<>(this.code.getKey(), alter(this.code.getValue(), -1)));
    }

    private static final Object[] alter(final Object[] args, final int diff) {
        final Object[] copy = copyOf(args, args.length);
        final int old = ((IndentEmitter) copy[0]).getAsInt();
        copy[0] = (IndentEmitter) () -> Math.max(0, old + diff);
        return copy;
    }

    @Override
    public String toString() {
        return format(this.code.getKey(), this.code.getValue());
    }

}
