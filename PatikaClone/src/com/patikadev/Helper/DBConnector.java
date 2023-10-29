package com.patikadev.Helper;

import java.sql.*;

public class DBConnector {

    private Connection connect = null;

    public Connection connectDB(){
        try {
            this.connect = DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return this.connect;
    }

    public static Connection getInstance(){
        DBConnector db = new DBConnector();
        return db.connectDB();
    }
    public static String getCourseNameById(int courseId) {
        String courseName = null;
        String query = "SELECT name FROM course WHERE id = ?";
        try {
            PreparedStatement pr = getInstance().prepareStatement(query);
            pr.setInt(1, courseId);
            ResultSet data = pr.executeQuery();
            if (data.next()) {
                courseName = data.getString("name");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return courseName;
    }

}
