import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.event.*;

// This class is a combination of a JLabel and a JTextField.
// It is separated out into a class because the combination of Label + Text needs to be used multiple times throughout the program
//  and it would be repetitive and error prone to specify a separate JLabel and JTextField everytime we wanted to do that.
class LabelledText extends JPanel {

    private JLabel label;
    private JTextField text;

    public LabelledText(String label, int columns) {

        this.label = new JLabel(label);
        this.text = new JTextField(columns);

        this.add(this.label);
        this.add(this.text);

    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}

class DrawingPanel extends JPanel {

    private Image current_image;

    public DrawingPanel() {

        setPreferredSize(new Dimension(400, 400));

    }

    public void setImage(Image image) {
        this.current_image = image;
        repaint();
    }

    public Image getImage() {
        return current_image;
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (current_image != null) {
            int xOffset = (getWidth() - current_image.getWidth(this)) / 2;
            int yOffset = (getHeight() - current_image.getHeight(this)) / 2;
            g.drawImage(current_image, xOffset, yOffset, this);
        }

    }

}

public class UI extends JFrame {

    Container container = getContentPane();

    // This stores the list of treasures
    private DefaultListModel<Treasure> list_model = new DefaultListModel<>();

    // This is the main display panel consisting of an DrawingPanel and LabelledText fields for the info for the currently selected entry
    // The display panel occupies the right half of the screen
    private JPanel display = new JPanel();
    public DrawingPanel drawing_panel = new DrawingPanel(); // This has to be public so Museum can use it for pprinting. Maybe change later?
    private LabelledText number = new LabelledText("Catalogue No.:", 5);
    private LabelledText name = new LabelledText("Name:", 20);
    private LabelledText category = new LabelledText("Category:", 10);
    private LabelledText image_path = new LabelledText("Image Path:", 20);
    private LabelledText country = new LabelledText("Country:", 10);

    // These buttons are placed below the main display panel. The provide buttons for saving, deleting, updating, etc;
    // The buttons are put into their own JPanel to make the layout cleaner (the buttons stay in one row).
    private JPanel control_buttons = new JPanel();
    public JButton save = new JButton("Save Changes");
    public JButton print = new JButton("Print Entry");
    public JButton update = new JButton("Update Entry");
    public JButton delete = new JButton("Delete Entry");
    public JButton undo = new JButton("Undo Deletion");

    // This occupies the left half of the GUI and provides tabs to switch between the list of treasures, a search panel, and a new treasure panel
    private JTabbedPane tabbed_pane = new JTabbedPane();

    // This shows a list of all treasures with an option to filter by category
    private JPanel catalogue_tab = new JPanel();
    public JList<Treasure> list = new JList<>(list_model);
    public DefaultComboBoxModel<String> category_filter_model = new DefaultComboBoxModel<>();
    public JComboBox<String> category_filter = new JComboBox<>(category_filter_model);
    public DefaultComboBoxModel<String> country_filter_model = new DefaultComboBoxModel<>();
    public JComboBox<String> country_filter = new JComboBox<>(country_filter_model);
    public JButton clear_filter_button = new JButton("Clear Filters");

    // This allows the creation of a new treasure
    private JPanel create_tab = new JPanel();
    private LabelledText new_name = new LabelledText("Name:", 20);
    private LabelledText new_number = new LabelledText("Catalogue No.:", 5);
    private LabelledText new_category = new LabelledText("Category:", 10);
    private LabelledText new_image_path = new LabelledText("Image Path:", 20);
    private LabelledText new_country = new LabelledText("Country:", 10);
    public JButton create_button = new JButton("Create New Entry");

    // This allows searching by name or catalogue number
    private JPanel search_tab = new JPanel();
    private LabelledText query = new LabelledText("Query:", 20);
    public JButton name_search_button = new JButton("Search By Name");
    public JButton number_search_button = new JButton("Search By Catalogue No.");

    private ActionListener action_listener;
    private ListSelectionListener list_selection_listener;

