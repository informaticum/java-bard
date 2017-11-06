package de.informaticum.javabard.api;

import static java.util.Objects.requireNonNull;
import java.util.Locale;
import de.informaticum.javabard.impl.MultiCodeBlock;
import de.informaticum.javabard.impl.SingleCodeBlock;

public abstract class AbstractCodeBlock
implements CodeBlock {

    public static CodeBlock of(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

    public static CodeBlock code(final String format, final Object... args) {
        return new SingleCodeBlock(requireNonNull(format), requireNonNull(args));
    }

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return this.add(code(requireNonNull(format), requireNonNull(args)).indent(this.getIndent()));
    }

    @Override
    public CodeBlock add(final CodeBlock code) {
        return new MultiCodeBlock(this, requireNonNull(code));
    }

    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

}
