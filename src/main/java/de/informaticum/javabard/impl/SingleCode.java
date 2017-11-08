package de.informaticum.javabard.impl;

import static de.informaticum.javabard.api.FormattableEmitters.indentation;
import static de.informaticum.javabard.api.Util.nonNull;
import static java.lang.Math.max;
import static java.lang.String.format;
import java.util.Formattable;
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

    private SingleCode(final int indent, final String format, final Object[] arguments) {
        assert indent >= 0;
        assert format != null;
        assert arguments != null;
        this.indent = indent;
        final Object[] args = arguments.clone();
        this.code = () -> format(format, args);
    }

    private SingleCode(final int indent, final Locale locale, final String format, final Object[] arguments) {
        assert indent >= 0;
        assert locale != null;
        assert format != null;
        assert arguments != null;
        this.indent = indent;
        final Object[] args = arguments.clone();
        this.code = () -> format(locale, format, args);
    }

    public SingleCode(final String format, final Object... args)
    throws IllegalArgumentException {
        this(0, nonNull(format), nonNull(args));
    }

    public SingleCode(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        this(0, nonNull(locale), nonNull(format), nonNull(args));
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
    public String toString() {
        final StringBuilder out = new StringBuilder();
        final String data = this.code.get();
        assert data != null; // To prevent Formattable's side-effects, we cannot assert earlier!
        try (final Scanner scanner = new Scanner(data)) {
            final Formattable indentation = indentation(this.indent);
            while (scanner.hasNextLine()) {
                out.append(format("%s%s%n", indentation, scanner.nextLine()));
            }
        }
        return out.toString();
    }

}
