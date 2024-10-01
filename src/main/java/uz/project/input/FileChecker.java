package uz.project.input;

import java.io.File;


// File validator
public class FileChecker {


    // Method to check whether given path is log file
    public boolean isChecked(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return false;
        }

        if (!file.isFile()) {
            System.err.println("Path is not a file: " + filePath);
            return false;
        }

        if (!filePath.toLowerCase().endsWith(".log")) {
            System.err.println("File is not a log file: " + filePath);
            return false;
        }

        return true;
    }
}
