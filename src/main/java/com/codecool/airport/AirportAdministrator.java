package com.codecool.airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class AirportAdministrator {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public AirportAdministrator(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public List<String> getUniquePlanes(){
        List<String> result = new ArrayList<>();
        try(Connection conn = getConnection(dbUrl, dbUser, dbPassword)){
            String SQL = "SELECT DISTINCT aircraft_type FROM airplanes_landed ORDER BY aircraft_type DESC";
            PreparedStatement st = conn.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String type = rs.getString("aircraft_type");
                result.add(type);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}
