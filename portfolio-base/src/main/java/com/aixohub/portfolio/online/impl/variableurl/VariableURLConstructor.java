package com.aixohub.portfolio.online.impl.variableurl;

import com.aixohub.portfolio.online.impl.variableurl.macros.Macro;
import com.aixohub.portfolio.online.impl.variableurl.urls.VariableURL;

import java.util.List;

public interface VariableURLConstructor {
    VariableURL construct(List<Macro> macros);
}
