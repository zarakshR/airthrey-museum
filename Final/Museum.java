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
            store.writeData();
        }

        if (e.getSource() == ui.print) {
            System.out.println("PRINT");
        }

        if (e.getSource() == ui.update) {
            Treasure old = ui.list.getSelectedValue();
            if (old == null) {
                System.err.println("Attempting to update non-selected value");
                return;
            }

            Treasure edited = ui.getEditedTreasure();
            if (edited == null) {
                System.err.println("Cannot update: One or more fileds empty");
                return;
            }

            // remove old treasure and add new one, then reload entries, using a filter if one is selected
            store.treasures.remove(old);
            store.treasures.add(edited);

            String selected_filter = (String) ui.category_filter.getSelectedItem();
            if (selected_filter == null) {
                ui.loadEntries(store.treasures);
                ui.focus(edited);
            } else {
                ui.loadEntries(store.treasures, selected_filter);
                ui.focus(edited);
            }

        }

        if (e.getSource() == ui.delete) {
            Treasure selected_value = ui.list.getSelectedValue();
            if (selected_value == null) {
                System.err.println("nothing selected");
                return;
            }

            store.delete(selected_value);

            // Reload the entries; make sure to check if a filter is selected and reload appropriately
            String selected_filter = (String) ui.category_filter.getSelectedItem();
            if (selected_filter == null) {
                ui.loadEntries(store.treasures);
            } else {
                ui.loadEntries(store.treasures, selected_filter);
            }
        }

        if (e.getSource() == ui.undo) {
            Treasure undone = store.undo();
            if (undone == null) {
                System.err.println("Undo stack empty");
                return;
            }

            // Refocus on the just-undeleted treasure
            ui.loadEntries(store.treasures);
            ui.focus(undone);
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
            Treasure new_treasure = ui.getNewTreasure();
            if (new_treasure == null) {
                System.err.println("Cannot create: one or more fields empty");
                return;
            }

            // Add the new treasure to the data and reload the entries, then focus on the newly created entry
            store.treasures.add(new_treasure);

            String selected_filter = (String) ui.category_filter.getSelectedItem();
            if (selected_filter == null) {
                ui.loadEntries(store.treasures);
                ui.focus(new_treasure);
            } else {
                ui.loadEntries(store.treasures, selected_filter);
                ui.focus(new_treasure);
            }
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
