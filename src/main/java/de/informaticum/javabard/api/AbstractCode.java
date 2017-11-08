package de.informaticum.javabard.api;

import java.util.Locale;

public abstract class AbstractCode
implements Code {

    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

}
