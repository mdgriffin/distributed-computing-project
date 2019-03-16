package com.mdgriffin.distributedcomputingproject.server;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SessionImpl implements Session {

    private String id;
    private Date expiry;
    private String username;
    private static final int SESSION_EXPIRY_HOURS = 1;

    SessionImpl (String username) {
        this.id = UUID.randomUUID().toString();
        this.expiry = getDatePlusHours(SESSION_EXPIRY_HOURS);
        this.username = username;
    }

    private SessionImpl () {}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getExpiry() {
        return expiry;
    }

    @Override
    public String getUsername() {
        return username;
    }

    private static Date getDatePlusHours (int numHours) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, numHours); // adds one hour
        return cal.getTime();
    }

}
