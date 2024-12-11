package com.aixohub.portfolio.online.portfolioreport;

import com.aixohub.portfolio.model.SecurityEvent;

import java.time.LocalDate;

public class PRSecurityEvent {
    public LocalDate date;
    public String type;
    public String details;

    public PRSecurityEvent(SecurityEvent event) {
        this.date = event.getDate();
        this.type = event.getType().toString();
        this.details = event.getDetails();
    }

}
