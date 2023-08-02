// My code assumes that each number maps to a unique entry (i.e., the number field is a primary key)

/*
   DataStore class supporting the main FileDemo program

   Each instance of this class looks after a collection of text/number pairs in two arrays:
   The string array "texts" contains text items from element 0 up and including the element
   indicated by the variable "top".
   The int array "numbers" is similar.
   Each specific element in "texts" has a corresponding int in "numbers" at the same
   array index position.

   Initially the collection is empty. "top" is initially -1 to indicate this.

   Methods available are:

   o readData: The data storage arrays are in effect emptied by setting top to -1.
      Then the contents of file data.txt are read in. Each line in the
      file consists of some text, then a tab, then a number.
      Each line of text is split up into its text and number components,
      and the text/number pair is added to the arrays (by calls of addEntry).

   o writeData: The entire current contents of the data store is written
      out to file data.txt, overwriting its previous contents.
      The output format precisely mirrors the original data format in
      data.txt, so that the data can subsequently be read back in by readData.

   o addEntry: When called, adds a given text/number pair to the data store
      by increasing "top" and inserting the new data into the elements
      indicated by "top".

   * updateDescription: Change the description (text part) of an entry to a given string

   o lookupNumber: Given a text, seeks it in the texts array and returns the
      corresponding number from the numbers array.

   * lookupText: Lookup the text entry at a specific index

   * lookupImagePath: Lookup up the image path for the entry at a given index

   o doubleNumber: Given a text, seeks it in the texts array and doubles the
     number in the corresponding element of the numbers array.

   o fillChoice: Fills up a given JComboBox widget with the contents of the texts array.

*/

import java.awt.*;
import javax.swing.*;
import java.io.*;

class DataStore {

    private final int MAX = 50; // Used as limit on arrays

    private String[] texts = new String[MAX]; // Three arrays for data storage
    private String[] imagePaths = new String[MAX];
    private String[] roomNumbers = new String[MAX];
    private int[] numbers = new int[MAX];
    private int top = -1; // Entries indexed 0..top of the arrays are occupied

    // No constructor needed here: all the initialization is in the declarations
    // above

    // Read data from data.txt, split up each line and store the data in the arrays.
    // If any ill formatted data, or other file problem is encountered,
    // the reading is abandoned, but storing all correctly read data up to that
    // point.
    public void readData() {

        top = -1; // Empty current data storage by adjusting the top index.
        try {
            BufferedReader input = new BufferedReader(new FileReader("treasures.txt"));
            String dataLine; // To receive each line from the file

            while ((dataLine = input.readLine()) != null) { // Get next line from file

                int tabpos1; // Splits upto end of text part
                int tabpos2; // Splits upto end of number part
                int tabpos3; // Splits upto end of image path part
                // text after tabpos3 will be the room number

                // Find the position of the *first* tab for splitting the line
                tabpos1 = dataLine.indexOf('\t');
                // Check, in case no tab was found (indexOf returns -1)
                if (tabpos1 < 0) {
                    System.out.println("Ill-formatted tabs");
                    break; // Bad data: read no more!
                }

                tabpos2 = dataLine.indexOf('\t', tabpos1 + 1);
                if (tabpos2 < 0) {
                    System.out.println("Diagnostic: Ill-formatted tabs or missing data");
                    break;
                }

                tabpos3 = dataLine.indexOf('\t', tabpos2 + 1);
                if (tabpos3 < 0) {
                    System.out.println("Diagnostic: Ill-formatted tabs or missing data");
                    break;
                }

                // Split the line: extract the parts of the string up to the tab,
                // and from after the tab to the end of the string
                String textPart = dataLine.substring(0, tabpos1);
                String numberPart = dataLine.substring(tabpos1 + 1, tabpos2);
                String imagePathPart = dataLine.substring(tabpos2 + 1, tabpos3);
                String roommNumberPart = dataLine.substring(tabpos3 + 1);

                // Convert numberPart to a proper int for storing
                int n = 0; // To hold the converted number
                try {
                    n = Integer.parseInt(numberPart); // Convert
                } catch (NumberFormatException ex) {
                    System.out.println("Bad data in number");
                    break; // Bad data: read no further!
                }

                // We now have the text and number parts,
                // so store the data obtained as next entry in the arrays
                addEntry(textPart, n, imagePathPart, roommNumberPart);
            }

            for (String string : roomNumbers) {
                System.out.print(string);
            }
            System.out.println("DataStore.readData()");

            input.close(); // File finished, arrays full or bad data, so close file
        } catch (IOException ex) {
            System.out.println("File reading error"); // File handling error: read no further
        }

    } // readData

