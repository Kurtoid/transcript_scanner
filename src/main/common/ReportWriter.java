package main.common;

import main.common.courses.PrereqChecker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReportWriter {
    public static void saveReports(ArrayList<ParsedReport> reports) {
        try {
            HashMap<PrereqChecker.CLASSTYPES, Integer> requirements = PrereqChecker.getRequirements();
            String[] headers = new String[requirements.size() + 1];
            String[] types = new String[requirements.size()+1];
            headers[0] = "Student Name";
            int i = 1;
            for (Map.Entry<PrereqChecker.CLASSTYPES, Integer> e : requirements.entrySet()) {
                headers[i] = e.getKey().getName();
                types[i] = e.getKey().name();
                i++;
            }
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("out.csv"));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(headers));

            for (ParsedReport pr : reports) {
                String[] row = new String[headers.length];
                Set<PrereqChecker.CLASSTYPES> missingClasses = PrereqChecker.getMissingClasses(pr);

                row[0] = pr.name;
                for (int j = 1; j < headers.length; j++) {
                    if (missingClasses.contains(PrereqChecker.CLASSTYPES.valueOf(types[j]))) {
                        row[j] = "missing";
                    }else{
                        row[j] = "met";
                    }
                }
                csvPrinter.printRecord(row);
            }
            csvPrinter.flush();
        } catch (IOException ignored) {

        }
    }
}
