package application;

import java.io.IOException;

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

public class SellerPage {

	public Scene createContent(Stage primaryStage, LoginPage loginPage) {
		// Header Section
		HBox header = createHeader(primaryStage, loginPage);

		// Main Content Area (Placeholder for now)
		VBox content = createFormSection();

		// Combine Layout
		BorderPane layout = new BorderPane();
		layout.setTop(header);
		layout.setCenter(content);
		layout.setStyle("-fx-background-color: lightgray;");

		return new Scene(layout, 800, 600);
	}

	private HBox createHeader(Stage primaryStage, LoginPage loginPage) {
		// Logo
		Image logoImage = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
		ImageView logoView = new ImageView(logoImage);
		logoView.setFitWidth(50);
		logoView.setPreserveRatio(true);

		// Store Name
		Label storeName = new Label("Devil Dialect");
		storeName.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		storeName.setTextFill(Color.WHITE);

		// Sign Out Button
		Button signOutButton = new Button("Sign Out");
		signOutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		signOutButton.setOnAction(e -> {
			loginPage.loadUsers(); // Reload the user data
			primaryStage.setScene(loginPage.createContent(primaryStage)); // Redirect to login page
		});

		// Layout
		HBox header = new HBox(20, logoView, storeName, signOutButton);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(10));
		header.setStyle("-fx-background-color: navy;");

