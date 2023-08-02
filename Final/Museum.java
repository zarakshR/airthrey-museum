import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class Museum implements ActionListener, ListSelectionListener {

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
            Treasure t = ui.list.getSelectedValue();

            if (t == null) {
                ui.notify("Select an entry before attempting to print it!");
                return;
            }

            PrintJob print_job = ui.getToolkit().getPrintJob(ui, "Printing " + t, null);
            if (print_job == null) {
                ui.notify("Printing failed or cancelled");
                return;
            }

            Graphics graphics = print_job.getGraphics();

            int xLocation = 10;
            int yLocation = 20;
            graphics.drawString("Name: " + t.name(), xLocation, yLocation);

            yLocation += 20;
            graphics.drawString("Category: " + t.category(), xLocation, yLocation);

            yLocation += 20;
            graphics.drawString("Image Path: " + t.image_path(), xLocation, yLocation);

            yLocation += 30;
            graphics.drawImage(ui.drawing_panel.getImage(), xLocation, yLocation, 400, 400, null);

            graphics.dispose();
            print_job.end();
        }

        if (e.getSource() == ui.update) {
            Treasure old = ui.list.getSelectedValue();
            if (old == null) {
                ui.notify("Select an entry before attempting to update it");
                return;
            }

            Treasure edited = ui.getEditedTreasure();
            if (edited == null) {
                ui.notify("Cannot update: One or more fields are empty. Fill in all the fields before attempting to update it");
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
                ui.notify("Select an entry before attempting to delete it!");
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
                ui.notify("Cannot undo: No undo history left!");
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

            if (query.compareTo("") == 0) {
                ui.notify("No query given for search!");
                return;
            }

            Treasure search_result = store.searchByName(query);

            if (search_result == null) {
                ui.notify("No matching name found for " + query);
                return;
            }

            ui.focus(search_result);
        }

        if (e.getSource() == ui.number_search_button) {
            String query = ui.getSearchQuery();

            if (query.compareTo("") == 0) {
                ui.notify("No query given for search!");
                return;
            }

            Treasure search_result = store.searchByNumber(query);

            if (search_result == null) {
                ui.notify("No matching number found for " + query);
                return;
            }

            ui.focus(search_result);
        }

        if (e.getSource() == ui.create_button) {
            Treasure new_treasure = ui.getNewTreasure();
            if (new_treasure == null) {
                ui.notify("Cannot create: One or more fields are empty, fill in all fields before attempting to create a treasure");
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
