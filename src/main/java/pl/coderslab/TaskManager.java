package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        final String fileName = "tasks.csv";
        Path filePath = Paths.get(fileName);
        String[][] data = readFile(filePath);
        String option = getOption();
        while (!"exit".equals(option)) {
            System.out.println(option);
            switch (option) {
                case "add":
                    data = Arrays.copyOf(data, data.length + 1);
                    data[data.length - 1] = newTask();
                    option = getOption();
                    break;
                case "remove":
                    while (true) {
                        try {
                            data = ArrayUtils.remove(data, removeTaskNumber());
                            break;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("There is no task in selected position. Please check list and try again");
                            break;
                        }
                    }
                    option = getOption();
                    break;
                case "list":
                    printTasks(data);
                    option = getOption();
                    break;
                case "exit":
                    option = "exit";
                    break;
                default:
                    System.out.println("INCORRECT INPUT!!!!!!!!");
                    option = getOption();
                    break;
            }
        }
        try {
            writeFile(filePath, data);
        } catch (IOException e) {
            System.out.println("Program has thrown an exception with message: " + e.getMessage() + ". Cannot create a file");
        }

    }

    public static String[][] readFile(Path filePath) {
        String[][] fileString = new String[0][0];
        try (Scanner scanFile = new Scanner(filePath)) {
            while (scanFile.hasNextLine()) {
                fileString = Arrays.copyOf(fileString, fileString.length + 1);
                fileString[fileString.length - 1] = scanFile.nextLine().split(", ");
            }
        } catch (FileNotFoundException e) {
            try {
                Files.createFile(filePath);
            } catch (IOException ex) {
                System.out.println("Program returned exception with message :" + ex.getMessage() + "\nPlease contact support");
            }
        } catch (IOException e) {
            System.out.println("Program returned exception with message :" + e.getMessage() + "\nPlease contact support");
        }
        return fileString;
    }

    public static String getOption() {
        Scanner userInput = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE + "Please select an option" + ConsoleColors.RESET);
        String[] options = {"add", "remove", "list", "exit"};
        for (String line : options) {
            System.out.println(line);
        }
        return userInput.nextLine();
    }

    public static String[] newTask() {
        Scanner addTaskInput = new Scanner(System.in);
        String[] newTask = new String[3];
        System.out.println("Please add task description");
        newTask[0] = addTaskInput.nextLine();
        while (true) {
            System.out.println("Please add task due date: yyyy-mm-dd");
            String date = addTaskInput.nextLine();
            String[] dateArray = date.split("-");
            if ((dateArray.length == 3)
                    && (dateArray[0].length() == 4)
                    && (dateArray[1].length() == 2)
                    && (dateArray[2].length() == 2)) {
                if ((NumberUtils.isParsable(dateArray[0])) // todo check months number 01-12 and days in months
                        && (NumberUtils.isParsable(dateArray[1]))
                        && (NumberUtils.isParsable(dateArray[2]))) {
                    newTask[1] = date;
                    break;
                } else {
                    System.out.println("Please input only digits");
                }
            } else {
                System.out.println("Wrong format. Please type in date yyyy-mm-dd");
            }
        }
        while (true) {
            System.out.println("Is your task inportant? true/false");
            String importenance = addTaskInput.nextLine();
            if ("true".equals(importenance) || "false".equals(importenance)) {
                newTask[2] = importenance;
                break;
            } else {
                System.out.println("Please type true or false");
            }
        }
        return newTask;
    }

    public static int removeTaskNumber() {
        int taskNumber = -1;
        Scanner removeTaskInput = new Scanner(System.in);
        System.out.println("Please select number to remove");
        while (true) {
            String taskToRemove = removeTaskInput.nextLine();
            try {
                taskNumber = Integer.parseInt(taskToRemove);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please type a number:");
            }
        }
        return taskNumber;
    }

    public static void printTasks(String[][] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.println(i + " : " + Arrays.toString(data[i]).replace("[", "").replace("]", ""));
        }

    }

    public static void writeFile(Path filePath, String[][] data) throws IOException {
        try {
            Files.delete(filePath);
        } catch (IOException e) {
        }
        Files.createFile(filePath);
        for (int i = 0; i < data.length; i++) {
            Files.writeString(filePath, Arrays.toString(data[i]).replace("[", "").replace("]", "") + "\n", StandardOpenOption.APPEND);
        }
        System.out.println(ConsoleColors.RED + "File written\nBye, bye." + ConsoleColors.RESET);
    }
}
