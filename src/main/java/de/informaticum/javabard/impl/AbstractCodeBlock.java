package de.informaticum.javabard.impl;

import static java.util.Objects.requireNonNull;
import de.informaticum.javabard.api.CodeBlock;

public abstract class AbstractCodeBlock
implements CodeBlock {

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return this.add(new SingleCodeBlock(requireNonNull(format), requireNonNull(args)).indent(this.getIndent()));
    }

    @Override
    public CodeBlock add(final CodeBlock code) {
        return new MultiCodeBlock(this, requireNonNull(code));
    }

}
