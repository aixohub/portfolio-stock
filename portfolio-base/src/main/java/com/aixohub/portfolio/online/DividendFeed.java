package com.aixohub.portfolio.online;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityEvent.DividendEvent;

import java.io.IOException;
import java.util.List;

public interface DividendFeed {

    List<DividendEvent> getDividendPayments(Security security) throws IOException;

}
