package de.informaticum.javabard.impl;

import static java.util.stream.Collectors.joining;
import de.informaticum.javabard.api.Code;

public abstract class AbstractCodeSequence
extends AbstractCode
implements CodeSequence {

    @Override
    public String toString() {
        return this.getCodes().stream().map(Code::toString).collect(joining());
    }

}
