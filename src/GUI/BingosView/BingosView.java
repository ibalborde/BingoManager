package GUI.BingosView;

import GUI.BingoCardView.BingoCardView;
import GUI.BingoDetailView.BingoDetailView;
import GUI.DialogsGenerator;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;
import Model.Database.DBManager;
import Model.Database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class BingosView implements Initializable {

    @FXML private InventoryListView<BingoCard> inventoryListViewController;
    @FXML private BingoCardView bingoCardViewController;
    @FXML private Label bingoIDLabel;
    @FXML private Button showDetailButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBManager.getInstance().currentDatabaseProperty().addListener((obs, o, n) -> updateTableModel());
        updateTableModel();
        InventoryListViewSetter.prepareBingosInventory(inventoryListViewController);

        inventoryListViewController.getAddButton().setOnAction(e -> {
            createBingos();
        });
        inventoryListViewController.getTableView().getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            setCurrentItem(n);
            inventoryListViewController.getRemoveButton().setDisable(n == null);
        });
        inventoryListViewController.getRemoveButton().setOnAction(e -> {
            TableView<BingoCard> table = inventoryListViewController.getTableView();
            BingoCard selectedItem = table.getSelectionModel().getSelectedItem();
            String id = selectedItem.getId();
            if (DialogsGenerator.confirmDestructive("eliminar el bingo " + id, "Eliminar")) {
                DBManager.getInstance().getCurrentDatabase().removeBingo(id);
            }
        });
    }

    //　MARK: - Internal

    /**
     * Crea la cantidad de bingos que el usuario indique
     */
    private void createBingos() {
        DialogsGenerator.askCountNewBingos().ifPresent(count -> {
            DBManager.getInstance().getCurrentDatabase().addNewBingos(count);
        });
    }

    // MARK: - Update

    /**
     * Actualiza el modelo de la tabla de bingos
     */
    private void updateTableModel() {
        Database db = DBManager.getInstance().getCurrentDatabase();
        inventoryListViewController.setModel(db == null ? null : db.getBingos());
    }

    /**
     * Muestra la información relacionada con el item actualmente seleccionado
     * @param bingoCard Item de bingo a procesar
     */
    private void setCurrentItem(BingoCard bingoCard) {
        if (bingoCard == null) {
            bingoIDLabel.setText(null);
            showDetailButton.setDisable(true);
            bingoCardViewController.setCardModel(null);
        } else {
            bingoIDLabel.setText(bingoCard.getId());
            showDetailButton.setDisable(false);
            bingoCardViewController.setCardModel(bingoCard.getBingo());
        }
    }

    // MARK: - Show Details

    @FXML private void showDetails() {
        BingoCard bingoCard = inventoryListViewController.getTableView().getSelectionModel().getSelectedItem();
        try {
            URL url = getClass().getResource("../BingoDetailView/BingoDetailView.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Node content = fxmlLoader.load();

            //fxmlLoader.setController(bingoDetailView);
            BingoDetailView bingoDetailView = (BingoDetailView) fxmlLoader.getController();
            bingoDetailView.setBingoCard(bingoCard);

            DialogsGenerator.showDialog("Detalles", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
