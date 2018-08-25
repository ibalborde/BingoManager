package GUI.ClientsView;

import GUI.DialogsGenerator;
import GUI.InventoryListView.FilterDelegate;
import GUI.InventoryListView.InventoryListView;
import GUI.UIHelper;
import Model.Database.BingoCard;
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
import java.util.Optional;
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
            DialogsGenerator.askForNewClient().ifPresent(client -> db.addNewClient(client));
        });
        inventoryListViewController.getRemoveButton().setOnAction(e -> {
            TableView<Client> table = inventoryListViewController.getTableView();
            Client selectedItem = table.getSelectionModel().getSelectedItem();

            String id = selectedItem.getId();
            if (UIHelper.confirmDeletion("cliente " + selectedItem.getFullName())) {
                DBManager.getInstance().getCurrentDatabase().removeClient(id);
            }
        });

        setupTable();
    }

    // MARK: - Internal

    /**
     * Prapara la tabla para mostrar la información
     */
    private void setupTable() {
        TableView<Client> table = inventoryListViewController.getTableView();

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            setCurrentItem(n);
            inventoryListViewController.getRemoveButton().setDisable(n == null);
            showDetailsButton.setDisable(n == null);
        });

        TableColumn<Client, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(it -> it.getValue().idProperty());

        TableColumn<Client, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(it -> it.getValue().nameProperty());

        TableColumn<Client, String> lastNameCol = new TableColumn<>("Apellido");
        lastNameCol.setCellValueFactory(it -> it.getValue().lastNameProperty());

        TableColumn<Client, String> addressCol = new TableColumn<>("Dirección");
        addressCol.setCellValueFactory(it -> it.getValue().addressProperty());

        TableColumn<Client, String> telephoneCol = new TableColumn<>("Teléfono");
        telephoneCol.setCellValueFactory(it -> it.getValue().telephoneProperty());

        TableColumn<Client, String> dniCol = new TableColumn<>("Dni");
        dniCol.setCellValueFactory(it -> it.getValue().dniProperty());

        table.getColumns().addAll(nameCol, lastNameCol, dniCol, telephoneCol, addressCol, idCol);
        table.widthProperty().addListener((obs, o, n) -> {
            Double newWidth = n.doubleValue() / 6;
            idCol.setPrefWidth(newWidth);
            nameCol.setPrefWidth(newWidth);
            lastNameCol.setPrefWidth(newWidth);
            addressCol.setPrefWidth(newWidth);
            telephoneCol.setPrefWidth(newWidth);
            dniCol.setPrefWidth(newWidth);
        });
    }

    @FXML private void showDetails() {

    }

    private void setCurrentItem(Client client) {
        if (client == null) {
            clientIDLabel.setText(null);
            clientNameLabel.setText(null);
        } else {
            clientIDLabel.setText(client.getId());
            clientNameLabel.setText(client.getFullName());
        }
    }

    @Override
    public boolean shouldIncoude(Client element, String pattern) {
        return true;
    }
}
