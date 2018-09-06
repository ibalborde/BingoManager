package GUI.BingoPlayView;

import GUI.CustomTextFields.NumberSpinner;
import GUI.CustomTextFields.NumberTextField;
import GUI.InventoryListView.InventoryListView;
import Model.Database.Client;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BingoPlayView implements Initializable {

    // MARK: - GUI

    @FXML private VBox inventoryListView;
    @FXML private HBox numberInsertBox;

    @FXML private ListView<Integer> numbersListView;

    @FXML private Button addButton;
    private NumberTextField numberTextField;

    @FXML private InventoryListView<Client> inventoryListViewController;

    // MARK: - Properties

    private BooleanProperty isPlaying;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberTextField = new NumberTextField();
        isPlaying = new SimpleBooleanProperty();

        numberTextField.setPrefWidth(200);
        numberInsertBox.getChildren().add(1, numberTextField);



        inventoryListViewController.removeRemoveButton();
        inventoryListViewController.removeAddButton();
    }

    // MARK: - Internal

    @FXML private void addNumber() {

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
