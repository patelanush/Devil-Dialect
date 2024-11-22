package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginPage extends Application {

    private Map<String, String[]> users = new HashMap<>();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Devil Dialect");

        loadUsers();

        Scene loginScene = createLoginScene();
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public Scene createLoginScene() {
        Text title = new Text("Devil Dialect");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.RED);
        
        Image logo = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);  
        logoView.setPreserveRatio(true);

        TextField asuIdInput = new TextField();
        asuIdInput.setPromptText("ASU ID");
        asuIdInput.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        Button loginButton = new Button("Log in");
        loginButton.setPrefWidth(200);
        loginButton.setStyle("-fx-background-color: silver; -fx-text-fill: black;");
        loginButton.setOnAction(e -> handleLogin(asuIdInput.getText(), passwordInput.getText()));

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: indigo;");
        layout.getChildren().addAll(title, logoView, asuIdInput, passwordInput, loginButton);

        return new Scene(layout, 400, 500);
    }
    
    public Scene createContent(Stage primaryStage) {
        Text title = new Text("Devil Dialect");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.RED);

        Image logo = new Image(getClass().getResourceAsStream("/images/devil.jpg"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        TextField asuIdInput = new TextField();
        asuIdInput.setPromptText("ASU ID");
        asuIdInput.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        Button loginButton = new Button("Log in");
        loginButton.setPrefWidth(200);
        loginButton.setStyle("-fx-background-color: silver; -fx-text-fill: black;");
        loginButton.setOnAction(e -> handleLogin(asuIdInput.getText(), passwordInput.getText()));

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: indigo;");
        layout.getChildren().addAll(title, logoView, asuIdInput, passwordInput, loginButton);

        return new Scene(layout, 400, 500);
    }

    public void loadUsers() {
        if (users.isEmpty()) { // Load only if users are not already loaded
            try (BufferedReader reader = new BufferedReader(new FileReader("src/users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String asuId = parts[0];
                        String password = parts[1];
                        String role = parts[2];
                        users.put(asuId, new String[]{password, role});
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading users: " + e.getMessage());
            }
        }
    }

    private void handleLogin(String asuId, String password) {
        if (users.containsKey(asuId)) {
            String[] credentials = users.get(asuId);
            String correctPassword = credentials[0];
            String role = credentials[1];
            
            if (correctPassword.equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + asuId + "!");
                redirectToRolePage(role);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect or invalid ASU ID or password.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect or invalid ASU ID or password.");
        }
    }

    private void redirectToRolePage(String role) {
        Scene newScene = null;
        switch (role) {
            case "buyer":
                BuyerPage buyerPage = new BuyerPage();
                newScene = buyerPage.createContent(primaryStage, this); // Pass primaryStage and this instance
                break;
            case "seller":
                SellerPage sellerPage = new SellerPage();
                newScene = sellerPage.createContent(primaryStage, this); // Pass primaryStage and this instance
                break;
            case "admin":
                AdminPage adminPage = new AdminPage();
                newScene = new Scene(adminPage.createContent(), 400, 500);
                break;
            default:
                System.out.println("Unknown role: " + role);
                return;
        }
        primaryStage.setScene(newScene);
    }
    
    public void reloadLoginScene() {
        primaryStage.setScene(createLoginScene());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
