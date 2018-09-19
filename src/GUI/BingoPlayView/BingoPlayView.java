package GUI.BingoPlayView;

import GUI.CustomTextFields.NumberTextField;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;

import Model.Database.DBManager;
import Model.Database.Database;
import Model.Helper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BingoPlayView implements Initializable {

    // MARK: - GUI

    @FXML private VBox inventoryListView;
    @FXML private HBox numberInsertBox;

    @FXML private ListView<Integer> numbersListView;

    @FXML private Button addButton;
    @FXML private TextField numberTextField;

    @FXML private InventoryListView<BingoCard> inventoryListViewController;

    // MARK: - Properties

    private BooleanProperty isPlaying;

    private FilteredList<BingoCard> bingoCards;

    private ObservableList<Integer> numbers;

    private Integer value;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberTextField.textProperty().addListener((obs, o, n) -> {
            try {
                this.value = Integer.parseInt(n);
                if (value > 0 && value < 100) {
                    addButton.setDisable(false);
                    System.out.println(value);
                    return;
                }
            } catch (Exception e) {}
            this.value = null;
            addButton.setDisable(true);
        });

        numbers = FXCollections.observableArrayList();
        numbersListView.setItems(numbers);

        isPlaying = new SimpleBooleanProperty();
        isPlaying.addListener((obs, o, n) -> {
            if (n) {
                prepareForPlay();
            }
        });

        numberTextField.setPrefWidth(200);

        inventoryListViewController.removeRemoveButton();
        inventoryListViewController.removeAddButton();
        InventoryListViewSetter.prepareBingosInventory(inventoryListViewController);
    }

    // MARK: - Internal

    @FXML private void addNumber() {
        if (value != null && Helper.addUniqueElementToList(value, numbers)) {
            updateBingos();
        }
    }

    private void updateBingos() {
        List<Integer> numbers = numbersListView.getItems().sorted();
        bingoCards.setPredicate(it -> {
            ArrayList<Integer> bnumbers = it.getBingo().getNotNullValues();

            return numbers.equals(bnumbers);
        });
    }

    /**
     * Prepara el entorno para jugar
     */
    private void prepareForPlay() {
        Database database = DBManager.getInstance().getCurrentDatabase();
        this.bingoCards = new FilteredList<>(database.retrieveSelledBingos());

        inventoryListViewController.setModel(bingoCards);
    }

    // MARK: - Getters & Setters

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }
}
