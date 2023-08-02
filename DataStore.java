import java.io.*;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Stack;

record Treasure(String name, String catalogue_number, String category, String image_path, String country) {

    @Override
    public String toString() {
        return "(" + catalogue_number + ") " + name;
    }

}

// The data format is -
// Catalogue Number <TAB> Name <TAB> Image Path <TAB> Category <TAB> Country
//
// The data file must have each entry on a separate line following the format above exactly
// None of the fields may be empty - If it is, the application quits 1 as the return code

class DataStore {

    private String filename;
    public HashSet<Treasure> treasures = new HashSet<>();
    private Stack<Treasure> undo_stack = new Stack<>(); // Use a stack for deleted treasures since we need LIFO semantics for undoing

    public DataStore(String filename) {

        this.filename = filename;

    }

    public void readData() {

        try {

            BufferedReader input = new BufferedReader(new FileReader(filename));

            // A string "buffer" to store each line while we process it.
            String record;

            while ((record = input.readLine()) != null) {

                // Split the read in line along the tab character
                String[] fields = record.split("\t");

                if (fields.length > 5) {
                    // If there are more than five fields, we can still make use of the data, but atleast let the user know something is wrong
                    System.err.println("WARNING: Extra data fields in " + filename);
                    System.err.println("Ignoring any additional fields");
                } else if (fields.length < 5) {
                    // If there are less than five fields, the data is corrupted and it is not safe to continue
                    System.err.println("ERROR: Missing data fields in " + filename);
                    System.err.println(
                            "The data store must be in the following format: Catalogue Number <TAB> Name <TAB> Image Path <TAB> Category <TAB> Country");
                    System.exit(1);
                }

                // "Destructure" the array to get the data in each field
                String catalogue_number = fields[0];
                String name = fields[1];
                String image_path = fields[2];
                String category = fields[3];
                String country = fields[4];

                // If any of the fields is empty, it is an error and we should abort
                if ((catalogue_number.compareTo("") == 0)
                        || (name.compareTo("") == 0)
                        || (image_path.compareTo("") == 0)
                        || (category.compareTo("") == 0)
                        || (country.compareTo("") == 0)) {
                    System.err.println("Corrupted data in " + filename + ": One or more fields are empty");
                    System.err.println("The data store must be in the following format: Catalogue Number <TAB> Name <TAB> Image Path <TAB> Category <TAB> Country");
                    System.exit(1);
                }

                treasures.add(new Treasure(name, catalogue_number, category, image_path, country));

            }

            input.close();

        } catch (FileNotFoundException e) {

            System.err.println(e);
            System.exit(2);

        } catch (IOException e) {

            System.err.println(e);
            System.exit(2);

        }

    }

    public void writeData() {
        try {

            BufferedWriter output = new BufferedWriter(new FileWriter(filename));

            // Just loop through each treasure and output its fields tab-delimited
            for (Treasure treasure : treasures) {
                output.write(
                        treasure.catalogue_number()
                            + "\t" + treasure.name()
                            + "\t" + treasure.image_path()
                            + "\t" + treasure.category()
                            + "\t" + treasure.country() + "\n");
            }

            output.close();

        } catch (IOException e) {

            System.err.println(e);
            System.exit(3);

        }

    }

    // Searches for and returns the *first* treasure that matches the given name
    public Treasure searchByName(String name) {

        for (Treasure treasure : treasures) {
            if (treasure.name().compareTo(name) == 0) {
                return treasure;
            }
        }

        return null;

    }

    // Same as above but searches by catalogue number
    public Treasure searchByNumber(String number) {

        for (Treasure treasure : treasures) {
            if (treasure.catalogue_number().compareTo(number) == 0) {
                return treasure;
            }
        }

        return null;

    }

    // Deletes a treasure, but silently adds it to the undo stack
    public void delete(Treasure t) {
        undo_stack.add(t);
        treasures.remove(t);
    }

    // Return last treasure added to undo stack or null if none exists
    public Treasure undo() {
        try {
            Treasure t = undo_stack.pop();
            treasures.add(t);
            return t;
        } catch (EmptyStackException e) {
            return null;
        }
    }
}
