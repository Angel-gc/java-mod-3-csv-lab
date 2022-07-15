import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Person> personList = restoreList();
        // run method based on whatever option the user chose
        choice(personList);
    }

    private static void choice(List<Person> people) throws IOException {
        // give user 3 options : add person to list, print a list, exit the program
        Input sc = Input.getInstance();
        String optionChosen = sc.getString("Would you like to: \n 1. Add a person: add \n 2.Print a list of current people: print \n 3. Exit Program: exit");
        switch (optionChosen) {
            case "add":
                addPerson(people);
                break;
            case "print":
                printList(people);
                break;
            case "exit":
                exitProgram(people);
                break;
        }
    }
    private static List<Person> restoreList() throws IOException {
        List<Person> priorPeopleList = new ArrayList<Person>();
        Input sc = Input.getInstance();
        try {
                // ask the user if they want to restore list of people
                String input = sc.getString("Would you like to restore a list of people? Type yes, anything other than yes will be interpreted as no: ");
                if (input.equals("yes")) {
                    // yes? prompt for file name and try to restore from old file
                    String fileName = sc.getString("Do you want to read from an old file?");
                    File file = new File(fileName);
                    if (file.exists()) {
                        // read from file and make a list from people
                        String personCSV = readFromFile(fileName, false);
                        Person readFile = new Person(personCSV);
                        return priorPeopleList;
                    } else {
                        // no ? start brand-new list
                       List<Person> newListOfPeople = new ArrayList<Person>();
                       return newListOfPeople;
                }
            }
        } finally {

            }
        return priorPeopleList;
    }

    private static void addPerson(List<Person> people){
        // add: ask for the person info, add new Person to list, return user to the 3 options before
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the persons first name?");
        String firstName = scanner.nextLine();
        System.out.println("What is the persons last name?");
        String lastName = scanner.nextLine();
        System.out.println("What is the persons birth day?");
        int birthDay = scanner.nextInt();
        System.out.println("What is the persons birth month?");
        int birthMonth = scanner.nextInt();
        System.out.println("What is the persons birth year?");
        int birthYear = scanner.nextInt();

        Person person = new Person(firstName, lastName, birthYear, birthMonth, birthDay);

        people.add(person);
    }

    private static void printList(List<Person> people) {
        // print a list: print the person info for each person on current list, return user to the 3 options
        for (Person person : people) {
            System.out.println("Current person info: " + person);
        }
    }
    private static void exitProgram(List<Person> people) throws IOException {
        // exit the program: save all the persons on list to my file
        for (Person person : people) {
            String personCSV = person.formatAsCSV();
            writeToFile("person.csv", personCSV);
        }
        System.exit(0);
    }


    static String readFromFile(String fileName, boolean addNewLine) throws IOException {
        String returnString = "";
        Scanner fileReader = null;
        try {
            File myFile = new File(fileName);
            fileReader = new Scanner(myFile);
            while (fileReader.hasNextLine()) {
                returnString += fileReader.nextLine();
                if (addNewLine)
                    returnString += "\n";
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
