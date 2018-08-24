package GUI;

import Model.Database.DBManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    private DBManager dbManager;

    public ChoiceBox listDB;

    @FXML
    public void selectDB(){
        System.out.println("Puto");

    }

    @FXML
    public void createDB(){
      String newName = null;
      do {
          newName = UIHelper.getUserTextInput("Crear Base de Datos","Ingrese el nombre de la nueva base de datos","Nueva base de datos:");
      } while (newName == null || dbManager.createDB(newName));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbManager = DBManager.getInstance();
        this.listDB.setItems(dbManager.getDatabasesNames());
        this.listDB.valueProperty().bindBidirectional(dbManager.lastSetDatabaseNameProperty());
    }

}
