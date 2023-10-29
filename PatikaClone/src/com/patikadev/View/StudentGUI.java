package com.patikadev.View;

import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_student;
    private JTable tbl_patika_list;
    private JLabel lbl_welcome;
    private JPanel pnl_student;
    private JPanel pnl_tabika_list;
    private JScrollPane scrl_studet;
    private JScrollPane scrl_patika;
    private JTable tbl_MyContent_list;
    private JButton btn_logout;
    private JScrollPane scrl_content_list;
    private JTable tbl_content_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;

    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private DefaultTableModel mdl_MyContent_list;
    private Object[] row_MyContent_list;
    private JPopupMenu contentMenu;
    Student student;
    private JPopupMenu quizMenu;


    public StudentGUI(Student student) {
        this.student = student;
        add(wrapper);
        setSize(700, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin : " + student.getName());


        mdl_patika_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }

        };
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);


        contentMenu = new JPopupMenu();
        JMenuItem addMyContent = new JMenuItem("Kendi Derslerine Ekle");
        contentMenu.add(addMyContent);

        addMyContent.addActionListener(e -> {
            if (Helper.confirm("Emin misiniz?")) {
                int selectedRow = tbl_content_list.getSelectedRow();
                String newcontent_id_str = tbl_content_list.getValueAt(selectedRow, 0).toString();
                int newcontent_id = Integer.parseInt(newcontent_id_str);

                MyContent myContent = new MyContent();
                myContent.setExp(tbl_content_list.getValueAt(selectedRow, 2).toString());
                myContent.setLink(tbl_content_list.getValueAt(selectedRow, 3).toString());
                myContent.setCourse_id(tbl_content_list.getValueAt(selectedRow, 4).toString());

                if (MyContent.add(myContent, newcontent_id)) {
                    Helper.showMsg("İçerik başarıyla eklendi.");
                    loadMyContentModel();
                } else {
                    Helper.showMsg("İçerik eklenirken bir hata oluştu.");
                }
            }
        });


        mdl_content_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };


        Object[] col_content_list = {"ID", "Title", "Exp", "Link", "Info"};
        mdl_content_list.setColumnIdentifiers(col_content_list);
        row_content_list = new Object[col_content_list.length];

        loadContentModel();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.setComponentPopupMenu(contentMenu);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);

                if (selected_row >= 0 && selected_row < tbl_patika_list.getRowCount()) {
                    tbl_content_list.setRowSelectionInterval(selected_row, selected_row);
                }
            }
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        mdl_MyContent_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        quizMenu = new JPopupMenu();
        JMenuItem addContent = new JMenuItem("Quizi çöz.");
        quizMenu.add(addContent);
        addContent.addActionListener(e -> {
            String selectedTitle = getSelectedMyContentTitle();


            String  ContentID = (tbl_MyContent_list.getValueAt(tbl_MyContent_list.getSelectedRow(), 1).toString());
            System.out.println(ContentID);

            QuizGUI quizGUI = new QuizGUI(ContentID, student);
            quizGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadMyContentModel(selectedTitle);
                }
            });


        });

        Object[] col_MyContent_list = {"id", "title", "exp", "link", "info"};
        mdl_MyContent_list.setColumnIdentifiers(col_MyContent_list);
        row_MyContent_list = new Object[col_MyContent_list.length];

        loadMyContentModel();
        tbl_MyContent_list.setModel(mdl_MyContent_list);
        tbl_MyContent_list.setComponentPopupMenu(quizMenu);
        tbl_MyContent_list.getTableHeader().setReorderingAllowed(false);
    }

    public String getSelectedMyContentTitle() {
        int selectedRow = tbl_MyContent_list.getSelectedRow();
        if (selectedRow != -1) {
            return tbl_MyContent_list.getValueAt(selectedRow, 1).toString();
        }
        return null;
    }

    public void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getList()) {
            i = 0;

            row_content_list[i++] = obj.getId();
            row_content_list[i++] = obj.getTitle();
            row_content_list[i++] = obj.getExp();
            row_content_list[i++] = obj.getLink();
            row_content_list[i++] = obj.getCourse().getName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    public void loadMyContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_MyContent_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (MyContent obj : MyContent.getList()) {
            i = 0;


            row_MyContent_list[i++] = obj.getId();
            row_MyContent_list[i++] = Content.getTitleByNewContentId(obj.getNewContent_id());
            System.out.println( obj.getNewContent_id());
            row_MyContent_list[i++] = obj.getExp();
            row_MyContent_list[i++] = obj.getLink();
            row_MyContent_list[i++] = obj.getCourse_id();
            mdl_MyContent_list.addRow(row_MyContent_list);

        }
    }

    public void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        for (Patika obj : Patika.getList()) {

            int i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }


    }

    public void loadMyContentModel(String s) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_MyContent_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getList(s)) {
            i = 0;
            row_MyContent_list[i++] = obj.getId();
            row_MyContent_list[i++] = obj.getTitle();
            row_MyContent_list[i++] = obj.getExp();
            row_MyContent_list[i++] = obj.getLink();
            row_MyContent_list[i++] = obj.getCourse_id();
            mdl_MyContent_list.addRow(row_MyContent_list);

        }

    }


    public static void main(String[] args) {
        Helper.setLayout();
        StudentGUI stu = new StudentGUI(new Student());
    }


}