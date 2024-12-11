package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;

public class Currency implements Macro {
    public Currency(CharSequence input) {
        if (!"CURRENCY".equals(input)) //$NON-NLS-1$
            throw new IllegalArgumentException(input.toString());
    }

    @Override
    public CharSequence resolve(Security security) {
        return security != null ? security.getCurrencyCode() : null;
    }

    @Override
    public VariableURLConstructor getVariableURLConstructor() {
        return null;
    }
}
