package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Patika;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddContentGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_content_title;
    private JTextField fld_content_title;
    private JTextField fld_content_exp;
    private JTextField fld_content_ytlink;
    private JTextField fld_content_info;
    private JButton btn_add_content;
    private JTextField fld_quiz_question;
    private JTextField fld_true_answer;
    private JTextField fld_false_answer;
    private JTextField fld_false_answer_1;
    private JTextField fld_false_answer_2;
    private JButton btn_add_quiz;
    private JComboBox cmb_course_info;
    private Course course;
    ArrayList<Quiz> quizList = new ArrayList<>();


    public void setCourseComboBox(String courseName) {
        for (int i = 0; i < cmb_course_info.getItemCount(); i++) {
            Item item = (Item) cmb_course_info.getItemAt(i);
            String value = item.getValue();
            if (value.equals(courseName)) {
                cmb_course_info.setSelectedIndex(i);
                break;
            }
        }
    }



    public AddContentGUI(String i){
        add(wrapper);
        setSize(400,700);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);




        loadCourseCombo();

        btn_add_content.addActionListener(e -> {
            if ( Helper.isFieldEmpty(fld_content_title) || Helper.isFieldEmpty(fld_content_exp) || Helper.isFieldEmpty(fld_content_ytlink)) {
                Helper.showMsg("fill");
            } else {
                String contentTitle = fld_content_title.getText();
                String exp = fld_content_exp.getText();
                String ytLink = fld_content_ytlink.getText();
                Item courseItem = (Item) cmb_course_info.getSelectedItem();

                if (Content.add(contentTitle, exp, ytLink, courseItem.getKey())) {
                    Helper.showMsg("done");
                } else {
                    Helper.showMsg("error");
                }
            }

        });

        btn_add_quiz.addActionListener(e -> {

            String contentTitle = fld_content_title.getText();
            int contentId = getContentIdByTitle(contentTitle);

            if (contentId != -1 ) {
                String quizQuestion = fld_quiz_question.getText();
                String trueAnswer = fld_true_answer.getText();
                String falseAnswer = fld_false_answer.getText();
                String falseAnswer1 = fld_false_answer_1.getText();
                String falseAnswer2 = fld_false_answer_2.getText();

                boolean result = Quiz.add(contentId, quizQuestion, trueAnswer, falseAnswer, falseAnswer1, falseAnswer2);
                if (result) {
                    quizList.add(new Quiz(quizQuestion, trueAnswer, falseAnswer, falseAnswer1, falseAnswer2));

                    fld_quiz_question.setText("");
                    fld_true_answer.setText("");
                    fld_false_answer.setText("");
                    fld_false_answer_1.setText("");
                    fld_false_answer_2.setText("");
                    Helper.showMsg("Quiz Başarıyla Eklendi");
                } else {
                    Helper.showMsg("error");
                }
            } else {
                Helper.showMsg("Quizi eklemek istediğiniz içerik bulunmamaktadır.");
            }
            dispose();
        });





    }
    public int getContentIdByTitle(String title) {
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



    public void loadCourseCombo(){
        cmb_course_info.removeAllItems();
        for (Course obj : Course.getList()){
            cmb_course_info.addItem(new Item(obj.getId(), obj.getName()));
        }
    }



}
