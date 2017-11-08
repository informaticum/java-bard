package de.informaticum.javabard.api.deprecated;

import java.util.Locale;

@Deprecated(/* Resolve at the end, to keep the API clean! */)
public abstract interface LocalisableToString {

    public abstract String toString(final Locale locale);

}
