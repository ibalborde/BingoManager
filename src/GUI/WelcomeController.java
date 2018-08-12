package GUI;

import Model.DBManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    public ChoiceBox listDB;

    @FXML
    public void selectDB(){
        System.out.println("Puto");

    }

    @FXML
    public void createDB(){
        listDB.getItems().add("c9os");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.listDB.getItems().addAll(DBManager.getInstance().getDatabases());

    }
}
