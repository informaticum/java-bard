package de.informaticum.javabard.impl;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import de.informaticum.javabard.api.CodeBlock;

public class SingleCodeBlock
implements CodeBlock {

    private final Entry<String, Object[]> code;

    public SingleCodeBlock(final String format, final Object... args) {
        assert format != null;
        assert args != null;
        this.code = new SimpleImmutableEntry<>(format, args.clone());
    }

    public static final CodeBlock of(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return new MultiCodeBlock(this, new SingleCodeBlock(requireNonNull(format), requireNonNull(args)));
    }

    @Override
    public CodeBlock indent() {
        return this;
    }

    @Override
    public CodeBlock unindent() {
        return this;
    }

    @Override
    public String toString() {
        return format(this.code.getKey(), this.code.getValue());
    }

}
