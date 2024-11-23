package application;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartPage {

    private List<Book> cart; // Reference to the cart
    private Stage primaryStage; // Reference to the primary stage
    private BuyerPage buyerPage; // Reference to the BuyerPage to go back

    public CartPage(List<Book> cart, Stage primaryStage, BuyerPage buyerPage) {
        this.cart = cart;
        this.primaryStage = primaryStage;
        this.buyerPage = buyerPage;
    }

    public Scene createContent() {
        // Title
        Label cartTitle = new Label("Your Cart");
        cartTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Cart items
        VBox cartItemsBox = new VBox(10);
        cartItemsBox.setPadding(new Insets(10));
        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty!");
            cartItemsBox.getChildren().add(emptyLabel);
        } else {
            for (Book book : cart) {
                Label bookInfo = new Label(book.getTitle() + " - $" + String.format("%.2f", book.getPrice()));
                cartItemsBox.getChildren().add(bookInfo);
            }
        }
        
        double cartTotal = 0.0;
        for (Book book : cart) {
            cartTotal += book.getPrice();
        }

        // Display Cart Total
        Label totalLabel = new Label("Total: $" + String.format("%.2f", cartTotal));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Confirm Purchase Button
        Button confirmButton = new Button("Confirm Purchase");
        confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> {
            if (cart.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cart is Empty", "You cannot place an order with an empty cart.");
                return;
            }

            // Clear the cart and reset the total
            cart.clear();
            buyerPage.cartTotal = 0.0;
            buyerPage.purchaseButton.setText("Purchase: ($0.00)");

            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

            // Redirect back to the BuyerPage
            primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage()));
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            // Redirect back to the BuyerPage
            primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage()));
        });

        // Layout
        VBox layout = new VBox(20, cartTitle, cartItemsBox, totalLabel, confirmButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: lightgray;");

        return new Scene(layout, 800, 600);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
