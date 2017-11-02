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
        assert code.getKey() != null;
        assert code.getValue() != null;
        assert code.getValue().length >= 1;
        assert code.getValue()[0] instanceof IndentEmitter;
        this.code = code;
    }

    public SingleCodeBlock(final String format, final Object... args) {
        this(new SimpleImmutableEntry<>(withIndent(requireNonNull(format)), withIndent(requireNonNull(args))));
    }

    private static final String withIndent(final String format) {
        assert format != null;
        return "%s" + format;
    }

    private static final IndentEmitter ZERO_INDENT = () -> 0;

    private static final Object[] withIndent(final Object[] src) {
        assert src != null;
        final Object[] dest = new Object[src.length + 1];
        dest[0] = ZERO_INDENT;
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
        final int indent = max(0, ((IndentEmitter) copy[0]).getAsInt() + diff);
        copy[0] = (IndentEmitter) () -> indent;
        return copy;
    }

    @Override
    public int getIndent() {
        return ((IndentEmitter) this.code.getValue()[0]).getAsInt();
    }

    @Override
    public String toString() {
        return format(this.code.getKey(), this.code.getValue());
    }

}