		return header;
	}

	private VBox createFormSection() {
	    // Form Title
	    Label formTitle = new Label("Please Input the Following Information About Your Book:");
	    formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	    formTitle.setTextFill(Color.BLACK);

	    // Name of Book
	    Label nameLabel = new Label("Name of Book:");
	    TextField nameField = new TextField();

	    // Author
	    Label authorLabel = new Label("Author:");
	    TextField authorField = new TextField();

	    // Year
	    Label yearLabel = new Label("Year:");
	    TextField yearField = new TextField();

	    // Category
	    Label categoryLabel = new Label("Category:");
	    ComboBox<String> categoryDropdown = new ComboBox<>();
	    categoryDropdown.getItems().addAll(
	        "Natural Science Books",
	        "Computer Books",
	        "Math Books",
	        "English Language Books",
	        "Other Books"
	    );

	    // Condition
	    Label conditionLabel = new Label("Condition:");
	    ToggleGroup conditionGroup = new ToggleGroup();
	    RadioButton likeNew = new RadioButton("Used Like New");
	    RadioButton moderatelyUsed = new RadioButton("Moderately Used");
	    RadioButton heavilyUsed = new RadioButton("Heavily Used");
	    likeNew.setToggleGroup(conditionGroup);
	    moderatelyUsed.setToggleGroup(conditionGroup);
	    heavilyUsed.setToggleGroup(conditionGroup);

	    HBox conditionBox = new HBox(10, likeNew, moderatelyUsed, heavilyUsed);
	    conditionBox.setAlignment(Pos.CENTER);

	    // Price Paid
	    Label pricePaidLabel = new Label("Original Price:");
	    TextField pricePaidField = new TextField();

	    // Estimated Selling Price
	    Label estimatedPriceLabel = new Label("System Generated Buying Price:");
	    TextField estimatedPriceField = new TextField();
	    estimatedPriceField.setEditable(false);

	    // Add listeners to update estimated price dynamically
	    categoryDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
	        updateEstimatedPrice(categoryDropdown, conditionGroup, pricePaidField, estimatedPriceField);
	    });

	    conditionGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
	        updateEstimatedPrice(categoryDropdown, conditionGroup, pricePaidField, estimatedPriceField);
	    });

	    pricePaidField.textProperty().addListener((observable, oldValue, newValue) -> {
	        updateEstimatedPrice(categoryDropdown, conditionGroup, pricePaidField, estimatedPriceField);
	    });

	    // List My Book Button
	    Button listButton = new Button("List my Book!");
	    listButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
	    listButton.setOnAction(e -> {
	        // Validate form inputs
	        String name = nameField.getText().trim();
	        String author = authorField.getText().trim();
	        String year = yearField.getText().trim();
	        String category = categoryDropdown.getValue();
	        RadioButton selectedCondition = (RadioButton) conditionGroup.getSelectedToggle();
	        String condition = (selectedCondition != null) ? selectedCondition.getText() : null;
	        String pricePaid = pricePaidField.getText().trim();
	        String estimatedPrice = estimatedPriceField.getText().trim();

	        if (name.isEmpty() || author.isEmpty() || year.isEmpty() || category == null || condition == null || pricePaid.isEmpty() || estimatedPrice.isEmpty()) {
	            // Show error alert
	            showAlert(Alert.AlertType.ERROR, "Error", "Please fill out all fields before listing the book.");
	            return;
	        }

	        // Write to books.txt
	        String bookDetails = String.format("%s,%s,%s,%s,%s,%s", name, author, year, condition, category, estimatedPrice);
	        try {
	            FileHandler.appendToBooksFile(bookDetails); // FileHandler handles appending to the file
	            showAlert(Alert.AlertType.INFORMATION, "Success", "Your book has been listed successfully!");
	        } catch (IOException ex) {
	            showAlert(Alert.AlertType.ERROR, "Error", "Failed to list your book. Please try again.");
	        }

	        // Clear the form after success
	        nameField.clear();
	        authorField.clear();
	        yearField.clear();
	        conditionGroup.getSelectedToggle().setSelected(false);
	        categoryDropdown.setValue(null);
	        pricePaidField.clear();
	        estimatedPriceField.clear();
	    });

	    // Layout
	    VBox formLayout = new VBox(10,
	        formTitle, 
	        nameLabel, nameField, 
	        authorLabel, authorField, 
	        yearLabel, yearField, 
	        categoryLabel, categoryDropdown, 
	        conditionLabel, conditionBox, 
	        pricePaidLabel, pricePaidField, 
	        estimatedPriceLabel, estimatedPriceField, 
	        listButton
	    );
	    formLayout.setAlignment(Pos.CENTER);
	    formLayout.setPadding(new Insets(20));
	    formLayout.setStyle("-fx-background-color: lightgray;");
	    return formLayout;
	}

	private void updateEstimatedPrice(ComboBox<String> categoryDropdown, ToggleGroup conditionGroup,
			TextField pricePaidField, TextField estimatedPriceField) {
		// Get the original price
		double originalPrice;
		try {
			originalPrice = Double.parseDouble(pricePaidField.getText());
		} catch (NumberFormatException e) {
			estimatedPriceField.setText("0.0");
			return;
		}

		// Get the condition multiplier
		double conditionMultiplier = 0.0;
		RadioButton selectedCondition = (RadioButton) conditionGroup.getSelectedToggle();
		if (selectedCondition != null) {
			switch (selectedCondition.getText()) {
			case "Used Like New":
				conditionMultiplier = 0.85;
				break;
			case "Moderately Used":
				conditionMultiplier = 0.75;
				break;
			case "Heavily Used":
				conditionMultiplier = 0.6;
				break;
			}
		}

		// Get the category multiplier
		double categoryMultiplier = 0.0;
		String selectedCategory = categoryDropdown.getValue();
		if (selectedCategory != null) {
			switch (selectedCategory) {
			case "Natural Science Books":
				categoryMultiplier = 0.9;
				break;
			case "Computer Books":
				categoryMultiplier = 1;
				break;
			case "Math Books":
				categoryMultiplier = 0.95;
				break;
			case "English Language Books":
				categoryMultiplier = 0.85;
				break;
			case "Other Books":
				categoryMultiplier = 0.8;
				break;
			}
		}

		// Calculate the estimated price
		if (conditionMultiplier > 0 && categoryMultiplier > 0) {
			double estimatedPrice = originalPrice * conditionMultiplier * categoryMultiplier;
			estimatedPriceField.setText(String.format("%.2f", estimatedPrice)); // Format to 2 decimal places
		} else {
			estimatedPriceField.setText("0.0");
		}
	}
	
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
