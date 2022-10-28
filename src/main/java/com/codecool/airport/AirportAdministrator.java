package com.codecool.airport;

public class AirportAdministrator {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public AirportAdministrator(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
}
