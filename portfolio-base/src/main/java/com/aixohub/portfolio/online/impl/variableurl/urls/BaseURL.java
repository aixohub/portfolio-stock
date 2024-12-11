package com.aixohub.portfolio.online.impl.variableurl.urls;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.macros.Macro;

import java.util.List;

public abstract class BaseURL implements VariableURL {
    protected List<Macro> macros;
    protected Security security;

    public BaseURL(List<Macro> macros) {
        this.macros = macros;
    }

    public List<Macro> getMacros() {
        return macros;
    }

    public Security getSecurity() {
        return security;
    }

    @Override
    public void setSecurity(Security security) {
        this.security = security;
    }
}
