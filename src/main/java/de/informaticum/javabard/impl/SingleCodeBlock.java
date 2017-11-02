package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import de.informaticum.javabard.api.CodeBlock;

public class SingleCodeBlock
extends AbstractCodeBlock {

    private final Entry<String, Object[]> code;

    private SingleCodeBlock(final Entry<String, Object[]> code) {
        assert code != null;
        this.code = code;
    }

    private static final IndentEmitter ZERO_INDENT = () -> 0;

    public SingleCodeBlock(final String format, final Object... args) {
        this(new SimpleImmutableEntry<>("%s" + requireNonNull(format), prepend(ZERO_INDENT, requireNonNull(args).clone())));
    }

    private static final Object[] prepend(final Object element, final Object[] src) {
        assert element != null;
        assert src != null;
        final Object[] dest = new Object[src.length + 1];
        dest[0] = element;
        arraycopy(src, 0, dest, 1, src.length);
        return dest;
    }

    @Override
    public CodeBlock indent(final int diff) {
        return new SingleCodeBlock(new SimpleImmutableEntry<>(this.code.getKey(), resetIndent(this.code.getValue(), diff)));
    }

    private static final Object[] resetIndent(final Object[] args, final int diff) {
        assert args != null;
        assert args.length >= 1;
        assert args[0] instanceof IndentEmitter;
        final Object[] copy = copyOf(args, args.length);
        final int old = ((IndentEmitter) copy[0]).getAsInt();
        copy[0] = (IndentEmitter) () -> max(0, old + diff);
        return copy;
    }

    @Override
    public int getIndent() {
        assert this.code.getValue().length >= 1;
        assert this.code.getValue()[0] instanceof IndentEmitter;
        return ((IndentEmitter) this.code.getValue()[0]).getAsInt();
    }

    @Override
    public String toString() {
        return format(this.code.getKey(), this.code.getValue());
    }

}
