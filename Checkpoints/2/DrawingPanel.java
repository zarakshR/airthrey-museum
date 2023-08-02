// Class DrawingPanel: to display a chosen image.
// The image to be displayed is notified to this class by calling its setImage method.

import java.awt.*;
import javax.swing.*;

public class DrawingPanel extends JPanel {

    // Constructor: only need to set up the size here, so the layout manager knows
    public DrawingPanel() {

        // Do not set any background color; let it remain transparent so we don't have
        // to fight with the image dimensions
        setPreferredSize(new Dimension(400, 400));

    } // DrawingPanel constructor

    private Image image; // The image on display just now (null initially)

    // Set a new image to be displayed, and refresh the panel on-screen
    public void setImage(Image newImage) {

        image = newImage;
        repaint();

    } // setImage

    // And this actually draws the image, centred in the panel:
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (image != null) { // Make sure that there is an image
            int xOffset = (getWidth() - image.getWidth(this)) / 2; // x and y offsets to center the image in the panel
            int yOffset = (getHeight() - image.getHeight(this)) / 2;
            g.drawImage(image, xOffset, yOffset, this); // Note: Coordinates relative to this panel
        }

    } // paintComponent

} // class DrawingPanel
