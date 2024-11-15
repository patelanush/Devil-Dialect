package application;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SellerPage {
    public VBox createContent() {
        VBox layout = new VBox();
        layout.setSpacing(10);
        
        Text welcomeText = new Text("Welcome to the Seller Page");
        layout.getChildren().add(welcomeText);


        return layout;
    }
}
