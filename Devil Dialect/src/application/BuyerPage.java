package application;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BuyerPage {
    public VBox createContent() {
        VBox layout = new VBox();
        layout.setSpacing(10);
        
        Text welcomeText = new Text("Welcome to the Buyer Page");
        layout.getChildren().add(welcomeText);


        return layout;
    }
}
