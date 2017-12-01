package de.informaticum.javabard.impl;

import java.util.List;
import de.informaticum.javabard.api.Code;

public abstract class CodeSequence
extends AbstractCode {

    public abstract List<? extends Code> getCodes();

}
