package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String lang;

    private Patika patika;
    private User educator;

    public Course(int id, int user_id, int patika_id, String name, String lang){
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.lang=lang;
        this.patika=Patika.getFetch(patika_id);
        this.educator=User.getFetch(user_id);
    }

    public Course() {

    }

    public Course(String courseName, String patikaName, String courseLang) {
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id(){
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }
    public static ArrayList<Course> getList(){
        ArrayList<Course> courseList = new ArrayList<>();
        String sql = "SELECT * FROM course";
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()){
                int id = data.getInt("id");
                int user_id =data.getInt("user_id");
                int patika_id =data.getInt("patika_id");
                String name = data.getString("name");
                String lang = data.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }


    public static boolean add(int user_id, int patika_id, String name, String lang){
        String query = "INSERT INTO course (user_id , patika_id , name, lang) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, patika_id);
            pr.setString(3, name);
            pr.setString(4, lang);
           return pr.executeUpdate() != -1;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static ArrayList<Course> getListByUser(int user_id){
        ArrayList<Course> courseList = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE user_id =  " + user_id;
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()){
                int id = data.getInt("id");
                int userID =data.getInt("user_id");
                int patika_id =data.getInt("patika_id");
                String name = data.getString("name");
                String lang = data.getString("lang");
                obj = new Course(id, userID, patika_id, name, lang);
                courseList.add(obj);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }
    public static ArrayList<Course> getCoursetByPatikaID(int patikaID) {
        ArrayList<Course> courseList = new ArrayList<>();


        String sql = "SELECT * FROM course WHERE patika_id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(sql);
            pr.setInt(1, patikaID);
            ResultSet data = pr.executeQuery();

            while (data.next()) {
                Course course = new Course();
                course.setId(data.getInt("id"));
                course.setUser_id(data.getInt("user_id"));
                course.setPatika_id(data.getInt("patika_id"));
                course.setName(data.getString("name"));
                course.setLang(data.getString("lang"));

                courseList.add(course);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return courseList;
    }


    public static boolean delete(int id){
        String dlt = "DELETE FROM course WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(dlt);
            pr.setInt(1, id);


            return pr.executeUpdate() != -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static Course getFetch(int id){
        Course obj = null;
        String query = "SELECT * FROM course WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet data = pr.executeQuery();
            if (data.next()){
                obj = new Course();
                obj.setId(data.getInt("id"));
                obj.setUser_id(data.getInt("user_id"));
                obj.setPatika_id(data.getInt("patika_id"));
                obj.setName(data.getString("name"));
                obj.setLang(data.getString("lang"));


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

}
