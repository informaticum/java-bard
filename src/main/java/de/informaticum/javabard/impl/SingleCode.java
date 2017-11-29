package de.informaticum.javabard.impl;

import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.lang.String.format;
import java.util.Formattable;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;
import de.informaticum.javabard.api.Code;
import de.informaticum.javabard.api.FormattableEmitters;

public class SingleCode
extends AbstractCode {

    private final int indent;

    private final Supplier<? extends String> code;

    protected SingleCode(final int indent, final Supplier<? extends String> code) {
        assert indent >= 0;
        assert code != null;
        this.indent = indent;
        this.code = code;
    }

    @Override
    public Code indent(final int diff) {
        return new SingleCode(max(0, this.indent + diff), this.code);
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
            final Formattable i = FormattableEmitters.indent(this.indent);
            while (scanner.hasNextLine()) {
                out.append(format("%s%s%n", i, scanner.nextLine()));
            }
        }
        return out.toString();
    }

    public static final class Builder {

        private int indent = 0;

        private Locale locale = null;

        private final String format;

        private final Object[] args;

        public Builder(final String format, final Object... args)
        throws IllegalArgumentException {
            this.format = nonNull(format);
            this.args = nonNull(args);
        }

        public Builder setIndent(final int indent) {
            this.indent = max(0, indent);
            return this;
        }

        public final Builder setLocale(final Locale locale)
        throws IllegalArgumentException {
            this.locale = nonNull(locale);
            return this;
        }

        public final Builder setLocale(final Optional<? extends Locale> locale)
        throws IllegalArgumentException {
            this.locale = nonNull(locale).orElseGet(null);
            return this;
        }

        public final SingleCode build() {
            final Object[] defCopy = this.args.clone();
            final Supplier<String> code = this.locale == null ? () -> format(this.format, defCopy) : () -> format(this.locale, this.format, defCopy);
            return new SingleCode(this.indent, code);
        }

    }

}
