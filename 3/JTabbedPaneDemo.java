/*
   An application demonstrating a JTabbedPane

   The main layout is a single JTabbedPane, which displays only one of
   three separate coloured panels at any time. The user can switch
   between the panels using the named tabs along the top, or, on the
   red panel, explicit switching buttons.

   Note: The colouring is there only to emphasise visually in the demo that
   the panels are switching - it is NOT necessarily a good GUI design!]

   The individual coloured panels contain different combinations of GUI components
   - showing that they can have aspects special to themselves.

   There is a single integer variable, counter, which is "shared" by the three panels.
   It is accessed by each panel in different ways:
     o the red panel displays its value,
     o the yellow panel has a button for incrementing it
     o the cyan panel displays it and also has a decrementing button.

   The cyan panel also contains an editable text field. This is not used for
   anything in this program, but it does show how such a data entry field
   can be displayed, and how it retains it contents through panel switching
   actions.

   The way that the JTabbedPane layout is built is shown in method init.

   [Advanced programming note: This program is all in one class, and is rather
   unwieldy. There is a more sophisticated organization in which each of the
   panels is actually a separate class. The monolithic approach followed here is
   just about tolerable, but for bigger problems might become unmanageable.]

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JTabbedPaneDemo
      extends JFrame
      implements ActionListener {
   public static void main(String[] args) {
      JTabbedPaneDemo demo = new JTabbedPaneDemo();
      demo.pack(); // Adjust to fit the contents
      demo.setLocation(200, 100); // Window position on screen
      demo.setVisible(true);
   } // main

   public JTabbedPaneDemo() { // Constructor
      setTitle("JTabbedPane demonstration");
      setDefaultCloseOperation(EXIT_ON_CLOSE); // For main close box click event
      init(); // Sets up the GUI
   } // JTabbedPaneDemo constructor

   private JTabbedPane switchable; // Will contain and manage the switchable panels

   private JPanel redPanel, yellowPanel, cyanPanel; // Only one of these will be visible at any time within switchable

   private int counter; // Some globally accessible data

   // For the red panel:
   private JLabel redMessage = new JLabel("This panel contains a counter display and switching buttons"),
         counterOnRed = new JLabel("           "); // To display the counter
   private JButton goToYellow = new JButton("Go to yellow panel"), // Direct movement buttons
         goToCyan = new JButton("Go to cyan panel");

   // For the yellow panel:
   private JLabel yellowMessage = new JLabel("This panel contains a button to increment the global counter");
   private JButton incOnYellow = new JButton("+1 to counter"); // As it says

   // For the cyan panel:
   private JLabel cyanMessage1 = new JLabel("This panel contains a display of the counter,"),
         cyanMessage2 = new JLabel("a button to decrement the counter, and an editable text field");
   private JButton decOnCyan = new JButton("-1 from counter"); // As it says!
   private JLabel counterOnCyan = new JLabel("           "); // To display the counter
   private JTextField textOnCyan = new JTextField(20);

   // Set up the GUI and data
   public void init() {

      Container contentPane = getContentPane();
      contentPane.setLayout(new FlowLayout()); // Otherwise Frame defaults to BorderLayout

      redPanel = new JPanel(); // Create the red panel
      redPanel.setBackground(Color.red);
      redPanel.add(redMessage); // Add message label
      redPanel.add(counterOnRed); // Add label to display counter
      redPanel.add(goToYellow); // Explicit panel switching buttons
      goToYellow.addActionListener(this);
      redPanel.add(goToCyan);
      goToCyan.addActionListener(this);

      yellowPanel = new JPanel(); // Create the yellow panel
      yellowPanel.setBackground(Color.yellow);
      yellowPanel.add(yellowMessage); // Add message label
      yellowPanel.add(incOnYellow); // Add button to increment counter
      incOnYellow.addActionListener(this);

      cyanPanel = new JPanel(); // Create the cyan panel
      cyanPanel.setBackground(Color.cyan);
      cyanPanel.add(cyanMessage1); // Add message labels
      cyanPanel.add(cyanMessage2);
      cyanPanel.add(counterOnCyan); // Add label to display counter
      cyanPanel.add(decOnCyan); // Add button to decrement counter
      decOnCyan.addActionListener(this);
      cyanPanel.add(textOnCyan); // And the editable text field

      switchable = new JTabbedPane(); // Will have tabs at the top and the replaceable coloured panels as added
                                      // components
      switchable.setPreferredSize(new Dimension(350, 200)); // Set a suitable size

      switchable.add("Red panel", redPanel); // The JTabbedPane will only display one panel at a time
      switchable.add("Yellow panel", yellowPanel);
      switchable.add("Cyan panel", cyanPanel);
      contentPane.add(switchable);

      counter = 0; // Initialise the global data
      updateGUI(); // Make sure the GUI shows up to date info

   } // init

   // Handle button presses
   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == goToYellow) // Change displayed panel
         switchable.setSelectedComponent(yellowPanel);
      if (e.getSource() == goToCyan) // Change displayed panel
         switchable.setSelectedComponent(cyanPanel);
      if (e.getSource() == incOnYellow) // Change counter if requested
         counter++;
      if (e.getSource() == decOnCyan)
         counter--;
      updateGUI(); // Bring displayed data up to date on all panels

   } // actionPerformed

   // Make sure that the GUI displays up to date info
   private void updateGUI() {

      counterOnRed.setText("Counter is " + Integer.toString(counter) + " ");
      counterOnCyan.setText("Counter is " + Integer.toString(counter) + " ");

   } // updateGUI

} // class JTabbedPaneDemo
