package application;

public class Book {
    private String title;
    private String author;
    private int year;
    private String condition;
    private String category; // New field
    private double price; // New field

    public Book(String title, String author, int year, String condition, String category, double price) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.condition = condition;
        this.category = category;
        this.price = price;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getCondition() {
        return condition;
    }

    public String getCategory() {
        return category; // Getter for category
    }

    public double getPrice() {
        return price; // Getter for price
    }
}
