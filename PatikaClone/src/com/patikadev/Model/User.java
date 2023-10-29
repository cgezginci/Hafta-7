package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String uname;
    private String pass;
    private String type;

    public User(){}

    public User(int id, String name, String uname, String pass, String type) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM user";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(sql);
            while (data.next()){
                obj = new User();
                obj.setId(data.getInt("id"));
                obj.setName(data.getString("name"));
                obj.setUname(data.getString("uname"));
                obj.setPass(data.getString("pass"));
                obj.setType(data.getString("type"));
                userList.add(obj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

        public static boolean add(String name, String uname, String pass, String type){
            String query = "INSERT INTO user(name,uname,pass,type) VALUES (?,?,?,?)";
            User findUser = User.getFetch(uname);
            if (findUser != null){
                Helper.showMsg("Bu kullanıcı daha önce eklenmiş.");
                return false;
            }
            try {
                PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
                pr.setString(1, name);
                pr.setString(2, uname);
                pr.setString(3, pass);
                pr.setString(4, type);

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

      public static User getFetch(String uname){
            User obj = null;
            String query = "SELECT * FROM user WHERE uname = ?";
          try {
              PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
              pr.setString(1,uname);
              ResultSet data = pr.executeQuery();
              if (data.next()){
                  obj = new User();
                  obj.setId(data.getInt("id"));
                  obj.setName(data.getString("name"));
                  obj.setUname(data.getString("uname"));
                  obj.setPass(data.getString("pass"));
                  obj.setType(data.getString("type"));

              }
          } catch (Exception e) {
              System.out.println(e.getMessage());
          }
          return obj;
      }

    public static User getFetch(int id){
        User obj = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet data = pr.executeQuery();
            if (data.next()){
                obj = new User();
                obj.setId(data.getInt("id"));
                obj.setName(data.getString("name"));
                obj.setUname(data.getString("uname"));
                obj.setPass(data.getString("pass"));
                obj.setType(data.getString("type"));

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }
    public static User getFetch(String uname, String pass){
        User obj = null;
        String query = "SELECT * FROM user WHERE uname = ? AND pass = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,uname);
            pr.setString(2,pass);
            ResultSet data = pr.executeQuery();
            if (data.next()){
                switch (data.getString("type")){
                    case "operator" :
                        obj = new Operator();
                        break;
                    case "educator" :
                        obj = new Educator();
                        break;
                    case  "student" :
                        obj = new Student();
                        break;
                    default:
                        obj = new User();
                }

                obj.setId(data.getInt("id"));
                obj.setName(data.getString("name"));
                obj.setUname(data.getString("uname"));
                obj.setPass(data.getString("pass"));
                obj.setType(data.getString("type"));

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

    public static boolean delete(int id){
        String dlt = "DELETE FROM user WHERE id = ?";
        ArrayList<Course> courseList = Course.getListByUser(id);
        for (Course c: courseList){
            Course.delete(c.getId());
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(dlt);
            pr.setInt(1, id);


            return pr.executeUpdate() != -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static boolean update(int id, String name, String uname, String pass, String type){
        ArrayList<Object> types = new ArrayList<>();
        types.add("operator");
        types.add("educator");
        types.add("student");


        String updt ="UPDATE user SET name = ?, uname = ?, pass = ?, type = ? WHERE id = ?";
        User findUser = User.getFetch(uname);
        if (findUser != null && findUser.getId() != id){
            Helper.showMsg("Bu kullanıcı daha önce eklenmiş.");
            return false;
        }
        if (!types.contains(type)){
            Helper.showMsg("Lütfen geçerli bir üyelik tipi giriniz.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(updt);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setString(4, type);
            pr.setInt(5, id);
            return pr.executeUpdate() != -1;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }





    public static ArrayList<User> searchUserList(String query){
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet data = st.executeQuery(query);
            while (data.next()){
                obj = new User();
                obj.setId(data.getInt("id"));
                obj.setName(data.getString("name"));
                obj.setUname(data.getString("uname"));
                obj.setPass(data.getString("pass"));
                obj.setType(data.getString("type"));
                userList.add(obj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public static String searchQuery(String name, String uname, String type){
        String query = "SELECT * FROM user WHERE uname LIKE '%{{uname}}%' AND name LIKE '%{{name}}%'";
        query = query.replace("{{uname}}" , uname);
        query = query.replace("{{name}}" , name);
        if (!type.isEmpty()){
            query += "AND type  = '{{type}}'";
            query = query.replace("{{type}}" , type);
        }

        return query;

    }

}
