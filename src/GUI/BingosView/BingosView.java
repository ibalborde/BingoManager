package GUI.BingosView;

import GUI.BingoCardView.BingoCardView;
import GUI.DialogsGenerator;
import GUI.InventoryListView.FilterDelegate;
import GUI.InventoryListView.InventoryListView;
import GUI.UIHelper;
import Model.Database.BingoCard;
import Model.Database.DBManager;
import Model.Database.Database;
import Model.Helper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class BingosView implements Initializable, FilterDelegate<BingoCard> {

    @FXML private InventoryListView<BingoCard> inventoryListViewController;
    @FXML private BingoCardView bingoCardViewController;
    @FXML private Label bingoIDLabel;
    @FXML private Button showDetailButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBManager.getInstance().currentDatabaseProperty().addListener((obs, o, n) -> updateTableModel());
        updateTableModel();
        this.setupTable();

        inventoryListViewController.setFiltrable(this);
        inventoryListViewController.getAddButton().setOnAction(e -> {
            //DBManager.getInstance().getCurrentDatabase().addNewBingo();
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
            if (UIHelper.confirmDeletion("bingo " + id)) {
                DBManager.getInstance().getCurrentDatabase().removeBingo(id);
            }
        });
    }

    //　MARK: - Internal

    /**
     * Prepara la tabla
     */
    private void setupTable() {
        TableView<BingoCard> table = inventoryListViewController.getTableView();

        // Sell Status Column
        TableColumn<BingoCard, Boolean> bingoCardStatusColumn = new TableColumn<>("Estado");
        bingoCardStatusColumn.setCellValueFactory(
                new PropertyValueFactory<BingoCard, Boolean>("owned")
        );
        bingoCardStatusColumn.setCellFactory(e -> new CheckBoxTableCell<>());

        // Bingo ID Column
        TableColumn<BingoCard, String> bingoIDColumn = new TableColumn<>("ID");
        bingoIDColumn.setCellValueFactory(
                new PropertyValueFactory<BingoCard, String>("id")
        );
        table.widthProperty().addListener((obs, o, width) -> {
            double w = width.doubleValue();
            bingoCardStatusColumn.setPrefWidth(w * 0.1);
            bingoIDColumn.setPrefWidth(w * 0.9);
        });

        table.getColumns().addAll(bingoCardStatusColumn, bingoIDColumn);
    }

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
        // TODO: completar para mostrar detalles
    }

    // MARK: - FilterDelegate

    @Override
    public boolean shouldIncoude(BingoCard element, String pattern) {
        List<String> numbers = Arrays.asList(pattern.split(" "));
        return Helper.matrixContains(element.getBingo(), numbers) ||
               Helper.match(pattern, element.getId());
    }
}
