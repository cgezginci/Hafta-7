package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JPanel pnl_educator_menu;
    private JScrollPane scrl_educator_menu;
    private JPanel pnl_educator_course;
    private JScrollPane scrl_educator_course;
    private JTable tbl_educator_course;
    private JTable tbl_educator_menu;
    private JLabel lbl_welcome;
    private JButton btn_educator_logout;
    private JTextField fld_Srch_title;
    private JButton btn_Srch_title;
    private DefaultTableModel mdl_educator_list;
    private Object[] row_educator_list;
    private DefaultTableModel mdl_educator_course;
    private Object[] row_educator_course;
    private JPopupMenu educatorMenu;
    private JPopupMenu courseMenu;


    private final Educator educator;
    public EducatorGUI(Educator educator){
        this.educator = educator;

        add(wrapper);
        setSize(1000,500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_educator_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();


        });
        lbl_welcome.setText("Hoşgeldin : " + educator.getName());

        mdl_educator_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return super.isCellEditable(row, column);
            }
        };

        educatorMenu = new JPopupMenu();
        JMenuItem addContent = new JMenuItem("İçerik ekle");
        educatorMenu.add(addContent);
        addContent.addActionListener(e -> {
            String select_name = tbl_educator_menu.getValueAt(tbl_educator_menu.getSelectedRow() , 1).toString();
            AddContentGUI ad = new AddContentGUI(select_name);
            ad.setCourseComboBox(select_name);

            ad.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCourseModel();
                }
            });
        });



        Object[] col_educator_list = {"ID" , "Ders Adı" , "Eğitmen Adı" , "Patika Adı" , "Program Dili"};
        mdl_educator_list.setColumnIdentifiers(col_educator_list);
        row_educator_list = new Object[col_educator_list.length];
        loadEducatorModel();
        tbl_educator_menu.setModel(mdl_educator_list);
        tbl_educator_menu.setComponentPopupMenu(educatorMenu);
        tbl_educator_menu.getTableHeader().setReorderingAllowed(false);

        tbl_educator_menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_educator_menu.rowAtPoint(point);
                tbl_educator_menu.setRowSelectionInterval(selectedRow,selectedRow);
                int course_id = (int) tbl_educator_menu.getValueAt(selectedRow, 0);
                loadCourseModel(course_id);
            }
        });




        //coursemenu-----------------------------------------------------------------------------------

        mdl_educator_course = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        courseMenu = new JPopupMenu();
        JMenuItem deleteContent = new JMenuItem("Sil");
        courseMenu.add(deleteContent);
        deleteContent.addActionListener(e -> {
            if (Helper.confirm("sure")){
                int selectedRow = tbl_educator_course.getSelectedRow();
                int select_id = (int) tbl_educator_course.getValueAt(selectedRow, 0 );
                String str = tbl_educator_course.getValueAt(selectedRow, 4).toString();
                if (Content.delete(select_id)){
                    Helper.showMsg("done");
                    loadCourseModel(str);
                }else {
                    Helper.showMsg("error");
                }
            }
        });



        Object[] col_educator_course = {"ID" , "Başlık" ,"Açıklama" ,"Youtube Linki" , "Ders Adı"};
        mdl_educator_course.setColumnIdentifiers(col_educator_course);
        row_educator_course = new  Object[col_educator_course.length];


        tbl_educator_course.setModel(mdl_educator_course);
        tbl_educator_course.setComponentPopupMenu(courseMenu);
        tbl_educator_course.getTableHeader().setReorderingAllowed(false);

        tbl_educator_course.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_educator_course.rowAtPoint(point);
                tbl_educator_course.setRowSelectionInterval(selectedRow,selectedRow);

            }
        });

        btn_Srch_title.addActionListener(e -> {
            String title = fld_Srch_title.getText();
            String query = Content.searchQuery(title);
            ArrayList<Content> searchingContent = Content.searchContentList(query);
            loadCourseModel(searchingContent);
        });

    }
    public void loadCourseModel(ArrayList<Content> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_course.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : list){
            i = 0;
            row_educator_course[i++] =obj.getId();
            row_educator_course[i++] =obj.getTitle();
            row_educator_course[i++] =obj.getExp();
            row_educator_course[i++] =obj.getLink();
            row_educator_course[i++] =obj.getCourse().getName();
            mdl_educator_course.addRow(row_educator_course);

        }

    }
    public void loadCourseModel(String s){

        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_course.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getList(s)){
            i = 0;
            row_educator_course[i++] =obj.getId();
            row_educator_course[i++] =obj.getTitle();
            row_educator_course[i++] =obj.getExp();
            row_educator_course[i++] =obj.getLink();
            row_educator_course[i++] =obj.getCourse().getName();
            mdl_educator_course.addRow(row_educator_course);

        }

    }
    public void loadCourseModel(int course_id){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_course.getModel();
        clearModel.setRowCount(0);

        ArrayList<Content> contentList = Content.getFetch(course_id);

        if (contentList != null && !contentList.isEmpty()) {
            for (Content content : contentList) {
                row_educator_course[0] = content.getId();
                row_educator_course[1] = content.getTitle();
                row_educator_course[2] = content.getExp();
                row_educator_course[3] = content.getLink();
                row_educator_course[4] = content.getCourseName();
                mdl_educator_course.addRow(row_educator_course);
            }
        }
    }
    public void loadCourseModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_course.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getList()){
            i = 0;
            row_educator_course[i++] =obj.getId();
            row_educator_course[i++] =obj.getTitle();
            row_educator_course[i++] =obj.getExp();
            row_educator_course[i++] =obj.getLink();
            row_educator_course[i++] =obj.getCourse().getName();
            mdl_educator_course.addRow(row_educator_course);

        }

    }





    public void loadEducatorModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_menu.getModel();
        clearModel.setRowCount(0);
        ArrayList<Course> courseList;
        courseList = Course.getListByUser(educator.getId());
        int i = 0;
        for (Course course : courseList){
            row_educator_list = new Object[]{course.getId(),course.getName(), course.getEducator().getName() , course.getPatika().getName(),course.getLang()};
            mdl_educator_list.addRow(row_educator_list);
        }
    }






    public static void main(String[] args) {
        Helper.setLayout();
        EducatorGUI edu = new EducatorGUI(new Educator());

    }

}

