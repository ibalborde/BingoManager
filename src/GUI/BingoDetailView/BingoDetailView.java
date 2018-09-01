package GUI.BingoDetailView;

import GUI.DialogsGenerator;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import GUI.UIHelper;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Database.DBManager;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
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

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableView.TableViewSelectionModel<Client> selectionModel = inventoryListViewController.getTableView().getSelectionModel();

        InventoryListViewSetter.prepareClientInventory(inventoryListViewController);

        inventoryListViewController.getAddButton().setOnAction(e -> {
            try {
                DialogsGenerator.askForClientList().ifPresent(list -> {
                    for (Client client: list) {
                        bingoCard.addOwner(client.getId());
                    }
                    DBManager.getInstance().saveData(bingoCard);
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        inventoryListViewController.getRemoveButton().setOnAction(e -> {
            List<Client> clients = new ArrayList<>(selectionModel.getSelectedItems());
            String message = "eliminar " + clients.size() + " cliente(s)";
            if (UIHelper.confirmDestructive(message, "Eliminar")) {
                for (Client client: clients) {
                    bingoCard.removeOwner(client.getId());
                }

                DBManager.getInstance().saveData(bingoCard);
            }
        });

        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.selectedItemProperty().addListener((obs, o, n) -> {
            inventoryListViewController.getRemoveButton().setDisable(n == null);
        });
    }

    // MARK: - Setter

    public void setBingoCard(BingoCard bingoCard) {
        this.bingoCard = bingoCard;
        this.idLabel.setText(bingoCard.getId());
        this.payCheckbox.setSelected(bingoCard.isPayed());
        this.payCheckbox.setDisable(!bingoCard.isOwned());
        this.inventoryListViewController.setModel(bingoCard.getOwners());


        bingoCard.payedProperty().addListener((obs, o, n) -> {
            payCheckbox.setSelected(n);
        });

        bingoCard.getOwnersIds().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                inventoryListViewController.setModel(bingoCard.getOwners());
                payCheckbox.setDisable(!bingoCard.isOwned());
            }
        });

        payCheckbox.setOnAction(e -> {
            DBManager dbManager = DBManager.getInstance();
            if (payCheckbox.isSelected()) {
                bingoCard.setPayed(true);
                dbManager.saveData(bingoCard);
            } else if (UIHelper.confirmDestructive("marcar como \"no pagado\"", "Si")) {
                bingoCard.setPayed(false);
                dbManager.saveData(bingoCard);
            } else {
                payCheckbox.setSelected(true);
            }
        });
    }
}
