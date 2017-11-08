package de.informaticum.javabard.impl;

import java.util.Locale;
import de.informaticum.javabard.api.Code;

public abstract class AbstractCode
implements Code {

    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

}
