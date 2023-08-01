import java.io.*;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Stack;

record Treasure(String name, String catalogue_number, String category, String image_path) {

    @Override
    public String toString() {
        return "(" + catalogue_number + ") " + name;
    }
}

// The data format is -
// Catalogue Number <TAB> Name <TAB> Image Path <TAB> Category
//
// The data file must have each entry on a separate line following the format above exactly
// None of the fields may be empty - If it is, the application quits 1 as the return code

class DataStore {

    private String filename;
    public HashSet<Treasure> treasures = new HashSet<>();
    private Stack<Treasure> undo_stack = new Stack<>();

    public DataStore(String filename) {

        this.filename = filename;

    }

    public void readData() {

        try {

            BufferedReader input = new BufferedReader(new FileReader(filename));

            String record;

            while ((record = input.readLine()) != null) {

                String[] fields = record.split("\t");

                String catalogue_number = fields[0];
                String name = fields[1];
                String image_path = fields[2];
                String category = fields[3];

                if ((catalogue_number.compareTo("") == 0)
                        || (name.compareTo("") == 0)
                        || (image_path.compareTo("") == 0)
                        || (category.compareTo("") == 0)) {
                    System.err.println("Corrupted data in " + filename + ": One or more fields are empty");
                    System.exit(1);
                }

                treasures.add(new Treasure(name, catalogue_number, category, image_path));

            }

            input.close();

        } catch (FileNotFoundException e) {

            System.err.println(e);

        } catch (IOException e) {

            System.err.println(e);

        }

    }

    public void writeData() {
        try {

            BufferedWriter output = new BufferedWriter(new FileWriter(filename));

            for (Treasure treasure : treasures) {
                output.write(
                        treasure.catalogue_number() + "\t" + treasure.name() + "\t" + treasure.image_path() + "\t" + treasure.category() + "\n");
            }

            output.close();

        } catch (IOException e) {

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

    public void delete(Treasure t) {
        undo_stack.add(t);
        treasures.remove(t);
    }

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
