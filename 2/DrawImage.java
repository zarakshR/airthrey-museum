/*
   Demonstrating how to display a picture from a file in an application's window.

   A very simple application that displays one of two different GIF images
   in a drawing panel positioned between two image selector buttons.

*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawImage extends JFrame
                       implements ActionListener {

    // Start up the application
    public static void main(String args[]) {

        DrawImage app = new DrawImage();
        app.setLocation(200,200);
        app.pack();   // This makes the window exactly the right size for its laid out contents
                      // or could instead explicitly set the size, eg app.setSize(600,500);
        app.setVisible(true);

    } // main

    // Fetch the two images from given file names
    private Image scotlandMapImage = getToolkit().getImage("scotland-map.gif"),
                  uosLogoImage = getToolkit().getImage("uos.png");

    private JButton choice1 = new JButton("Show Scotland Map gif"),       // Image choice buttons
                    choice2 = new JButton("Show University logo png");

    private DrawingPanel thePicture = new DrawingPanel();             // Widget for displaying the image

    // Constructor: lays out the display
    public DrawImage() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);                      // For main close box click event

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(choice1);
        choice1.addActionListener(this);
        contentPane.add(thePicture);
        contentPane.add(choice2);
        choice2.addActionListener(this);

    } // DrawImage constructor

    // actionPerformed: choose a picture to display and notify the drawing panel (it repaints itself)
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == choice1)
            thePicture.setImage(scotlandMapImage);
        if(e.getSource() == choice2)
            thePicture.setImage(uosLogoImage);

    } // actionPerfomed

} // class DrawImage
