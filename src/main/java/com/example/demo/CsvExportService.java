package com.example.demo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class CsvExportService {

    private final UserRepository userRepository;

    public CsvExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void writeUsersToCsv(Writer writer) {

        List<User> users = userRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (User user : users) {
                csvPrinter.printRecord(user.getID(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getPhoto());
            }
        } catch (IOException e) {
            System.out.println("Error While writing CSV " + e.toString());
        }
    }
}
