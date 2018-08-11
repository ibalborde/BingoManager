package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class WelcomeController {
    public ChoiceBox listDB;

    @FXML
    public void selectDB(){
        System.out.println("Puto");

    }

    @FXML
    public void createDB(){
        listDB.getItems().add("c9os");

    }


}
