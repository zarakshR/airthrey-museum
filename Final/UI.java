import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.event.*;

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

    private DefaultListModel<Treasure> list_model = new DefaultListModel<>();

    private JPanel display = new JPanel();
    private DrawingPanel drawing_panel = new DrawingPanel();
    private LabelledText number = new LabelledText("Catalogue No.:", 5);
    private LabelledText name = new LabelledText("Name:", 20);
    private LabelledText category = new LabelledText("Category:", 10);
    private LabelledText image_path = new LabelledText("Image Path:", 20);

    private JPanel control_buttons = new JPanel();
    public JButton save = new JButton("Save Changes");
    public JButton print = new JButton("Print Entry");
    public JButton update = new JButton("Update Entry");
    public JButton delete = new JButton("Delete Entry");
    public JButton undo = new JButton("Undo Deletion");

    private JTabbedPane tabbed_pane = new JTabbedPane();

    private JPanel catalogue_tab = new JPanel();
    public JList<Treasure> list = new JList<>(list_model);
    public DefaultComboBoxModel<String> category_filter_model = new DefaultComboBoxModel<>();
    public JComboBox<String> category_filter = new JComboBox<>(category_filter_model);
    public JButton clear_filter_button = new JButton("Clear Filter");

    private JPanel create_tab = new JPanel();
    private LabelledText new_name = new LabelledText("Name:", 20);
    private LabelledText new_number = new LabelledText("Catalogue No.:", 5);
    private LabelledText new_category = new LabelledText("Category:", 10);
    private LabelledText new_image_path = new LabelledText("Image Path:", 20);
    public JButton create_button = new JButton("Create New Entry");

    private JPanel search_tab = new JPanel();
    private LabelledText query = new LabelledText("Query:", 20);
    public JButton name_search_button = new JButton("Search By Name");
    public JButton number_search_button = new JButton("Search By Catalogue No.");

    private ActionListener action_listener;
    private ListSelectionListener list_selection_listener;

    public <T extends ActionListener, U extends ListSelectionListener> UI(T action_listener, U list_selection_listener) {

        this.action_listener = action_listener;
        this.list_selection_listener = list_selection_listener;

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

        create_button.addActionListener(action_listener);
        create_tab.add(create_button);
        // END CREATE TAB
        // ----------------------------------------------------------------
    }

    // Load all treasures from the given hash set into list_model
    public void loadEntries(HashSet<Treasure> treasures) {

        list_model.removeAllElements();
        list_model.addAll(treasures);

    }

    // Like above but only add the treasures whose categories match the given category
    public void loadEntries(HashSet<Treasure> treasures, String category) {

        list_model.removeAllElements();

        for (Treasure treasure : treasures) {
            if (treasure.category().compareTo(category) == 0) {
                list_model.addElement(treasure);
            }
        }

    }

    // Find all unique categories from the given hash set and add them into category_filter_model
    public void loadFilters(HashSet<Treasure> treasures) {

        category_filter_model.removeAllElements();

        // Use a set for the filters so duplicates get ignored automatically
        HashSet<String> filter_set = new HashSet<>();
        for (Treasure treasure : treasures) {
            filter_set.add(treasure.category());
        }

        category_filter_model.addAll(filter_set);

    }

    public void clearFilters() {

        category_filter.setSelectedItem(null);

    }

    // Make the main display panel "focus on" (i.e., change the name, category, image, etc to) the given treasure
    public void focus(Treasure t) {

        list.setSelectedValue(t, true);
        drawing_panel.setImage(getToolkit().getImage(t.image_path()));
        name.setText(t.name());
        number.setText(t.catalogue_number());
        category.setText(t.category());
        image_path.setText(t.image_path());

    }

    // Focus on nothing
    public void focus() {

        list.setSelectedValue(null, false);
        drawing_panel.setImage(null);
        name.setText("");
        number.setText("");
        category.setText("");
        image_path.setText("");

    }

    public String getSearchQuery() {

        return query.getText();

    }

    // Get a new treasure that made from the values currently in the description boxes
    public Treasure getEditedTreasure() {

        String new_name = name.getText();
        String new_number = number.getText();
        String new_category = category.getText();
        String new_image_path = image_path.getText();

        if ((new_name.compareTo("") == 0)
                || (new_number.compareTo("") == 0)
                || (new_image_path.compareTo("") == 0)
                || (new_category.compareTo("") == 0)) {
            return null;
        }

        return new Treasure(new_name, new_number, new_category, new_image_path);

    }

}
