package de.informaticum.javabard.api;

import static java.lang.String.valueOf;
import java.util.Formattable;
import java.util.Formatter;

public class LiteralFormattable
implements Formattable {

    private final Object anything;

    public LiteralFormattable(final Object anything) {
        this.anything = anything;
    }

    @Override
    public void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
        formatter.format("%s", valueOf(this.anything));
    }

}
