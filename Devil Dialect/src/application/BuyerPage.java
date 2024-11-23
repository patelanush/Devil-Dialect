package application;

import java.util.ArrayList;
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
	private List<Book> cart = new ArrayList<>(); // Cart to hold added books
	public double cartTotal = 0.0; // Total price of the cart
	public Button purchaseButton; // Cart button to update the price dynamically
	public Scene createContent(Stage primaryStage, LoginPage loginPage) {
	    // Header Section
	    HBox header = createHeader(primaryStage, loginPage);

	    // Sidebar Section
	    VBox sidebar = createSidebar();

	    // Main Content Area
	    createBookGrid(); // Initialize the book grid

	    // Combine Layout
	    BorderPane layout = new BorderPane();
	    layout.setTop(header);
	    layout.setLeft(sidebar);
	    layout.setCenter(bookGrid); // Use the initialized bookGrid here
	    layout.setStyle("-fx-background-color: lightgray;");

	    return new Scene(layout, 800, 600);
	}

	private HBox createHeader(Stage primaryStage, LoginPage loginPage) {
	    // Company Logo
	    Image logoImage = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
	    ImageView logoView = new ImageView(logoImage);
	    logoView.setFitWidth(50);
	    logoView.setPreserveRatio(true);

	    // Store Name
	    Label storeName = new Label("Devil Dialect");
	    storeName.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    storeName.setTextFill(Color.WHITE);

	    // Cart Button
	    purchaseButton = new Button(String.format("Purchase: ($%.2f)", cartTotal)); // Use current cart total
	    purchaseButton.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white;");
	    purchaseButton.setOnAction(e -> {
	        // Navigate to the CartPage
	        CartPage cartPage = new CartPage(cart, primaryStage, this);
	        primaryStage.setScene(cartPage.createContent());
	    });

	    // Sign Out Button
	    Button signOutButton = new Button("Sign Out");
	    signOutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
	    signOutButton.setOnAction(e -> {
	        loginPage.reloadLoginScene(); // Redirect back to the login page
	    });

	    // Layout
	    HBox header = new HBox(20, logoView, storeName, purchaseButton, signOutButton);
	    header.setAlignment(Pos.CENTER);
	    header.setPadding(new Insets(10));
	    header.setStyle("-fx-background-color: navy;");

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
        sidebar.setStyle("-fx-background-color: lightgray;");
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

        // Category
        Label categoryLabel = new Label("Category: " + book.getCategory());

        // Condition
        Label conditionLabel = new Label("Condition: " + book.getCondition());

        // Price
        Label priceLabel = new Label(String.format("Price: $%.2f", book.getPrice()));
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        // Add to Cart Button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> {
            // Add the book to the cart
            cart.add(book);
            cartTotal += book.getPrice();

            // Update the cart button text
            purchaseButton.setText(String.format("Purchase: ($%.2f)", cartTotal));

            // Show success alert
            showSuccessAlert(book.getTitle());

        });

        // Arrange elements in a vertical box
        VBox bookCard = new VBox(10, titleLabel, authorLabel, yearLabel, categoryLabel, conditionLabel, priceLabel, addToCartButton);
        bookCard.setPadding(new Insets(10));
        bookCard.setStyle("-fx-background-color: white; -fx-border-color: silver; -fx-border-radius: 5;");
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
                VBox bookCard = createBookCard(book); 
                bookGrid.add(bookCard, column, row); 

                column++;
                if (column == 3) { // Move to the next row after 3 columns
                    column = 0;
                    row++;
                }
            }
        }
    }
    
    private void showSuccessAlert(String bookTitle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(bookTitle + " has been added to your cart!");
        alert.showAndWait();
    }
    
    
}
