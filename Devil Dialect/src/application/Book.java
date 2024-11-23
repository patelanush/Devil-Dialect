package application;

public class Book {
	
	//book variables
	private String title;
	private String author;
	private int year;
	private String condition;
	private String category;
	private double price;

	//constructor
	public Book(String title, String author, int year, String condition, String category, double price) {
		this.title = title;
		this.author = author;
		this.year = year;
		this.condition = condition;
		this.category = category;
		this.price = price;
	}
	
	//getters and setters
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
		return category;
	}

	public double getPrice() {
		return price;
	}
}
