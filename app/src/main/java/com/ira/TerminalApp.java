package com.ira;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TerminalApp {
    public static void main(String[] args) {
        TerminalApp program = new TerminalApp();
        program.start();

    }

    private final Scanner scanner = new Scanner(System.in);
    private final RandomTable randomTable;

    public TerminalApp() {
        RandomTable loadedTable = load();

        if (loadedTable != null) {
            randomTable = loadedTable;
            System.out.println();
        } else {
            int numberOfRows;
            int numberOfColumns;
            while (true) {
                try {
                    numberOfRows = askPositiveInt("Enter Number of Rows: ");
                    numberOfColumns = askPositiveInt("Enter Number of Columns: ");
                    break;
                } catch (ActionCancelledException exception) {
                    System.out.println("\nCannot Go To Menu Yet\n");
                }
            }
            randomTable = new RandomTable();
            RandomTableOperations.initializeRandomTable(randomTable, numberOfRows, numberOfColumns);
            save();
            printDivider();
        }
    }

    public void start() {
        while (true) {
            try {
                menu();
            } catch (UserExitException exception) {
                System.out.println("Bye");
                return;
            }
        }
    }

    private void menu() throws UserExitException {
        String menuOption;
        try {
            menuOption = askMenu();
        } catch (ActionCancelledException exception) {
            System.out.println("\nAlready In Menu\n");
            return;
        }
        printDivider();

        try {
            switch (menuOption) {
                case "SEARCH":
                    String searchResult = askString("Enter String To Search: ");
                    System.out.println(RandomTableOperations.print(randomTable));
                    System.out.println("\n" + RandomTableOperations.search(randomTable, searchResult));
                    break;
                case "EDIT":
                    String newKey = askString("Enter Replacement Key: ");
                    String newValue = askString("Enter Replacement Value: ");
                    Map<Axis, Integer> coordinates = askCoordinates();
                    RandomTableOperations.edit(randomTable,
                            coordinates.get(Axis.Y),
                            coordinates.get(Axis.X),
                            Collections.singletonMap(newKey, newValue));
                    break;
                case "PRINT":
                    System.out.println(RandomTableOperations.print(randomTable));
                    break;
                case "RESET":
                    int numberOfRows = askPositiveInt("Enter Number of Rows: ");
                    int numberOfColumns = askPositiveInt("Enter Number of Columns: ");
                    RandomTableOperations.reset(randomTable, numberOfRows, numberOfColumns);
                    break;
                case "ADD COLUMN":
                    RandomTableOperations.addColumn(randomTable, askPositiveInt("Enter Row Span: "));
                    break;
                case "ADD ROW":
                    RandomTableOperations.addRow(randomTable, askPositiveInt("Enter Column Span: "));
                    break;
                case "SORT ROW":
                    RandomTableOperations.sortEachRow(randomTable);
                    System.out.println(RandomTableOperations.print(randomTable));
                    break;
                case "EXIT":
                    throw new UserExitException();
                default:
                    System.out.println("Wrong Menu Option");
            }
        } catch (ActionCancelledException exception) {
            System.out.println("\nUser Cancelled Action");
            System.out.println("Returning To Menu");
        }
        save();
        printDivider();
    }

    private String askMenu() throws ActionCancelledException {
        System.out.println("SEARCH");
        System.out.println("EDIT");
        System.out.println("PRINT");
        System.out.println("RESET");
        System.out.println("ADD COLUMN");
        System.out.println("ADD ROW");
        System.out.println("SORT ROW");
        System.out.println("EXIT");
        System.out.println();
        return askString("Enter Choice: ").toUpperCase();
    }

    private Map<Axis, Integer> askCoordinates() throws ActionCancelledException {
        int rowMax = randomTable.getRowLength();
        int[] columnMaxes = randomTable.getColumnLengths();

        while (true) {
            int xCoordinate = askNonNegativeInt("Enter X: ");
            int yCoordinate = askNonNegativeInt("Enter Y: ");

            if (yCoordinate < rowMax && xCoordinate < columnMaxes[yCoordinate]) {
                HashMap<Axis, Integer> coordinates = new HashMap<>();
                coordinates.put(Axis.X, xCoordinate);
                coordinates.put(Axis.Y, yCoordinate);
                return coordinates;
            } else {
                System.out.println("\nIndex Out Of Range\n");
            }
        }
    }

    private int askNonNegativeInt(String prompt) throws ActionCancelledException {
        while (true) {
            int intInput = askInt(prompt);
            if (intInput < 0) System.out.println("\nOnly Non-Negative Values Are Allowed\n");
            else return intInput;
        }
    }

    private int askPositiveInt(String prompt) throws ActionCancelledException {
        while (true) {
            int intInput = askInt(prompt);
            if (intInput <= 0) System.out.println("\nOnly Positive Values Are Allowed\n");
            else return intInput;
        }
    }

    private int askInt(String prompt) throws ActionCancelledException {
        while (true) {
            String stringInput = askString(prompt);
            try {
                return Integer.parseInt(stringInput);
            } catch (NumberFormatException exception) {
                System.out.println("\nWrong Type\n");
            }
        }
    }

    private String askString(String prompt) throws ActionCancelledException {
        System.out.print(prompt);
        String inputString = scanner.nextLine().trim();
        if (inputString.equalsIgnoreCase("MENU")) throw new ActionCancelledException();
        else return inputString;
    }

    private void printDivider() {
        System.out.println("\n========================================\n");
    }

    private void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("data");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(randomTable);
        } catch (FileNotFoundException e) {
            System.out.println("\nFailed To Create Save File\n");
        } catch (IOException e) {
            System.out.println("\nFailed To Write Table To Save File");
            e.printStackTrace();
        }
    }

    private RandomTable load() {
        RandomTable loadedTable = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loadedTable = (RandomTable) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("\nFailed To Load Save File");
            System.out.println("Creating New...\n");
        } catch (IOException e) {
            System.out.println("\nFailed To Read Table In Save File\n");
        } catch (ClassNotFoundException e) {
            System.out.println("\nCorrupted Save File\n");
        }
        return loadedTable;
    }
}