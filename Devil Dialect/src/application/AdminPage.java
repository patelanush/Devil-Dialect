package application;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

//mock up of admin view
public class AdminPage {
    public VBox createContent() {
        VBox layout = new VBox();
        layout.setSpacing(10);
        
        Text welcomeText = new Text("Welcome to the Admin Page");
        layout.getChildren().add(welcomeText);


        return layout;
    }
}
