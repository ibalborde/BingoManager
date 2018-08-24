package GUI.InventoryListView;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


public class InventoryListView<T> implements Initializable {

    // MARK: - UI Components

    @FXML private Button cancelButton;
    @FXML private Button addButton;
    @FXML private Button removeButton;

    @FXML private TableView<T> tableView;
    @FXML private TextField textField;

    @FXML private HBox extraFilterPane;

    // MARK: - Data Management Variables

    private ObservableList<T> model;
    private FilteredList<T> filteredList;

    private FilterDelegate<T> filterDelegate;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(event -> {
            textField.setText("");
        });

        textField.textProperty().addListener((obs, o, n) -> updateFilter());

        updateFilter();
    }

    // MARK: - Internal

    private void updateFilter() {
        final String pattern = textField.getText().trim();
        cancelButton.setDisable(pattern.isEmpty());

        if (filteredList == null) {
            return;
        }

        if (pattern.isEmpty() || filterDelegate == null) {
            filteredList.setPredicate(item -> true);
        } else {
            filteredList.setPredicate(item -> filterDelegate.shouldIncoude(item ,pattern));
        }
    }

    // MARK: - Getters

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    public TextField getTextField() {
        return textField;
    }

    public HBox getExtraFilterPane() {
        return extraFilterPane;
    }

    // MARK: - Setters


    public void setFiltrable(FilterDelegate<T> filtrable) {
        this.filterDelegate = filtrable;
    }

    public void setModel(ObservableList<T> model) {
        this.model = model;
        this.filteredList = model == null ? null : new FilteredList<>(model);
        tableView.setItems(filteredList);
        updateFilter();
    }
}
