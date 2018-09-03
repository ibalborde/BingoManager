package GUI.ClientsView;

import GUI.BingoDetailView.BingoDetailView;
import GUI.ClientDetailView.ClientDetailView;
import GUI.DialogsGenerator;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Database.DBManager;
import Model.Database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsView implements Initializable {

    @FXML private InventoryListView<Client> inventoryListViewController;

    @FXML private Button showDetailsButton;
    @FXML private Label clientIDLabel;
    @FXML private Label clientNameLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InventoryListViewSetter.prepareClientInventory(inventoryListViewController);

        TableView<Client> table = inventoryListViewController.getTableView();

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            setCurrentItem(n);
            inventoryListViewController.getRemoveButton().setDisable(n == null);
            showDetailsButton.setDisable(n == null);
        });

        Database db = DBManager.getInstance().getCurrentDatabase();
        inventoryListViewController.setModel(db.getClients());

        inventoryListViewController.getAddButton().setOnAction( e -> {
            DialogsGenerator.askForNewClient().ifPresent(client -> db.addNewClient(client));
        });
        inventoryListViewController.getRemoveButton().setOnAction(e -> {
            Client selectedItem = table.getSelectionModel().getSelectedItem();

            String id = selectedItem.getId();
            if (DialogsGenerator.confirmDestructive("eliminar al usuario" + selectedItem.getFullName(), "Eliminar")) {
                DBManager.getInstance().getCurrentDatabase().removeClient(id);
            }
        });
    }

    // MARK: - Internal

    @FXML private void showDetails() {
        Client client = inventoryListViewController.getTableView().getSelectionModel().getSelectedItem();
        try {
            URL url = getClass().getResource("../ClientDetailView/ClientDetailView.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Node content = fxmlLoader.load();

            //fxmlLoader.setController(bingoDetailView);
            ClientDetailView bingoDetailView = (ClientDetailView) fxmlLoader.getController();
            bingoDetailView.setClient(client);

            DialogsGenerator.showDialog("Detalles", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
