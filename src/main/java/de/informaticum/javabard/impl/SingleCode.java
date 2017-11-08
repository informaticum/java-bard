package de.informaticum.javabard.impl;

import static de.informaticum.javabard.api.FormattableEmitters.indentation;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Scanner;
import de.informaticum.javabard.api.Code;

public class SingleCode
extends AbstractCode {

    private final Entry<String, Object[]> code;

    private final int indent;

    private SingleCode(final int indent, final String format, final Object[] arguments) {
        assert indent >= 0;
        assert format != null;
        assert arguments != null;
        this.code = new SimpleImmutableEntry<>(format, arguments.clone());
        this.indent = indent;
    }

    public SingleCode(final String format, final Object... args) {
        this(0, requireNonNull(format), requireNonNull(args));
    }

    @Override
    public Code indent(final int diff) {
        return new SingleCode(max(0, this.indent + diff), this.code.getKey(), this.code.getValue());
    }

    @Override
    public int getIndent() {
        return this.indent;
    }

    @Override
    public String toString(final Locale locale) {
        requireNonNull(locale);
        final StringBuilder out = new StringBuilder();
        final String data = format(locale, this.code.getKey(), this.code.getValue());
        try (final Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLine()) {
                out.append(format("%s%s%n", indentation(this.indent), scanner.nextLine()));
            }
        }
        return out.toString();
    }

}
