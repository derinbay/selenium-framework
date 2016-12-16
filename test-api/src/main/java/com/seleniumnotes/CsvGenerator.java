package com.seleniumnotes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Taylan on 1.06.2016.
 */
public class CsvGenerator {

    public static void generateCsvFile(List<Object> objects, String... fields) {
        try {
            String subProjectDirectory = System.getProperties().get("user.dir").toString();
            String projectDirectory = subProjectDirectory.substring(0, subProjectDirectory.indexOf("selenium-framework"));
            FileWriter writer = new FileWriter(projectDirectory + "report.csv");

            writer.append("Field 1,");

            for (Object object : objects) {
                for (String field : fields) {
                    writer.append(field);
                    writer.append(",");
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
