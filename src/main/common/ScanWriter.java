package main.common;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ScanWriter {

    public static void writeAll(ArrayList<ParsedReport> reports) throws IOException {
        Writer writer = Files.newBufferedWriter(Paths.get("students.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Student Name", "GPA"));
        //Writing records in the generated CSV file
        for(ParsedReport rep : reports){
            csvPrinter.printRecord("name", rep.GPA);
        }
        csvPrinter.flush();

    }
}