    // Write the array contents out to data.txt, in a compatible format for readData
    public void writeData() {

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("treasures.txt"));

            // Process each stored text/number pair from 0 to top
            for (int i = 0; i <= top; i++) {

                // Build a correctly structured string as an output line:
                // the two corresponding array items are separated by a tab
                String theNumber = Integer.toString(numbers[i]);

                String outputLine = texts[i] + "\t" + theNumber + "\t" + imagePaths[i] + "\t" + roomNumbers[i];
                // And output the line to the file, followed by a new line
                output.write(outputLine);
                output.newLine();

            }

            output.close();
        } catch (IOException ex) {
            System.out.println("File writing error"); // File management error: write no more
        }

    } // writeData

    // Add one more text/number entry to the arrays, if there is space.
    // If there is no space left, the new data is simply ignored, with no error
    // report
    public void addEntry(String text, int n, String imagePath, String roomNumber) {

        if (top == MAX - 1)
            return; // If arrays are full: no addition of data

        // There is space, so add new items
        top++; // Adjust pointer to next free space
        texts[top] = text; // and put the data into that space
        numbers[top] = n;
        imagePaths[top] = imagePath;
        roomNumbers[top] = roomNumber;

    } // addEntry

    // Update texts at the given index with newDescription
    public void updateDescription(String newDescription, int n) {
        texts[n] = newDescription;
    }

    // Search for the given text in the texts array,
    // and return the corresponding number, or 0 if not found.
    // (0 is not a clever way to deal with "not found" but it will do for now!)
    public int lookupNumber(String text) {

        // Scan all the entries
        for (int i = 0; i <= top; i++)

            if (text.equals(texts[i])) // Check next text
                // Found the required entry! Return the corresponding number
                return numbers[i];

        // Execution will only arrive here if didn't find the required entry
        return -1;

    } // lookupNumber

    public String lookupText(int n) {
        return texts[n];
    }

    public String lookupRoomNumber(int n) {
        return roomNumbers[n];
    }

    public String lookupImagePath(int n) {
        System.out.println("Requested: " + imagePaths[n]);
        return imagePaths[n];
    }

    public int searchByName(String query) {
        System.out.println("Search request: " + query);
        for (int i = 0; i < top; i++) {
            if (texts[i].compareTo(query) == 0) {
                return i;
            }
        }

        return -1;
    }

    // Search for the given text in the texts array,
    // and double the corresponding number, or no action if not found
    public void doubleNumber(String text) {

        // Scan all the entries
        for (int i = 0; i <= top; i++)

            if (text.equals(texts[i])) { // Check next text
                // Found it, so double the number
                numbers[i] = 2 * numbers[i];
                // And leave the loop and method immediately because the work is done!
                return;
            }

    } // doubleNumbers

    // Fills list with all *unique* room numbers
    public void fillRoomNumbers(JComboBox<String> list) {

        list.removeAllItems();

        String[] scratch = new String[MAX];

        int flag;
        int scratch_top = 0;

        for (int i = 0; i < top; i++) {

            flag = 0;

            for (int j = 0; j < scratch_top; j++) {
                if (scratch[j].compareTo(roomNumbers[i]) == 0) {
                    flag = 1;
                }
            }

            if (flag == 0) {
                scratch[scratch_top] = roomNumbers[i];
                scratch_top++;
            }

        }

        for (int i = 0; i < scratch_top; i++) {
            list.addItem(scratch[i]);
        }

        if (scratch_top >= 0) {
            list.setSelectedIndex(0);
        }
    }

    // Fills list with all items in a specific room
    public void fillChoice(JComboBox<String> list, String roomNumber) {

        list.removeAllItems();

        int flag = 0;
        for (int i = 0; i < top; i++) {
            if (roomNumbers[i].compareTo(roomNumber) == 0) {
                list.addItem(texts[i]);
                flag = 1;
            }
        }

        if (flag == 1) {
            list.setSelectedIndex(0);
        }
    }

    // Fill up the given choice list with the contents of the texts array
    public void fillChoice(JComboBox<String> list) {

        // Empty the current entries in the list
        list.removeAllItems();
        // Step through the occupied part of the array
        for (int i = 0; i <= top; i++)
            // Add next text item to the list
            list.addItem(texts[i]);

        // Finally if there is at least one entry, select the first one
        if (top >= 0)
            list.setSelectedIndex(0);

    } // fillChoice

} // class DataStore
