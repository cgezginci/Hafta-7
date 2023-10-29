package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private int content_id;
    private String question;
    private String trueAnswer;
    private String falseAnswer;
    private String falseAnswer1;
    private String falseAnswer2;
    private Content content;

    public Quiz(String question, String trueAnswer, String falseAnswer, String falseAnswer1, String falseAnswer2) {
        this.question = question;

        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
        this.falseAnswer1 = falseAnswer1;
        this.falseAnswer2 = falseAnswer2;
        this.content = Content.getContentByID(content_id);

    }
    public Quiz(int content_id, String question, String trueAnswer, String falseAnswer, String falseAnswer1, String falseAnswer2) {
        this.content_id = content_id;
        this.question = question;
        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
        this.falseAnswer1 = falseAnswer1;
        this.falseAnswer2 = falseAnswer2;
        this.content = Content.getContentByID(content_id);
    }

    public Quiz(int id, int contentID, String question, String trueAnswer, String falseAnswer, String falseAnswer1, String falseAnswer2) {
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }





    public static boolean add(int content_id, String question, String trueAnswer, String falseAnswer, String falseAnswer1, String falseAnswer2){
        String query= "INSERT INTO quiz (content_id,question,true_answer,false_answer,false_answer_1,false_answer_2) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, content_id);
            pr.setString(2, question);
            pr.setString(3, trueAnswer);
            pr.setString(4, falseAnswer);
            pr.setString(5, falseAnswer1);
            pr.setString(6, falseAnswer2);

            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Quiz> getList(int content_id) {
        ArrayList<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM quiz WHERE content_id = ?";
        try {
            PreparedStatement st = DBConnector.getInstance().prepareStatement(query);
            st.setInt(1, content_id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String question = rs.getString("question");
                String trueAnswer = rs.getString("true_answer");
                String falseAnswer = rs.getString("false_answer");
                String falseAnswer1 = rs.getString("false_answer_1");
                String falseAnswer2 = rs.getString("false_answer_2");

                Quiz obj = new Quiz(content_id ,question, trueAnswer, falseAnswer, falseAnswer1, falseAnswer2);
                quizList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quizList;
    }







    public boolean checkAnswer(String userAnswer) {
        return userAnswer.equals(trueAnswer);
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public String getFalseAnswer() {
        return falseAnswer;
    }

    public void setFalseAnswer(String falseAnswer) {
        this.falseAnswer = falseAnswer;
    }

    public String getFalseAnswer1() {
        return falseAnswer1;
    }

    public void setFalseAnswer1(String falseAnswer1) {
        this.falseAnswer1 = falseAnswer1;
    }

    public String getFalseAnswer2() {
        return falseAnswer2;
    }

    public void setFalseAnswer2(String falseAnswer2) {
        this.falseAnswer2 = falseAnswer2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public static Quiz getByContentID(int contentID) {
        String query = "SELECT * FROM quiz WHERE content_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, contentID);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String trueAnswer = rs.getString("true_answer");
                String falseAnswer = rs.getString("false_answer");
                String falseAnswer1 = rs.getString("false_answer_1");
                String falseAnswer2 = rs.getString("false_answer_2");
                return new Quiz(id, contentID, question, trueAnswer, falseAnswer, falseAnswer1, falseAnswer2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
    
