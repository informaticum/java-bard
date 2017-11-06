package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.informaticum.javabard.api.AbstractCodeBlock;
import de.informaticum.javabard.api.CodeBlock;

public class MultiCodeBlock
extends AbstractCodeBlock {

    private final List<CodeBlock> codes = new ArrayList<>();

    public MultiCodeBlock(final CodeBlock... codes) {
        requireNonNull(codes);
        for (final CodeBlock code : codes) {
            assert code != null;
            if (code instanceof MultiCodeBlock) {
                this.codes.addAll(((MultiCodeBlock) code).codes);
            } else {
                this.codes.add(code);
            }
        }
    }

    @Override
    public CodeBlock indent(final int diff) {
        final int d = max(diff, -this.getIndent()); // negative indent (a.k.a. unindent) must be capped
        return new MultiCodeBlock(this.codes.stream().map(c -> c.indent(d)).toArray(CodeBlock[]::new));
    }

    @Override
    public int getIndent() {
        return this.codes.stream().mapToInt(CodeBlock::getIndent).min().orElse(0);
    }

    @Override
    public String toString(final Locale locale) {
        requireNonNull(locale);
        return this.codes.stream().map(c -> c.toString(locale)).collect(joining());
    }

}
