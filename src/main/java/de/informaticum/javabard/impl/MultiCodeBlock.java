package de.informaticum.javabard.impl;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import de.informaticum.javabard.api.CodeBlock;

public class MultiCodeBlock
implements CodeBlock {

    private final List<CodeBlock> codes = new ArrayList<>();

    MultiCodeBlock(final CodeBlock... codes) {
        this.codes.addAll(asList(codes));
    }

    private MultiCodeBlock(final MultiCodeBlock origin, final CodeBlock... codes) {
        this.codes.addAll(origin.codes);
        this.codes.addAll(asList(codes));
    }

    @Override
    public CodeBlock add(final String format, final Object... args) {
        return new MultiCodeBlock(this, new SingleCodeBlock(requireNonNull(format), requireNonNull(args)));
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
