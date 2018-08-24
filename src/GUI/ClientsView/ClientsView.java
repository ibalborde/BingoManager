package GUI.ClientsView;

import GUI.InventoryListView.FilterDelegate;
import GUI.InventoryListView.InventoryListView;
import Model.Database.Client;
import Model.Database.DBManager;
import Model.Database.Database;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsView implements Initializable, FilterDelegate<Client> {

    @FXML private InventoryListView<Client> inventoryListViewController;

    @FXML private Button showDetailsButton;
    @FXML private Label clientIDLabel;
    @FXML private Label clientNameLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Database db = DBManager.getInstance().getCurrentDatabase();
        inventoryListViewController.setFiltrable(this);
        inventoryListViewController.setModel(db.getClients());

        inventoryListViewController.getAddButton().setOnAction( e -> {
            db.addNewClient("carlos", "h", "", "", "");
        });

        setupTable();
    }

    private void setupTable() {
        TableView<Client> table = inventoryListViewController.getTableView();

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            setCurrentItem(n);
        });

        TableColumn<Client, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(it -> it.getValue().getId());

        TableColumn<Client, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(it -> it.getValue().nameProperty());

        TableColumn<Client, String> lastNameCol = new TableColumn<>("Apellido");
        lastNameCol.setCellValueFactory(it -> it.getValue().lastNameProperty());

        TableColumn<Client, String> addressCol = new TableColumn<>("Dirección");
        addressCol.setCellValueFactory(it -> it.getValue().addressProperty());

        TableColumn<Client, String> telephoneCol = new TableColumn<>("Teléfono");
        telephoneCol.setCellValueFactory(it -> it.getValue().telephoneProperty());

        table.getColumns().addAll(nameCol, lastNameCol, telephoneCol, addressCol, idCol);
        table.widthProperty().addListener((obs, o, n) -> {
            Double newWidth = n.doubleValue() / 5;
            idCol.setPrefWidth(newWidth);
            nameCol.setPrefWidth(newWidth);
            lastNameCol.setPrefWidth(newWidth);
            addressCol.setPrefWidth(newWidth);
            telephoneCol.setPrefWidth(newWidth);
        });
    }

    @FXML private void showDetails() {

    }

    private void setCurrentItem(Client client) {
        if (client == null) {
            clientIDLabel.setText(null);
            clientNameLabel.setText(null);
        } else {
            clientIDLabel.setText(client.getId().get());
            clientNameLabel.setText(client.getFullName());
        }
    }

    @Override
    public boolean shouldIncoude(Client element, String pattern) {
        return true;
    }
}
