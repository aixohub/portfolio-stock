package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;

public class ConstString implements Macro {
    private final CharSequence string;

    public ConstString(CharSequence string) {
        this.string = string;
    }

    @Override
    public CharSequence resolve(Security security) {
        return string;
    }

    @Override
    public VariableURLConstructor getVariableURLConstructor() {
        return null;
    }
}
