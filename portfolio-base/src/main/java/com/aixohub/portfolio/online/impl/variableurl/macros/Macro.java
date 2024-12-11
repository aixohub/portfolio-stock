package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;

public interface Macro {
    VariableURLConstructor getVariableURLConstructor();

    CharSequence resolve(Security security) throws UnsupportedOperationException;
}
