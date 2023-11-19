package org.zenith.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class FileUtils {
    public static void writeToFile(String path, String content) {
        try {
            File file = new File(path);

            if (!file.isDirectory()) {
                FileWriter writer = new FileWriter(path);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void createFile(String path, String content) {
        try {
            File file = new File(path);

            if (file.createNewFile()) {
                FileUtils.writeToFile(path, content);
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void readFile(String path) {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                builder.append(data).append(System.lineSeparator());
            }

            System.out.println(builder.toString());

            scanner.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);

        if (file.delete()) {
            System.out.printf("Successfully deleted: %s%n", file.getName());
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    public static void listFiles(String path) {
        File dir = new File(path);

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }
}
