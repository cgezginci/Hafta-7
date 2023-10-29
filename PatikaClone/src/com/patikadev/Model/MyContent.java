package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MyContent {

    int id;
    int newcontent_id;
    String exp;
    String link;
    String course_id;
    private Content content;


    public MyContent(int id, int newcontent_id, String exp, String link, String course_id) {
        this.id = id;
        this.newcontent_id = newcontent_id;
        this.exp = exp;
        this.link = link;
        this.course_id = course_id;
        this.content = Content.getFetchh(newcontent_id);

    }

    public MyContent() {

    }


    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewContent_id() {
        return newcontent_id;
    }

    public void setNewContent_id(int newcontent_id) {
        this.newcontent_id = newcontent_id;
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

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public static ArrayList<MyContent> getList() {
        ArrayList<MyContent> myContentList = new ArrayList<>();
        String sql = "SELECT * FROM mycontent";
        MyContent obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()) {
                obj = new MyContent();
                int id = data.getInt("id");
                int newcontent_id = data.getInt("newcontent_id");
                String exp = data.getString("exp");
                String link = data.getString("link");
                String course_id = data.getString("course_id");
                obj = new MyContent(id, newcontent_id, exp, link, course_id);
                myContentList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return myContentList;
    }

    public static boolean add(MyContent myContent, int newcontent_id) {
        String contentTitle = Content.getTitleByNewContentId(newcontent_id);

        if (contentTitle != null){
            myContent.setNewContent_id(newcontent_id);
            String query = "INSERT INTO mycontent (newcontent_id, exp, link, course_id) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
                pr.setInt(1, myContent.getNewContent_id());
                pr.setString(2, myContent.getExp());
                pr.setString(3, myContent.getLink());
                pr.setString(4, myContent.getCourse_id());

                int response = pr.executeUpdate();

                if (response == -1) {
                    Helper.showMsg("Hata oluştu.");
                    return false;
                }

                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            Helper.showMsg("Content yok");
            return false;
        }

    }

    public static boolean deleteContent(int content_id) {
        String query = "DELETE FROM mycontent WHERE newcontent_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, content_id);

            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("Hata oluştu.");
                return false;
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getContentIdByTitle(String title) {
        String query = "SELECT * FROM content WHERE title = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, title);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public static void deleteMyContentByContentId(int newcontent_id) {
        String query = "DELETE FROM mycontent WHERE newcontent_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, newcontent_id);

            int response = pr.executeUpdate();

            if (response == -1) {
                Helper.showMsg("Hata oluştu.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
