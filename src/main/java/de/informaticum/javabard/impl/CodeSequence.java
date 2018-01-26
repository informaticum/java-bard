package de.informaticum.javabard.impl;

import static java.util.stream.Collectors.joining;
import java.util.List;
import java.util.stream.Collector;
import de.informaticum.javabard.api.Code;

public abstract interface CodeSequence
extends Code {

    public abstract List<? extends Code> getCodes();

    @Override
    public default int getIndent() {
        return this.getCodes().stream().mapToInt(Code::getIndent).min().orElse(0);
    }

    @Override
    public abstract String toString();

    /**
     * Because one cannot supply a {@code default} implementation of the {@link Object#toString()} method, this abstract
     * implementation of {@link CodeSequence} does instead.
     */
    public static abstract class AbstractCodeSequence
    implements CodeSequence {

        private final Collector<? super CharSequence, ?, ? extends String> joiner;

        public AbstractCodeSequence() {
            this(joining());
        }

        public AbstractCodeSequence(final CharSequence delimiter) {
            this(joining(delimiter));
        }

        public AbstractCodeSequence(final Collector<? super CharSequence, ?, ? extends String> joiner) {
            this.joiner = joiner;
        }

        @Override
        public String toString() {
            return this.getCodes().stream().map(Code::toString).collect(this.joiner);
        }

    }

}
