package de.informaticum.javabard.impl;

import static java.lang.Math.max;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import de.informaticum.javabard.api.CodeBlock;

public class MultiCodeBlock
extends AbstractCodeBlock {

    private final List<CodeBlock> codes = new ArrayList<>();

    MultiCodeBlock(final CodeBlock... codes) {
        assert codes != null;
        assert codes.length != 0;
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
        // negative indent (a.k.a. unindent) must be capped
        final int d = max(diff, -this.getIndent());
        return new MultiCodeBlock(this.codes.stream().map(c -> c.indent(d)).toArray(CodeBlock[]::new));
    }

    @Override
    public int getIndent() {
        return this.codes.stream().mapToInt(CodeBlock::getIndent).min().getAsInt();
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();
        for (final CodeBlock code : this.codes) {
            try (Scanner scanner = new Scanner(code.toString())) {
                while (scanner.hasNextLine()) {
                    out.append(scanner.nextLine()).append(format("%n"));
                }
            }
        }
        return out.toString();
    }

}
