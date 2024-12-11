package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;

public class TickerSymbol implements Macro {
    public TickerSymbol(CharSequence input) {
        if (!"TICKER".equals(input)) //$NON-NLS-1$
            throw new IllegalArgumentException(input.toString());
    }

    @Override
    public CharSequence resolve(Security security) {
        return security != null ? security.getTickerSymbol() : null;
    }

    @Override
    public VariableURLConstructor getVariableURLConstructor() {
        return null;
    }
}
