package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
	public static List<Book> loadBooks() {
		//loads books from "books.txt" and returns a list of book objects
		List<Book> books = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("src/books.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 6) { // makes sure all the fields exist
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
			System.out.println("Error: " + e.getMessage());
		}
		return books;
	}
	
	
	//appends a book to "books.txt"
	public static void appendToBooksFile(String bookDetails) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/books.txt", true))) {
			writer.write(bookDetails);
			writer.newLine(); // adds a new line for the next entry
		}
	}
}
