package de.informaticum.javabard.impl;

import java.util.List;
import de.informaticum.javabard.api.Code;

public abstract interface CodeSequence
extends Code {

    public abstract List<? extends Code> getCodes();

    @Override
    public default int getIndent() {
        return this.getCodes().stream().mapToInt(Code::getIndent).min().orElse(0);
    }

}
