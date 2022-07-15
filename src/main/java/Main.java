import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // return old or new list
        // prompt the user and run methods based on their choice
        restoreList();
        System.out.println("Printing people from main: " + PeopleSingleton.getInstance().getPeople());
        saveListAsJSON(PeopleSingleton.getInstance().getPeople());
    }
    static void saveListAsJSON(List<Person> personList) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(personList);

        mapper.writeValue(new File("people.json"), personList);
        System.out.println("This is our Json output: " + json);
    }
    public static List<Person> readJson() throws JsonProcessingException {
        try {
            return Arrays.asList(new ObjectMapper().readValue(new File("people.json"), Person[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void restoreList() throws IOException {
        String fileName = null;
        Input sc = Input.getInstance();
                // ask the user if they want to restore list of people
        int input = sc.getInt("Would you like to restore a list of people? If yes type 1, any other number will be interpreted as no: ");
        if (input == 1) {
            // yes? prompt for file name and try to restore from old file
            sc.getString("");
           int fileToRestore = sc.getInt("Would you like to try to restore from 1. json or from 2. csv?");
           if (fileToRestore == 1){
               fileName = "people.json";
           } else if (fileToRestore == 2) {
               fileName = "people.csv";
               readJson();
               return;
           } else {
               System.out.println("Must enter 1 or 2. Let's try again.");
               restoreList();
           }
            boolean exists = Files.isReadable(Path.of(fileName));
            try {
                if (exists) {
                    // read from file and make a list from people
                    System.out.println("We found the file and we are reading it!");
                    readFromFile("people.csv");
                    mainMenuPrompt();
                } else {
                    // no ? start brand-new list
                    System.out.println("This file was not found start a new list.");
                    mainMenuPrompt();

                  }
            } catch (Exception e) {
                System.out.println("Looks like theres something wrong: " + e);
                System.out.println("Lets create a new file.");
                mainMenuPrompt();
            }
        }
            System.out.println("running menu from outside try catch");
            mainMenuPrompt();
    }
    private static void mainMenuPrompt() throws IOException {
        // give user 3 options : add person to list, print a list, exit the program
        Input sc = Input.getInstance();
        int optionChosen = sc.getInt("Would you like to: \n 1. Add a person \n 2. Print a list of current people \n 3. Exit Program \n Enter the number to the left of the option: " );

        switch (optionChosen) {
            case 1:
                addPerson();
                break;
            case 2:
                printList();
                break;
            case 3:
                exitProgram();
                break;
        }
    };

    private static void addPerson() throws IOException {
        // add: ask for the person info, add new Person to list, return user to the 3 options before
        Input sc = Input.getInstance();

        // for some reason the first input gets skipped over
        sc.getString("");
        String firstName = sc.getString("What is the persons first name?");
        String lastName = sc.getString("What is the persons last name?");
        int birthDay = sc.getInt("What is the persons birth day? (number only)");
        int birthMonth = sc.getInt("What is the persons birth month? (number only)");
        int birthYear = sc.getInt("What is the persons birth year? (number only)");

        Person person = new Person(firstName, lastName, birthYear, birthMonth, birthDay);

        PeopleSingleton.getInstance().addPerson(person);
        mainMenuPrompt();
    }

    private static void printList() throws IOException {
        // print a list: print the person info for each person on current list, return user to the 3 options
        System.out.println(PeopleSingleton.getInstance().getPeople());
        mainMenuPrompt();
    }
    private static void exitProgram() throws IOException {
        // exit the program: save all the persons on list to my file
        StringBuilder CSV = new StringBuilder();
        // PeopleSingleton people = PeopleSingleton.getInstance();
        // List peopleList = people.getPeople();

        // for some reason this doesn't like when I pass in peopleList
        for (Person person : PeopleSingleton.getInstance().getPeople()) {
            System.out.println("Before formatting person: " + person);
            String personCSV = person.formatAsCSV();
            System.out.println("After formatting person: " + personCSV);
            CSV.append(personCSV + "\n");
            writeToFile("people.csv", String.valueOf(CSV));
        }
    }

    static String readFromFile(String fileName) throws IOException {
        String returnString = "";
        Scanner fileReader = null;
        try {
            File myFile = new File(fileName);
            fileReader = new Scanner(myFile);
            while (fileReader.hasNextLine()) {
                returnString += fileReader.nextLine();
                // parse string
                String[] info = returnString.split(",");
                // create student from parsed string
                String firstName = info[0];
                String lastName= info[1];
                int birthYear= Integer.parseInt(info[2]);
                int birthMonth= Integer.parseInt(info[3]);
                int birthDay= Integer.parseInt(info[4]);
                Person person = new Person(firstName, lastName, birthYear, birthMonth, birthDay);
                // add person to list
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileReader != null)
                fileReader.close();
        }
        return returnString;
    }

    static void writeToFile(String fileName, String text) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(text);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
