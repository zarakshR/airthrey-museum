import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class Museum implements ActionListener, ListSelectionListener
{

    private DataStore store;
    private UI ui;

    public static void main(String[] args) {

        Museum museum = new Museum();

        museum.store.readData();

        museum.ui.setTitle("University of Airthrey Museum");
        museum.ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        museum.ui.setSize(1400, 600);
        museum.ui.setLocation(200, 100);
        museum.ui.setVisible(true);

    }

    public Museum() {

        store = new DataStore("treasures.txt");
        ui = new UI(this, this);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == ui.save) {
            System.out.println("SAVE");
        }

        if (e.getSource() == ui.print) {
            System.out.println("PRINT");
        }

        if (e.getSource() == ui.update) {
            System.out.println("UPDATE");
        }

        if (e.getSource() == ui.delete) {
            System.out.println("DELETE");
        }

        if (e.getSource() == ui.undo) {
            System.out.println("UNDO");
        }

        if (e.getSource() == ui.category_filter) {
            System.out.println("CATEGORY FILTER");
        }

        if (e.getSource() == ui.clear_filter_button) {
                System.out.println("CLEAR FILTER");
        }

        if (e.getSource() == ui.name_search_button) {
            System.out.println("NAME SEARCH");
        }

        if (e.getSource() == ui.number_search_button) {
            System.out.println("NUMBER SEARCH");
        }

        if (e.getSource() == ui.create_button) {
            System.out.println("CREATE");
        }

    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getSource() == ui.list) {
            System.out.println("LIST");
        }

    }
}
