package com.aixohub.portfolio.online.impl.variableurl.urls;

import com.aixohub.portfolio.model.Security;

public interface VariableURL extends Iterable<String> {
    void setSecurity(Security security);

    long getMaxFailedAttempts();
}
