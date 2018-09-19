package GUI.BingoPlayView;

import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;

import Model.Database.DBManager;
import Model.Database.Database;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class BingoPlayView implements Initializable {

    // MARK: - GUI

    @FXML private TilePane numbersGrid;

    @FXML private InventoryListView<BingoCard> inventoryListViewController;

    // MARK: - Properties

    private BooleanProperty isPlaying;

    private FilteredList<BingoCard> bingoCards;

    private Map<BingoCard, Set<Integer>> bingosIndex;

    private Set<Integer> numbers;

    private ObservableList<BooleanProperty> numberSelections;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bingosIndex = new HashMap<BingoCard, Set<Integer>>();
        this.numbers = new HashSet<>();
        this.numberSelections = FXCollections.observableArrayList();

        isPlaying = new SimpleBooleanProperty();
        isPlaying.addListener((obs, o, n) -> {
            if (n) {
                prepareForPlay();
            }
        });

        numbersGrid.setHgap(8);
        numbersGrid.setVgap(4);
        numbersGrid.setPrefColumns(10);
        numbersGrid.setTileAlignment(Pos.TOP_LEFT);

        inventoryListViewController.removeRemoveButton();
        inventoryListViewController.removeAddButton();
        InventoryListViewSetter.prepareBingosInventory(inventoryListViewController);
    }

    // MARK: - Internal

    private void setNumberSelections() {
        numberSelections.clear();
        for (int i = 1; i < 100; i++) {
            final int index = i;
            BooleanProperty booleanProperty = new SimpleBooleanProperty();
            booleanProperty.addListener((obs, o, n) -> {
                if (n) {
                    this.numbers.add(index);
                } else {
                    this.numbers.remove(index);
                }
                updateBingos();
            });
            numberSelections.add(booleanProperty);
        }
    }

    private void fillNumbersBox() {
        for (int i = 1; i <= 99; i++) {
            String text = "" + i;

            CheckBox checkBox = new CheckBox(text);
            checkBox.selectedProperty().bindBidirectional(numberSelections.get(i - 1));

            numbersGrid.getChildren().add(checkBox);
        }
    }

    private void updateBingos() {
        bingoCards.setPredicate(it -> {
            if (numbers.isEmpty()) {
                return false;
            }
            Set<Integer> itNumbers = this.bingosIndex.get(it);
            return numbers.containsAll(itNumbers);
        });
    }

    /**
     * Prepara el entorno para jugar
     */
    private void prepareForPlay() {
        Database database = DBManager.getInstance().getCurrentDatabase();
        ObservableList<BingoCard> bingos = database.retrieveSelledBingos();

        for (BingoCard bingoCard: bingos) {
            Set<Integer> numbers = new HashSet<>(bingoCard.getBingo().getNotNullValues());
            bingosIndex.put(bingoCard, numbers);
        }
        this.bingoCards = new FilteredList<>(bingos);
        inventoryListViewController.setModel(bingoCards);

        this.setNumberSelections();
        this.fillNumbersBox();

        this.updateBingos();
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
