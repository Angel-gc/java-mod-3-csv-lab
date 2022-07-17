import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    static List<String> readFromFile(String fileName) throws IOException {
        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());
        }
        return result;
    }

    static void writeToFile(String fileName, String text) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(text);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public static void appendToFile(String fileName, StringBuilder values) {
        System.out.println(fileName);
        try(FileWriter fw = new FileWriter(fileName,true)){
            fw.append(values);
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
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
}
