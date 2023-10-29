package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    int id;
    String title;
    String exp;
    String link;
    int course_id;
    private Course course;

    public Content(int id, String title, String exp, String link, int course_id) {
        this.id = id;
        this.title = title;
        this.exp = exp;
        this.link = link;
        this.course_id = course_id;
        this.course = Course.getFetch(course_id);
    }

    public Content() {

    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }


    public static ArrayList<Content> getList() {
        ArrayList<Content> contentList = new ArrayList<>();
        String sql = "SELECT * FROM content";
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()) {
                obj = new Content();
                int id = data.getInt("id");
                String title = data.getString("title");
                String exp = data.getString("exp");
                String link = data.getString("link");
                int course_id = data.getInt("course_id");
                obj = new Content(id, title, exp, link, course_id);
                contentList.add(obj);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;


    }

    public static ArrayList<Content> getList(String d) {
        ArrayList<Content> contentList = new ArrayList<>();
        String sql = "SELECT * FROM content WHERE course_id = ?";
        Content obj = null;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(sql);
            pr.setString(1,d);
            ResultSet data = pr.executeQuery();
            while (data.next()) {
                obj = new Content();
                int id = data.getInt("id");
                String title = data.getString("title");
                String exp = data.getString("exp");
                String link = data.getString("link");
                int course_id = data.getInt("course_id");
                obj = new Content(id, title, exp, link, course_id);
                contentList.add(obj);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
    public static ArrayList<Content> getFetch(int course_id){
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT * FROM content WHERE course_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,course_id);
            ResultSet data = pr.executeQuery();
            while (data.next()){
                Content obj = new Content();
                obj.setId(data.getInt("id"));
                obj.setTitle(data.getString("title"));
                obj.setExp(data.getString("exp"));
                obj.setLink(data.getString("link"));
                obj.setCourse_id(data.getInt("course_id"));
                contentList.add(obj);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
    public static ArrayList<Content> getList(int course_ID) {
        ArrayList<Content> contentList = new ArrayList<>();
        String sql = "SELECT * FROM content WHERE course_id = ?" ;
        Content obj;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(sql);
            pr.setInt(1,course_ID);
            ResultSet data = pr.executeQuery(sql);
            while (data.next()) {
                obj = new Content();
                int id = data.getInt("id");
                String title = data.getString("title");
                String exp = data.getString("exp");
                String link = data.getString("link");
                int course_id = data.getInt("course_id");
                obj = new Content(id, title, exp, link, course_id);
                contentList.add(obj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
    public static ArrayList<Content> getContent(String patName){
        ArrayList<Content> contenttList = new ArrayList<>();
        String query = "SELECT * FROM content WHERE title = ?";
        Content obj;

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1 , patName);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String exp = rs.getString("exp");
                String link = rs.getString("link");
                int course_id = rs.getInt("course_id");;
                obj = new Content(id ,title, exp,link,course_id);
                contenttList.add(obj);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getList();
    }

    public static boolean add(String title, String exp, String link, int course_id) {
        String query = "INSERT INTO content (title,exp,link,course_id) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, title);
            pr.setString(2, exp);
            pr.setString(3, link);
            pr.setInt(4, course_id);
            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("error");
            }
            return response != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean delete(int id) {
        String dlt = "DELETE FROM content WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(dlt);
            pr.setInt(1, id);
            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("Hata oluştu.");
                return false;
            }

            // Eğer içerik başarıyla silindi ise, bu içeriğe bağlı olan MyContent nesnelerini de sil.
            MyContent.deleteMyContentByContentId(id);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static ArrayList<Content> searchContentList(String query){
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(query);
            while (data.next()) {
                obj = new Content();
                int id = data.getInt("id");
                String title = data.getString("title");
                String exp = data.getString("exp");
                String link = data.getString("link");
                int course_id = data.getInt("course_id");
                obj = new Content(id, title, exp, link, course_id);
                contentList.add(obj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
    public static Content getContentByID(String id){
        Content obj = null;
        try {
            PreparedStatement pst = DBConnector.getInstance().prepareStatement("SELECT * FROM content WHERE id = ?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()){
                obj = new Content();
                int ID = rs.getInt("id");
                String title = rs.getString("title");
                String exp = rs.getString("exp");
                String link = rs.getString("link");
                int course_id = rs.getInt("course_id");
                obj = new Content(ID, title, exp, link, course_id);
                ArrayList<Quiz> quizList = new ArrayList<>();
                quizList = Quiz.getList(Integer.parseInt(title));

                obj = new Content();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }


    public static String searchQuery(String title){
        String query = "SELECT * FROM content WHERE title LIKE '%{{title}}%'";
        query = query.replace("{{title}}" , title);
        return query;

    }


    public String getCourseName() {
        return DBConnector.getCourseNameById(this.course_id);
    }

    public static Content getContentByID(int id) {
        Content content = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet data = pr.executeQuery();
            if (data.next()) {
                content = new Content();
                content.setId(data.getInt("id"));
                content.setTitle(data.getString("title"));
                content.setExp(data.getString("exp"));
                content.setLink(data.getString("link"));
                content.setCourse_id(data.getInt("course_id"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }
    public static String getTitleByNewContentId(int newcontent_id) {
        String query = "SELECT title FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, newcontent_id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Content getFetchh(int id){
        Content obj = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1 , id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Content(rs.getInt("id"), rs.getString("title"),rs.getString("exp"),rs.getString("link"),rs.getInt("course_id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
    public Quiz getQuizByContentID(int contentID) {
        System.out.println("psaldğp");
        return Quiz.getByContentID(contentID);
    }


}