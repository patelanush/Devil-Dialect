package application;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartPage {

	private List<Book> cart; //cart	
	private Stage primaryStage; //primarystage
	private BuyerPage buyerPage; //buyerpage so you can go back

	public CartPage(List<Book> cart, Stage primaryStage, BuyerPage buyerPage) {
		this.cart = cart;
		this.primaryStage = primaryStage;
		this.buyerPage = buyerPage;
	}
	
	//creates the layout and content for the cart page
	public Scene createContent() {
	    //logo
	    Image logoImage = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
	    ImageView logoView = new ImageView(logoImage);
	    logoView.setFitWidth(200);
	    logoView.setPreserveRatio(true);

	    //title
	    Label cartTitle = new Label("Your Cart");
	    cartTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

	    //cart items
	    VBox cartItemsBox = new VBox(10);
	    cartItemsBox.setPadding(new Insets(10));
	    cartItemsBox.setAlignment(Pos.CENTER);
	    if (cart.isEmpty()) {
	        Label emptyLabel = new Label("Your cart is empty!");
	        cartItemsBox.getChildren().add(emptyLabel);
	    } else {
	    	//displays all books in the cart (title and price)
	        for (Book book : cart) {
	            Label bookInfo = new Label(book.getTitle() + " - $" + String.format("%.2f", book.getPrice()));
	            bookInfo.setStyle("-fx-font-size: 14px; -fx-font-weight: normal;"); 
	            cartItemsBox.getChildren().add(bookInfo);
	        }
	    }
	    
	    // calculate and display the cart total
	    double cartTotal = 0.0;
	    for (Book book : cart) {
	        cartTotal += book.getPrice();
	    }

	   
	    Label totalLabel = new Label("Total: $" + String.format("%.2f", cartTotal));
	    totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    // confirm Purchase Button
	    Button confirmButton = new Button("Confirm Purchase");
	    confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
	    confirmButton.setOnAction(e -> {
	        if (cart.isEmpty()) {
	        	// alert when attempting to confirm an empty cart
	            showAlert(Alert.AlertType.WARNING, "Cart is Empty", "You cannot place an order with an empty cart.");
	            return;
	        }

	        //clear cart and reset total
	        cart.clear();
	        buyerPage.cartTotal = 0.0;
	        buyerPage.purchaseButton.setText("Purchase: ($0.00)");

	        //success alert
	        showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

	        //back to buyer page
	        primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage()));
	    });

	   //back button
	    Button backButton = new Button("Back");
	    backButton.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white;");
	    backButton.setOnAction(e -> {
	        
	        primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage()));
	    });

	    //cart page layout
	    VBox layout = new VBox(20, logoView, cartTitle, cartItemsBox, totalLabel, confirmButton, backButton);
	    layout.setAlignment(Pos.CENTER);
	    layout.setPadding(new Insets(20));
	    layout.setStyle("-fx-background-color: lightgray;");

	    return new Scene(layout, 800, 600);
	}

	//alert
	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
