package uz.project.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


//* Use for now! Need to implement 'tail -f' reader, TailReader from commons.io dependency worked.
// Input file parser
public class InputHandler {
    FileChecker fileChecker = new FileChecker();

    // Saver from log file into list of strings
    public ArrayList<String> saveToList(String path) {

        ArrayList<String> lineList = new ArrayList<>();

        if (fileChecker.isChecked(path)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    lineList.add(line);
                }
            }
            catch(IOException e){
                throw new RuntimeException(e.getMessage());
            }
        }
         else {
            System.err.println("<ERROR> - Provide existing .log file only!!!");
        }
        return lineList;
    }

}
