package application;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BuyerPage {
	
	private GridPane bookGrid;
	public Scene createContent(Stage primaryStage) {
	    // Header Section
	    HBox header = createHeader();

	    // Sidebar Section
	    VBox sidebar = createSidebar();

	    // Main Content Area
	    createBookGrid(); // Initialize the book grid

	    // Combine Layout
	    BorderPane layout = new BorderPane();
	    layout.setTop(header);
	    layout.setLeft(sidebar);
	    layout.setCenter(bookGrid); // Use the initialized bookGrid here
	    layout.setStyle("-fx-background-color: #f4f4f4;");

	    return new Scene(layout, 800, 600);
	}

    private HBox createHeader() {
        // Company Logo
        Image logoImage = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(50);
        logoView.setPreserveRatio(true);

        // Store Name
        Label storeName = new Label("Devil Dialect");
        storeName.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        storeName.setTextFill(Color.WHITE);

        // Search Bar
    

        // Cart Button
        Button cartButton = new Button("Cart ($0.00)");
        cartButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");

        // Sign Out Button
        Button signOutButton = new Button("Sign Out");
        signOutButton.setStyle("-fx-background-color: #FF4136; -fx-text-fill: white;");

        // Layout
        HBox header = new HBox(20, logoView, storeName, cartButton, signOutButton);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #002366;");

        return header;
    }

    private VBox createSidebar() {
        // Categories Dropdown
        Label categoryLabel = new Label("Categories");
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll(
            "All Categories", 
            "Natural Science Books", 
            "Computer Books", 
            "Math Books", 
            "English Language Books", 
            "Other Books"
        );
        categoryDropdown.setValue("All Categories"); // Default selection

        // Conditions Dropdown
        Label conditionLabel = new Label("Conditions");
        ComboBox<String> conditionDropdown = new ComboBox<>();
        conditionDropdown.getItems().addAll(
            "All Conditions", 
            "Used Like New", 
            "Moderately Used", 
            "Heavily Used"
        );
        conditionDropdown.setValue("All Conditions"); // Default selection

        // Event listeners for filtering
        categoryDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateBookGrid(categoryDropdown.getValue(), conditionDropdown.getValue());
        });
        conditionDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateBookGrid(categoryDropdown.getValue(), conditionDropdown.getValue());
        });

        // Layout
        VBox sidebar = new VBox(15, categoryLabel, categoryDropdown, conditionLabel, conditionDropdown);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #E8E8E8;");
        sidebar.setPrefWidth(200);

        return sidebar;
    }


    private GridPane createBookGrid() {
        bookGrid = new GridPane(); // Initialize the class-level variable
        bookGrid.setPadding(new Insets(20));
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);

        // Initially display all books
        updateBookGrid("All Categories", "All Conditions");

        return bookGrid;
    }


    private VBox createBookCard(Book book) {
        // Book Title
        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Author
        Label authorLabel = new Label("Author: " + book.getAuthor());

        // Year
        Label yearLabel = new Label("Year: " + book.getYear());

        // Condition
        Label conditionLabel = new Label("Condition: " + book.getCondition());

        // Add to Cart Button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> {
            System.out.println(book.getTitle() + " added to cart.");
        });

        // Arrange elements in a vertical box
        VBox bookCard = new VBox(10, titleLabel, authorLabel, yearLabel, conditionLabel, addToCartButton);
        bookCard.setPadding(new Insets(10));
        bookCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");
        return bookCard;
    }
    
    private void updateBookGrid(String selectedCategory, String selectedCondition) {
        // Clear the current book grid
        bookGrid.getChildren().clear();

        // Load books from the file
        List<Book> books = FileHandler.loadBooks();

        // Iterate through the books and filter by the selected category and condition
        int column = 0;
        int row = 0;
        for (Book book : books) {
            boolean matchesCategory = selectedCategory.equals("All Categories") || book.getCategory().equals(selectedCategory);
            boolean matchesCondition = selectedCondition.equals("All Conditions") || book.getCondition().equals(selectedCondition);

            if (matchesCategory && matchesCondition) {
                VBox bookCard = createBookCard(book); // Create a card for the book
                bookGrid.add(bookCard, column, row); // Add card to the grid

                column++;
                if (column == 3) { // Move to the next row after 3 columns
                    column = 0;
                    row++;
                }
            }
        }
    }
    
    
}
