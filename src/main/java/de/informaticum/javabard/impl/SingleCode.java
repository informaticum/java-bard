package de.informaticum.javabard.impl;

import static de.informaticum.javabard.api.FormattableEmitters.indentation;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.lang.String.format;
import java.util.Formattable;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Supplier;
import de.informaticum.javabard.api.Code;

public class SingleCode
implements Code {

    private final int indent;

    private final Supplier<String> code;

    private SingleCode(final int indent, final SingleCode blueprint) {
        assert indent >= 0;
        assert blueprint != null;
        this.indent = indent;
        this.code = blueprint.code;
    }

    public SingleCode(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        this.indent = 0;
        final Object[] defensiveCopy = args.clone();
        this.code = () -> format(format, defensiveCopy);
    }

    public SingleCode(final Locale locale, final String format, final Object... args) {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        this.indent = 0;
        final Object[] defensiveCopy = args.clone();
        this.code = () -> format(locale, format, defensiveCopy);
    }

    @Override
    public Code indent(final int diff) {
        return new SingleCode(max(0, this.indent + diff), this);
    }

    @Override
    public int getIndent() {
        return this.indent;
    }

    @Override
    public String toString()
    throws IllegalFormatException {
        final StringBuilder out = new StringBuilder();
        try (final Scanner scanner = new Scanner(this.code.get())) {
            final Formattable indentation = indentation(this.indent);
            while (scanner.hasNextLine()) {
                out.append(format("%s%s%n", indentation, scanner.nextLine()));
            }
        }
        return out.toString();
    }

}
