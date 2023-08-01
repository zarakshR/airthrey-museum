import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class UI extends JFrame {

    static class LabelledText extends JPanel {

        private JLabel label;
        private JTextField text;

        public LabelledText(String label, int columns) {

            this.label = new JLabel(label);
            this.text = new JTextField(columns);

            this.add(this.label);
            this.add(this.text);

        }

    }

    static class DrawingPanel extends JPanel {

        private Image current_image = getToolkit().getImage("images/globe.jpg");

        public DrawingPanel() {

            setPreferredSize(new Dimension(400, 400));

        }

        public void setImage(Image newImage) {
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

    class MainPanel extends JPanel {

        class ControlButtons extends JPanel {

            public JButton save = new JButton("Save Changes");
            public JButton print = new JButton("Print Entry");
            public JButton update = new JButton("Update Entry");
            public JButton delete = new JButton("Delete Entry");
            public JButton undo = new JButton("Undo Deletion");

            public ControlButtons() {

                save.addActionListener(action_listener);
                print.addActionListener(action_listener);
                update.addActionListener(action_listener);
                delete.addActionListener(action_listener);
                undo.addActionListener(action_listener);

                add(save);
                add(print);
                add(update);
                add(delete);
                add(undo);

            }

        }

        private DrawingPanel drawing_panel = new DrawingPanel();
        private LabelledText name = new LabelledText("Name:", 20);
        private LabelledText category = new LabelledText("Category:", 10);
        private LabelledText image_path = new LabelledText("Image Path:", 20);
        public ControlButtons control_buttons = new ControlButtons();

        public MainPanel() {

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(drawing_panel);
            add(name);
            add(category);
            add(image_path);
            add(control_buttons);

        }

    }

    class TabbedPane extends JPanel {

        class CatalogueTab extends JPanel {

            private DefaultListModel<String> list_model = new DefaultListModel<String>();
            public JList<String> list = new JList<>(list_model);
            public JComboBox<String> room_filter = new JComboBox<>();
            public JComboBox<String> category_filter = new JComboBox<>();

            public CatalogueTab() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                list_model.addElement("ASD1");
                list_model.addElement("ASD2");
                list_model.addElement("ASD3");
                list_model.addElement("ASD4");
                list_model.addElement("ASD5");
                list_model.addElement("ASD6");

                list.addListSelectionListener(list_selection_listener);
                add(list);

                add(new JLabel("Filter by Room"));
                room_filter.addActionListener(action_listener);
                add(room_filter);

                add(new JLabel("Filter by Category"));
                category_filter.addActionListener(action_listener);
                add(category_filter);

            }

        }

        class SearchTab extends JPanel {

            private LabelledText query = new LabelledText("Query:", 20);
            public JButton name_search = new JButton("Search By Name");
            public JButton number_search = new JButton("Search By Catalogue No.");

            public SearchTab() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                add(query);

                name_search.addActionListener(action_listener);
                add(name_search);

                number_search.addActionListener(action_listener);
                add(number_search);

            }

        }

        class CreateTab extends JPanel {

            private LabelledText name = new LabelledText("Name:", 20);
            private LabelledText category = new LabelledText("Category:", 10);
            private LabelledText image_path = new LabelledText("Image Path:", 20);
            public JButton create = new JButton("Create New Entry");

            public CreateTab() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                add(name);
                add(category);
                add(image_path);

                create.addActionListener(action_listener);
                add(create);

            }

        }

        public CatalogueTab catalogue_tab = new CatalogueTab();
        public SearchTab search_tab = new SearchTab();
        public CreateTab create_tab = new CreateTab();

        private JTabbedPane tabbed_pane = new JTabbedPane();

        public TabbedPane() {

            this.setMinimumSize(new Dimension(650, 600));

            this.tabbed_pane.add("Catalogue", this.catalogue_tab);
            this.tabbed_pane.add("Search", this.search_tab);
            this.tabbed_pane.add("Create Entry", this.create_tab);

            this.add(this.tabbed_pane);

        }

    }

    Container container = getContentPane();

    public TabbedPane tabs;
    public MainPanel display;

    private ActionListener action_listener;
    private ListSelectionListener list_selection_listener;

    public <T extends ActionListener, U extends ListSelectionListener> UI(T action_listener, U list_selection_listener) {

        this.action_listener = action_listener;
        this.list_selection_listener = list_selection_listener;

        this.tabs = new TabbedPane();
        this.display = new MainPanel();

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(tabs);
        container.add(display);

    }

}