    // Let the constructor accept an ActionListener and a (possibly different) ListSelectionListener so that the software is more modular and we can
    // switch out Museum.java for a custom action handler(s) later if needed.
    public <T extends ActionListener, U extends ListSelectionListener> UI(T action_listener, U list_selection_listener) {

        this.action_listener = action_listener;
        this.list_selection_listener = list_selection_listener;

        // Since container only contains tabbed_pane and display, set the layout to be along the X-axis
        // this ensures that tabbed_pane and display take up the left and right halves of the screen respectively
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        container.add(tabbed_pane);
        container.add(display);

        // ----------------------------------------------------------------
        // MAIN DISPLAY - Shows the selected entry's image, name, category, image_path, and the control buttons vertically aligned
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        display.add(drawing_panel);
        display.add(number);
        display.add(name);
        display.add(category);
        display.add(image_path);
        display.add(country);
        display.add(control_buttons);

        // Buttons for saving changes, printing, updating changes, deleting entries, and undoing deletions
        control_buttons.add(save);
        control_buttons.add(print);
        control_buttons.add(update);
        control_buttons.add(delete);
        control_buttons.add(undo);

        save.addActionListener(action_listener);
        print.addActionListener(action_listener);
        update.addActionListener(action_listener);
        delete.addActionListener(action_listener);
        undo.addActionListener(action_listener);
        // END MAIN DISPLAY
        // ----------------------------------------------------------------
        // CATALOGUE TAB - Shows a list of entries and a drop down box to filter by category
        tabbed_pane.add("Catalogue", catalogue_tab);

        list.addListSelectionListener(list_selection_listener);
        catalogue_tab.add(list);

        catalogue_tab.add(new JLabel("Filter by Category"));
        category_filter.addActionListener(action_listener);
        catalogue_tab.add(category_filter);

        catalogue_tab.add(new JLabel("Filter by Country"));
        country_filter.addActionListener(action_listener);
        catalogue_tab.add(country_filter);

        clear_filter_button.addActionListener(action_listener);
        catalogue_tab.add(clear_filter_button);
        // END CATALOGUE TAB
        // ----------------------------------------------------------------
        // SEARCH TAB - A text field to input search queries and buttons to search by name or catalogue number
        tabbed_pane.add("Search", search_tab);

        search_tab.add(query);

        name_search_button.addActionListener(action_listener);
        search_tab.add(name_search_button);

        number_search_button.addActionListener(action_listener);
        search_tab.add(number_search_button);
        // END SEARCH TAB
        // ----------------------------------------------------------------
        // CREATE TAB - Text fields to add name, category, and image paths of treasure to be added and a button to create treasure
        tabbed_pane.add("Create Entry", create_tab);

        create_tab.add(new_name);
        create_tab.add(new_number);
        create_tab.add(new_category);
        create_tab.add(new_image_path);
        create_tab.add(new_country);

        create_button.addActionListener(action_listener);
        create_tab.add(create_button);
        // END CREATE TAB
        // ----------------------------------------------------------------
    }

    // Load all treasures from the given hash set into list_model; takes active filters into account
    public void loadEntries(HashSet<Treasure> treasures) {

        list_model.removeAllElements();

        String selected_category = (String) category_filter.getSelectedItem();
        String selected_country = (String) country_filter.getSelectedItem();

        if ((selected_category == null) && (selected_country == null)) {

            list_model.addAll(treasures);

        } else if (selected_category == null) {

            for (Treasure treasure : treasures) {
                if (treasure.country().compareTo(selected_country) == 0) {
                    list_model.addElement(treasure);
                }
            }

        } else if (selected_country == null) {

            for (Treasure treasure : treasures) {
                if (treasure.category().compareTo(selected_category) == 0) {
                    list_model.addElement(treasure);
                }
            }

        } else {

            for (Treasure treasure : treasures) {
                if ((treasure.category().compareTo(selected_category) == 0) && (treasure.country().compareTo(selected_country) == 0)) {
                    list_model.addElement(treasure);
                }
            }

        }

    }

    // Find all unique categories and countries from the given hash set and add them into category_filter_model and country_filter_model
    public void loadFilters(HashSet<Treasure> treasures) {

        category_filter_model.removeAllElements();
        country_filter_model.removeAllElements();

        // Use a set for the filters so duplicates get ignored automatically
        HashSet<String> filter_set = new HashSet<>();
        HashSet<String> country_set = new HashSet<>();

        for (Treasure treasure : treasures) {
            filter_set.add(treasure.category());
            country_set.add(treasure.country());
        }

        category_filter_model.addAll(filter_set);
        country_filter_model.addAll(country_set);

    }

    public void clearFilters() {

        category_filter.setSelectedItem(null);
        country_filter.setSelectedItem(null);

    }

    // Make the main display panel "focus on" (i.e., change the name, category, image, etc to) the given treasure
    public void focus(Treasure t) {

        list.setSelectedValue(t, true);
        drawing_panel.setImage(getToolkit().getImage(t.image_path()));
        name.setText(t.name());
        number.setText(t.catalogue_number());
        category.setText(t.category());
        image_path.setText(t.image_path());
        country.setText(t.country());

    }

    // Focus on nothing
    public void focus() {

        list.setSelectedValue(null, false);
        drawing_panel.setImage(null);
        name.setText("");
        number.setText("");
        category.setText("");
        image_path.setText("");
        country.setText("");

    }

    public String getSearchQuery() {

        return query.getText();

    }

    // Get a new treasure made from the values currently in the description boxes
    public Treasure getEditedTreasure() {

        String new_name = name.getText();
        String new_number = number.getText();
        String new_category = category.getText();
        String new_image_path = image_path.getText();
        String new_country = country.getText();

        if ((new_name.compareTo("") == 0)
                || (new_number.compareTo("") == 0)
                || (new_image_path.compareTo("") == 0)
                || (new_category.compareTo("") == 0)
                || (new_country.compareTo("") == 0)) {
            return null;
        }

        return new Treasure(new_name, new_number, new_category, new_image_path, new_country);

    }

    // Get a new treasure made from the values currently in the creat entry boxes
    public Treasure getNewTreasure() {

        String name = new_name.getText();
        String number = new_number.getText();
        String category = new_category.getText();
        String image_path = new_image_path.getText();
        String country = new_country.getText();

        if ((name.compareTo("") == 0)
                || (number.compareTo("") == 0)
                || (image_path.compareTo("") == 0)
                || (category.compareTo("") == 0)
                || (country.compareTo("") == 0)) {
            return null;
        }

        return new Treasure(name, number, category, image_path, country);

    }

    public void notify(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
