package GUI.ClientDetailView;

import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Database.DBManager;
import Model.Database.Database;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientDetailView implements Initializable {

    // MARK: - UI Components

    @FXML private TextField nameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField dniTextField;
    @FXML private TextField telephoneTextField;
    @FXML private TextField addressTextField;

    @FXML private Button saveButton;
    @FXML private Button editButton;

    @FXML private Label idLabel;

    @FXML private InventoryListView<BingoCard> inventoryListViewController;

    // MARK: - Data

    private Client client;

    private ObservableList<BingoCard> bingos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InventoryListViewSetter.prepareBingosInventory(inventoryListViewController);
        inventoryListViewController.getAddButton().setVisible(false);
    }

    // MARK: - Setters

    public void setClient(Client client) {
        this.client = client;

        idLabel.setText(client.getId());
        nameTextField.setText(client.getName());
        lastNameTextField.setText(client.getLastName());
        dniTextField.setText(client.getDni());
        addressTextField.setText(client.getAddress());
        telephoneTextField.setText(client.getTelephone());

        Database database = DBManager.getInstance().getCurrentDatabase();
        bingos = database.retrieveClientBingsCards(client.getId());
        inventoryListViewController.setModel(bingos);
    }
}
