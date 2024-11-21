package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Ensure correct number of fields
                    String title = parts[0];
                    String author = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    String condition = parts[3];
                    String category = parts[4];
                    double price = Double.parseDouble(parts[5]);
                    books.add(new Book(title, author, year, condition, category, price));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books.txt: " + e.getMessage());
        }
        return books;
    }
}
