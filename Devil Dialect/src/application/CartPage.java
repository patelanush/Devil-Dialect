package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class CartPage {

    private List<Book> cart;
    private Stage primaryStage;
    private BuyerPage buyerPage;

    public CartPage(List<Book> cart, Stage primaryStage, BuyerPage buyerPage) {
        this.cart = cart;
        this.primaryStage = primaryStage;
        this.buyerPage = buyerPage;
    }

    public Scene createContent() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: lightgray;");
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Your Cart");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        layout.getChildren().add(title);

        // Display cart items
        for (Book book : cart) {
            Label bookLabel = new Label(book.getTitle() + " - $" + String.format("%.2f", book.getPrice()));
            layout.getChildren().add(bookLabel);
        }

        // Total price
        double total = cart.stream().mapToDouble(Book::getPrice).sum();
        Label totalLabel = new Label("Total: $" + String.format("%.2f", total));
        totalLabel.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(totalLabel);

        // Confirm Purchase Button
        Button confirmButton = new Button("Confirm Purchase");
        confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed!");
            cart.clear(); // Clear the cart after purchase
            primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage())); // Navigate back to BuyerPage
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        backButton.setOnAction(e -> primaryStage.setScene(buyerPage.createContent(primaryStage, new LoginPage())));

        HBox buttonBox = new HBox(10, backButton, confirmButton);
        buttonBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(buttonBox);

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
