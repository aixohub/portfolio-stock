package com.aixohub.portfolio.events;

import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;

public class SecurityCreatedEvent extends ChangeEvent {
    public SecurityCreatedEvent(Client client, Security security) {
        super(client, security);
    }

    public Security getSecurity() {
        return (Security) getSubject();
    }
}
