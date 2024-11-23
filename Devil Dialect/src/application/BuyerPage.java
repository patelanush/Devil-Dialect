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

	private GridPane bookGrid; // displays books in a grid
	private List<Book> cart = new ArrayList<>(); // cart to store selected books
	public double cartTotal = 0.0; // total price of items in the cart
	public Button purchaseButton; // button to display the cart total
	private LoginPage loginPage; //login page
	
	
	//builds the buyer page layout with header, sidebar, and book grid
	public Scene createContent(Stage primaryStage, LoginPage loginPage) {
		
		this.loginPage = loginPage; //saves the reference to this login page
		HBox header = createHeader(primaryStage, loginPage);

		VBox sidebar = createSidebar();

		createBookGrid();
		
		//combines all elements into the main layout
		BorderPane layout = new BorderPane();
		layout.setTop(header);
		layout.setLeft(sidebar);
		layout.setCenter(bookGrid);
		layout.setStyle("-fx-background-color: lightgray;");

		return new Scene(layout, 800, 600);
	}
	
	//getter for login page
	public LoginPage getLoginPage() {
	    return loginPage;
	}
	
	//creates the header with a logo, store name, purchase button, and sign-out button
	private HBox createHeader(Stage primaryStage, LoginPage loginPage) {

		Image logoImage = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
		ImageView logoView = new ImageView(logoImage);
		logoView.setFitWidth(50);
		logoView.setPreserveRatio(true);

		Label storeName = new Label("Devil Dialect");
		storeName.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		storeName.setTextFill(Color.WHITE);

		purchaseButton = new Button(String.format("Purchase: ($%.2f)", cartTotal));
		purchaseButton.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white;");
		purchaseButton.setOnAction(e -> {
		    CartPage cartPage = new CartPage(cart, primaryStage, this);
		    primaryStage.setScene(cartPage.createContent());
		});

		Button signOutButton = new Button("Sign Out");
		signOutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		signOutButton.setOnAction(e -> {
			loginPage.reloadLoginScene();
		});

		HBox header = new HBox(20, logoView, storeName, purchaseButton, signOutButton);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(10));
		header.setStyle("-fx-background-color: navy;");

		return header;
	}
	
	//creates the sidebar with the filtering options for categories and conditions
	private VBox createSidebar() {

		Label categoryLabel = new Label("Categories");
		ComboBox<String> categoryDropdown = new ComboBox<>();
		categoryDropdown.getItems().addAll("All Categories", "Natural Science Books", "Computer Books", "Math Books",
				"English Language Books", "Other Books");
		categoryDropdown.setValue("All Categories");

		Label conditionLabel = new Label("Conditions");
		ComboBox<String> conditionDropdown = new ComboBox<>();
		conditionDropdown.getItems().addAll("All Conditions", "Used Like New", "Moderately Used", "Heavily Used");
		conditionDropdown.setValue("All Conditions");
		
		
		// updates book grid based on selected filters
		categoryDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
			updateBookGrid(categoryDropdown.getValue(), conditionDropdown.getValue());
		});
		conditionDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
			updateBookGrid(categoryDropdown.getValue(), conditionDropdown.getValue());
		});

		VBox sidebar = new VBox(15, categoryLabel, categoryDropdown, conditionLabel, conditionDropdown);
		sidebar.setPadding(new Insets(10));
		sidebar.setStyle("-fx-background-color: lightgray;");
		sidebar.setPrefWidth(200);

		return sidebar;
	}
	
	//creates the grid that displays all books available for purchase.
	private GridPane createBookGrid() {
		bookGrid = new GridPane();
		bookGrid.setPadding(new Insets(20));
		bookGrid.setHgap(10);
		bookGrid.setVgap(10);

		updateBookGrid("All Categories", "All Conditions");

		return bookGrid;
	}
	
	
	//creates a book card for each book with its details and an "add to cart" button	
	private VBox createBookCard(Book book) {

		Label titleLabel = new Label(book.getTitle());
		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

		Label authorLabel = new Label("Author: " + book.getAuthor());

		Label yearLabel = new Label("Year: " + book.getYear());

		Label categoryLabel = new Label("Category: " + book.getCategory());

		Label conditionLabel = new Label("Condition: " + book.getCondition());

		Label priceLabel = new Label(String.format("Price: $%.2f", book.getPrice()));
		priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

		Button addToCartButton = new Button("Add to Cart");
		addToCartButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
		addToCartButton.setOnAction(e -> {

			cart.add(book);
			cartTotal += book.getPrice();

			purchaseButton.setText(String.format("Purchase: ($%.2f)", cartTotal));

			showSuccessAlert(book.getTitle());

		});

		VBox bookCard = new VBox(10, titleLabel, authorLabel, yearLabel, categoryLabel, conditionLabel, priceLabel,
				addToCartButton);
		bookCard.setPadding(new Insets(10));
		bookCard.setStyle("-fx-background-color: white; -fx-border-color: silver; -fx-border-radius: 5;");
		return bookCard;
	}
	
	
	//updates the book grid with books matching the selected category and condition.
	private void updateBookGrid(String selectedCategory, String selectedCondition) {
		bookGrid.getChildren().clear();

		List<Book> books = FileHandler.loadBooks();

		int column = 0;
		int row = 0;
		for (Book book : books) {
			boolean matchesCategory = selectedCategory.equals("All Categories")
					|| book.getCategory().equals(selectedCategory);
			boolean matchesCondition = selectedCondition.equals("All Conditions")
					|| book.getCondition().equals(selectedCondition);

			if (matchesCategory && matchesCondition) {
				VBox bookCard = createBookCard(book);
				bookGrid.add(bookCard, column, row);

				column++;
				if (column == 3) {
					column = 0;
					row++;
				}
			}
		}
	}
	
	
	// shows a success alert when a book is added to the cart.
	private void showSuccessAlert(String bookTitle) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(bookTitle + " has been added to your cart!");
		alert.showAndWait();
	}

}
