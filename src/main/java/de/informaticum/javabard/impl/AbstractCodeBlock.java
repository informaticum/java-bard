package de.informaticum.javabard.impl;

import static java.util.Objects.requireNonNull;
import de.informaticum.javabard.api.CodeBlock;

public abstract class AbstractCodeBlock
implements CodeBlock {

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return new MultiCodeBlock(this, new SingleCodeBlock(requireNonNull(format), requireNonNull(args)));
    }

    @Override
    public CodeBlock add(final CodeBlock code) {
        return new MultiCodeBlock(this, requireNonNull(code));
    }

}
