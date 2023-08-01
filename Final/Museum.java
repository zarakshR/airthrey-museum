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
        museum.ui.loadFilters(museum.store.treasures);
        museum.ui.loadEntries(museum.store.treasures);

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
            String selected_filter = (String) ui.category_filter.getSelectedItem();

            // If nothing is selected, then load all entries, otherwise load entries filtered by the selected filter
            if (selected_filter == null) {
                ui.loadEntries(store.treasures);
            } else {
                ui.loadEntries(store.treasures, selected_filter);
            }
        }

        if (e.getSource() == ui.clear_filter_button) {
            ui.clearFilters();
        }

        if (e.getSource() == ui.name_search_button) {
            String query = ui.getSearchQuery();

            if (query == null) {
                System.err.println("query is null");
                return;
            }

            Treasure search_result = store.searchByName(query);

            if (search_result == null) {
                System.err.println("No match found");
                return;
            }

            ui.focus(search_result);
        }

        if (e.getSource() == ui.number_search_button) {
            String query = ui.getSearchQuery();

            if (query == null) {
                System.err.println("query is null");
                return;
            }

            Treasure search_result = store.searchByNumber(query);

            if (search_result == null) {
                System.err.println("No match found");
                return;
            }

            ui.focus(search_result);
        }

        if (e.getSource() == ui.create_button) {
            System.out.println("CREATE");
        }

    }

    public void valueChanged(ListSelectionEvent e) {

        // Do nothing if this isn't the user's final selection
        if (e.getValueIsAdjusting()) {
            return;
        }

        if (e.getSource() == ui.list) {
            Treasure selected_value = ui.list.getSelectedValue();

            // If nothing is selected, then focus on nothing, else focus on the selected entry
            if (selected_value == null) {
                ui.focus();
            } else {
                ui.focus(ui.list.getSelectedValue());
            }
        }

    }
}
