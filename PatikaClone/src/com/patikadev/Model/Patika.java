package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patika {
    private int id;
    private String name;

    public Patika(int id , String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public static ArrayList<Patika> getList(){
        ArrayList<Patika> patikaList = new ArrayList<>();
        String sql = "SELECT * FROM patika";
        Patika obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()){
                obj = new Patika(data.getInt("id") , data.getString("name"));
                patikaList.add(obj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patikaList;
    }

    public static boolean add(String name){
        String query = "INSERT INTO patika (name) VALUES (?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);

            int response = pr.executeUpdate();

            if (response == -1){
                Helper.showMsg("error");
            }
            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean update(int id, String name){
        String query = "UPDATE patika SET name = ?  WHERE id = ? ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1 , name );
            pr.setInt(2 , id );
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Patika getFetch(int id){
        Patika obj = null;
        String query = "SELECT * FROM patika WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1 , id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Patika(rs.getInt("id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }


    public static boolean delete(int id){
        String dlt = "DELETE FROM patika WHERE id = ?";
        ArrayList<Course> courseList = Course.getList();
        for (Course obj : courseList){
            if (obj.getPatika().getId() == id){
                Course.delete(obj.getId());
            }
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(dlt);
            pr.setInt(1 , id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
