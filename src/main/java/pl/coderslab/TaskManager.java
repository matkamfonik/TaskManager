package pl.coderslab;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        String databaseName = "tasks.csv";
        Path databasePath = Paths.get(databaseName);
        ;
        try (Scanner readFile = new Scanner(databasePath)) {
            while (readFile.hasNextLine()) {
                System.out.println(readFile.nextLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
