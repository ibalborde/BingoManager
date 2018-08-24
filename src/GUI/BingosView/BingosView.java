package GUI.BingosView;

import GUI.BingoCardView.BingoCardView;
import GUI.CustomTextFields.NumberSpinner;
import GUI.InventoryListView.FilterDelegate;
import GUI.InventoryListView.InventoryListView;
import GUI.UIHelper;
import Model.Database.BingoCard;
import Model.Database.DBManager;
import Model.Database.Database;
import Model.Helper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
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
     * Muestra un cuadro de dialogo al usuario para saber
     * la cantidad de Bingos a crear
     * @return Valor opcional de la cantidad de bingos a crear.
     */
    private Optional<Integer> askCountNewBingos() {
        // Crea el cuadro de dialogo personalizado
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Crear Bingos");
        dialog.setHeaderText("¿Cuántos bingos quiere crear?");

        // Configura los botones
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType createButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(cancelButton, createButtonType);

        // Crea los campos de ingreso
        HBox countPane = new HBox();
        countPane.setPadding(new Insets(10, 10, 10, 10));

        NumberSpinner numberSpinner = new NumberSpinner();
        numberSpinner.setNumber(BigDecimal.ONE);
        countPane.getChildren().add(numberSpinner);
        HBox.setHgrow(numberSpinner, Priority.ALWAYS);

        // Busca el botón de aceptar
        Node okButton = dialog.getDialogPane().lookupButton(createButtonType);

        numberSpinner.numberProperty().addListener((obs, o, n) -> {
            okButton.setDisable(n.intValue() <= 0);
        });

        dialog.getDialogPane().setContent(countPane);

        // Obtiene el resultado
        dialog.setResultConverter(dialogButton -> {
            return dialogButton == createButtonType ? numberSpinner.getNumber().intValue() : null;
        });

        return dialog.showAndWait();
    }

    /**
     * Crea la cantidad de bingos que el usuario indique
     */
    private void createBingos() {
        askCountNewBingos().ifPresent(count -> {
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
