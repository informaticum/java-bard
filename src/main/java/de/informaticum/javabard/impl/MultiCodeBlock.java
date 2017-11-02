package de.informaticum.javabard.impl;

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
    public CodeBlock indent() {
        return new MultiCodeBlock(this.codes.stream().map(CodeBlock::indent).toArray(CodeBlock[]::new));
    }

    @Override
    public CodeBlock unindent() {
        return new MultiCodeBlock(this.codes.stream().map(CodeBlock::unindent).toArray(CodeBlock[]::new));
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
