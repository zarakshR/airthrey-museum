import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class Museum implements ActionListener, ListSelectionListener {

    private DataStore store;
    private UI ui;

    public static void main(String[] args) {

        Museum museum = new Museum();

        // Initialize the GUI the first time around. These will be refreshed as needed by our action handlers.
        museum.store.readData();
        museum.ui.loadFilters(museum.store.treasures);
        museum.ui.loadEntries(museum.store.treasures);

        // Set some application level settings
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

            // Prints -
            //      Name
            //      Category
            //      Image Path
            //      Image

            int xLocation = 10;
            int yLocation = 20;
            graphics.drawString("Name: " + t.name(), xLocation, yLocation);

            yLocation += 20;
            graphics.drawString("Category: " + t.category(), xLocation, yLocation);

            yLocation += 20;
            graphics.drawString("Image Path: " + t.image_path(), xLocation, yLocation);

            yLocation += 30;
            graphics.drawImage(ui.drawing_panel.getImage(), xLocation, yLocation, 400, 400, null); // TODO: How to scale image?

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

            // remove old treasure and add new one, then reload entries and change focus to the newly edited entry
            store.treasures.remove(old);
            store.treasures.add(edited);

            ui.loadEntries(store.treasures);
            ui.focus(edited);

        }

        if (e.getSource() == ui.delete) {
            Treasure selected_value = ui.list.getSelectedValue();
            if (selected_value == null) {
                ui.notify("Select an entry before attempting to delete it!");
                return;
            }

            store.delete(selected_value);

            // Reload the entries; and change focus to nothing
            ui.loadEntries(store.treasures);
            ui.focus();

        }

        if (e.getSource() == ui.undo) {
            Treasure undone = store.undo();

            if (undone == null) {
                ui.notify("Cannot undo: No undo history left!");
                return;
            }

            // Reload entries and refocus on the just-restored treasure
            ui.loadEntries(store.treasures);
            ui.focus(undone);
        }

        if ((e.getSource() == ui.category_filter) || (e.getSource() == ui.country_filter)) {
            ui.loadEntries(store.treasures);
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

            // Focus on the just found search result
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

            // Focus on the just found search result
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

            // reload the entries and focus on the just created entry
            ui.loadEntries(store.treasures);
            ui.focus(new_treasure);
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
