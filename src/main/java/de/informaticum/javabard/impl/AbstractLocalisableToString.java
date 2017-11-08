package de.informaticum.javabard.impl;

import java.util.Locale;
import de.informaticum.javabard.api.deprecated.LocalisableToString;

public abstract class AbstractLocalisableToString
implements LocalisableToString {

    @Override
    public String toString() {
        return this.toString(Locale.getDefault());
    }

}
