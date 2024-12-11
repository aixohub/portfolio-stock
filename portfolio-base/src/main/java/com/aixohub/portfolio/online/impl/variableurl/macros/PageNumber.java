package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;
import com.aixohub.portfolio.online.impl.variableurl.urls.PageURL;

public class PageNumber implements Macro {
    public PageNumber(CharSequence input) {
        if (!"PAGE".equals(input)) //$NON-NLS-1$
            throw new IllegalArgumentException(input.toString());
    }

    @Override
    public VariableURLConstructor getVariableURLConstructor() {
        return PageURL::new;
    }

    @Override
    public CharSequence resolve(Security security) {
        throw new UnsupportedOperationException();
    }

    public String resolve(long page) {
        return String.valueOf(page);
    }
}
