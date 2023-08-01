import java.io.*;
import java.util.HashSet;

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

            System.out.println(treasures);

        } catch (FileNotFoundException e) {

            System.err.println(e);

        } catch (IOException e) {

            System.err.println(e);

        }

    }

    public void writeData() {
    }

}
