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

        this.store = new DataStore("treasures.txt");
        this.ui = new UI(this, this);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == ui.display.control_buttons.save) {
                System.out.println("SAVE");
        }

        if (e.getSource() == ui.display.control_buttons.print) {
                System.out.println("PRINT");
        }

        if (e.getSource() == ui.display.control_buttons.update) {
                System.out.println("UPDATE");
        }

        if (e.getSource() == ui.display.control_buttons.delete) {
                System.out.println("DELETE");
        }

        if (e.getSource() == ui.display.control_buttons.undo) {
                System.out.println("UNDO");
        }

        if (e.getSource() == ui.tabs.catalogue_tab.room_filter) {
                System.out.println("ROOM FILTER");
        }

        if (e.getSource() == ui.tabs.catalogue_tab.category_filter) {
                System.out.println("CATEGORY FILTER");
        }

        if (e.getSource() == ui.tabs.search_tab.name_search) {
                System.out.println("NAME SEARCH");
        }

        if (e.getSource() == ui.tabs.search_tab.number_search) {
                System.out.println("NUMBER SEARCH");
        }

        if (e.getSource() == ui.tabs.create_tab.create) {
                System.out.println("CREATE");
        }

    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getSource() == ui.tabs.catalogue_tab.list) {
                System.out.println("LIST");
        }

    }
}
