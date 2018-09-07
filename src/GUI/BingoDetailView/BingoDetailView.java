package GUI.BingoDetailView;

import GUI.DialogsGenerator;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Database.DBManager;
import Model.Helper;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BingoDetailView implements Initializable {

    // MARK: - UI Components

    @FXML private Label idLabel;
    @FXML private CheckBox payCheckbox;
    @FXML private InventoryListView<Client> inventoryListViewController;

    // MARK: - Data

    private BingoCard bingoCard;

    private ObservableList<Client> clients;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableView.TableViewSelectionModel<Client> selectionModel = inventoryListViewController.getTableView().getSelectionModel();

        InventoryListViewSetter.prepareClientInventory(inventoryListViewController);

        inventoryListViewController.getAddButton().setOnAction(e -> {
            DialogsGenerator.askForClientList().ifPresent(list -> {
                for (Client client: list) {
                    bingoCard.addOwner(client.getId());
                    Helper.addUniqueElementToList(client, clients);
                }
                DBManager.getInstance().saveData(bingoCard);
            });

        });

        inventoryListViewController.getRemoveButton().setOnAction(e -> {
            List<Client> selectedClients = new ArrayList<>(selectionModel.getSelectedItems());
            String message = "eliminar " + selectedClients .size() + " cliente(s)";
            if (DialogsGenerator.confirmDestructive(message, "Eliminar")) {
                for (Client client: selectedClients) {
                    bingoCard.removeOwner(client.getId());
                    clients.remove(client);
                }

                DBManager.getInstance().saveData(bingoCard);
            }
        });

        // Selección de items
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.selectedItemProperty().addListener((obs, o, n) -> {
            inventoryListViewController.getRemoveButton().setDisable(n == null);
        });
    }

    // MARK: - Setter

    public void setBingoCard(BingoCard bingoCard) {
        this.bingoCard = bingoCard;

        this.idLabel.textProperty().bind(bingoCard.idProperty());
        this.payCheckbox.disableProperty().bind(bingoCard.ownedProperty().not()); // .setDisable(!bingoCard.isOwned());
        this.inventoryListViewController.setModel(bingoCard.getOwners());

        // Estado del checkbox
        this.payCheckbox.setSelected(bingoCard.isPayed());
        bingoCard.payedProperty().addListener((obs, o, n) -> {
            payCheckbox.setSelected(n);
        });

        clients = DBManager.getInstance().getCurrentDatabase().retrieveBingoOwners(bingoCard.getId());
        inventoryListViewController.setModel(clients);

        payCheckbox.setOnAction(e -> {
            DBManager dbManager = DBManager.getInstance();
            if (payCheckbox.isSelected()) {
                bingoCard.setPayed(true);
                dbManager.saveData(bingoCard);
            } else if (DialogsGenerator.confirmDestructive("marcar como \"no pagado\"", "Si")) {
                bingoCard.setPayed(false);
                dbManager.saveData(bingoCard);
            } else {
                payCheckbox.setSelected(true);
            }
        });
    }
}
