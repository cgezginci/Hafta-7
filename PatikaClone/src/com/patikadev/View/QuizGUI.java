package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Quiz;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class QuizGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_quiz_question;
    private JRadioButton rd_true_answer;
    private JRadioButton rd_false_answer;
    private JRadioButton rd_false_answer1;
    private JRadioButton rd_false_answer2;
    private JButton btn_submit;
    private boolean isAnswered = false;
    private JButton btn_submit_button;
    private JButton completeTheContentButton;
    private String selectedContentID;
    private Content content;
    ArrayList<Quiz> quizList = new ArrayList<>();
    Quiz quiz;
    private String selectedRadioText;
    private User user;
    private boolean isAllQuizAnswered = false;
    int quizCountWeCurrentlyIn;
    int counter;

    public QuizGUI(String selectedContentID , User user) {
        add(wrapper);
        setSize(400, 300);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);



        int contentID = Integer.parseInt(selectedContentID);
        Quiz quiz = content.getQuizByContentID(contentID);



        System.out.println(selectedContentID);
        if (quiz != null) {
            fld_quiz_question.setText(quiz.getQuestion());
            rd_true_answer.setText(quiz.getTrueAnswer());
            rd_false_answer.setText(quiz.getFalseAnswer());
            rd_false_answer1.setText(quiz.getFalseAnswer1());
            rd_false_answer2.setText(quiz.getFalseAnswer2());
        } else {
            Helper.showMsg("Bu içeriğin bir quiz'i bulunmamaktadır.");
        }


        btn_submit.addActionListener(e -> {
            if (!Objects.equals(user.getType(), "student")){
                Helper.showMsg("You can't answer quiz because you are not student!");

            } else {
                if (isAllQuizAnswered) {
                    Helper.showMsg("You answered all the questions!");
                } else if (rd_true_answer.isSelected() || rd_false_answer.isSelected() || rd_false_answer1.isSelected() || rd_false_answer2.isSelected()) {
                    if (Objects.equals(selectedRadioText, quiz.getTrueAnswer())) {
                        Helper.showMsg("You answer correct this quiz!");


                    } else {
                        Helper.showMsg("Wrong choice!");
                    }
                } else {
                    Helper.showMsg("Please choose an answer!");
                }
            }
        });

        ActionListener listener = e -> {
            if (rd_true_answer.isSelected()){
                selectedRadioText = rd_true_answer.getText();
            }else if (rd_false_answer.isSelected()){
                selectedRadioText = rd_false_answer.getText();
            }else if (rd_false_answer1.isSelected()){
                selectedRadioText = rd_false_answer1.getText();
            }else if (rd_false_answer2.isSelected()){
                selectedRadioText = rd_false_answer2.getText();
            }
        };
        rd_true_answer.addActionListener(listener);
        rd_false_answer.addActionListener(listener);
        rd_false_answer1.addActionListener(listener);
        rd_false_answer2.addActionListener(listener);
    }
    public static boolean list(int id){
        String dlt = "SEELCT FROM quiz WHERE id = ?";
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

    public static void main(String[] args) {
        Helper.setLayout();
        Quiz quiz = new Quiz("Soru", "Doğru Cevap", "Yanlış Cevap 1", "Yanlış Cevap 2", "Yanlış Cevap 3");

    }
}
