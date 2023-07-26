/*
   File reading/writing demo application

   Reads in file data.txt comprising lines with two items of data on each:
   some text, and an integer, separated by a tab character. The data is read in
   and stored by an instance of class DataStore.

   The data is displayed: A choice list (JComboBox) is displayed with all the texts,
   and a JTextField shows the number associated with the currently selected text.

   When the Double button is clicked, the stored integer associated with the currently
   selected text is doubled in the array where it is located, and the screen display is updated.

   When the Store button is clicked, all the data is written back to the file data.txt.
   The data is NOT written to the file automatically when the program exits!

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileDemo
        extends JFrame
        implements ActionListener // For the JButton handling and item selection in the JComboBox
{

    // Set up the application
    public static void main(String[] args) {

        FileDemo demo = new FileDemo();
        demo.setSize(300, 200); // Width, height of window
        demo.setLocation(200, 100); // Where on the screen
        demo.setVisible(true);

    } // main

    // Constructor
    public FileDemo() {

        setTitle("Files demonstration");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // For main close box click event
        init(); // Set up the GUI and data

    } // FileDemo

    private DataStore theData = new DataStore(); // Create a new DataStore object to hold all the data

    private JButton doubleButton = new JButton("Double"), // The buttons for the GUI
            storeButton = new JButton("Store"),
            updateDescriptionButton = new JButton("Update Description");

    private JComboBox<String> textChoice = new JComboBox<String>(); // For displaying a selectable text list
    private JTextField numberField = new JTextField(10); // For diplaying the number associated with the selected text
                                                         // item
    private JTextField descriptionField = new JTextField(80); // For diplaying the description text box that the user
                                                              // will use to update the description text

    private DrawingPanel drawingPanel = new DrawingPanel(); // The panel on which we'll draw our images
    private Image currentImage; // Holds the currently displayed image

    // Read the data file into the object theData, and set up the GUI
    public void init() {

        // Instruct the DataStore object theData to read in the data file
        theData.readData();

        // Set up the GUI
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        // The buttons notify actionPerformed when clicked
        contentPane.add(doubleButton);
        doubleButton.addActionListener(this);
        contentPane.add(storeButton);
        storeButton.addActionListener(this);

        contentPane.add(updateDescriptionButton);
        updateDescriptionButton.addActionListener(this);

        // Tell theData to fill up the drop-down JComboBox textChoice with the text
        // items,
        // add textChoice to the display,
        // and set it to notify actionPerformed when an item is selected
        theData.fillChoice(textChoice);
        contentPane.add(textChoice);
        textChoice.addActionListener(this);

        // Display numberField, and disable user editing
        contentPane.add(numberField);
        numberField.setEditable(false);

        // Display the description text box
        contentPane.add(descriptionField);
        descriptionField.setText(theData.lookupText(textChoice.getSelectedIndex()));

        contentPane.add(drawingPanel);
        drawingPanel.setImage(getToolkit().getImage(theData.lookupImagePath(textChoice.getSelectedIndex())));

        // Finally, make sure that initial display in numberField
        // is consistent with the initially selected text item
        updateNumberField();

    } // init

    // Handle button presses and item selection events
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == doubleButton) {
            // Need to double the currently selected integer
            // First find out Which text item is selected
            String chosen = (String) textChoice.getSelectedItem();
            // Now tell theData to find chosen in its data store and to double the number
            // associated with it
            theData.doubleNumber(chosen);
            // Finally update the number field display
            updateNumberField();
        }

        if (e.getSource() == storeButton)
            // Tell theData to write its contents back to the file
            theData.writeData();

        if (e.getSource() == updateDescriptionButton) {
            // Get the text currently in the description text box
            String newDescription = descriptionField.getText();
            // Get the currently selected text entry
            int choice_n = textChoice.getSelectedIndex();
            // Update entries with the new description
            theData.updateDescription(newDescription, choice_n);
            // Re-populate textChoice completely and re-select the currently selected index
            // This is inefficient but simple; I am holding off from any optimizations
            // because the dataset is currently small
            theData.fillChoice(textChoice);
            textChoice.setSelectedIndex(choice_n);
        }

        if (e.getSource() == textChoice) {
            // Handle an event from the combo box list: it might be an item
            // selection, or deselection or some other change to the combo box.
            // Note: Deselection occurs when changing changing a selection, or
            // removing the currently selected item - including when all items
            // are removed from the list. In this situation there will not be a
            // selected item so we must check and avoid processing when there is
            // no selected item.
            // What needs to be done is for the number field to be brought up to date
            // with the new currently selected text, or set to blank if nothing is selected.
            // The check for "nothing selected" can be done here, or in updateNumberField
            // (in this example code it is done in both).
            if (textChoice.getSelectedIndex() == -1) { // -1 indicates "nothing selected"
                // No item is currently selected
                numberField.setText("");
                currentImage = null;
            } else {
                descriptionField.setText(theData.lookupText(textChoice.getSelectedIndex()));
                drawingPanel.setImage(getToolkit().getImage(theData.lookupImagePath(textChoice.getSelectedIndex())));
            }
            updateNumberField();
        }

    } // actionPerformed

    // Display the correct integer from the data store in numberField
    // - the one associated with the currently selected text,
    // or set to blank if nothing is selected
    private void updateNumberField() {

        // First find out which text item is selected
        String chosen = (String) textChoice.getSelectedItem(); // Will be null if nothing selected
        if (chosen == null) {
            numberField.setText("");
        } else {
            // Then ask theData to look up the chosen text in its data store, and to return
            // the associated number
            int theNumber = theData.lookupNumber(chosen);
            // Finally display the number in the number field
            numberField.setText(Integer.toString(theNumber));
        }

    } // updateNumberField

} // class FileDemo
