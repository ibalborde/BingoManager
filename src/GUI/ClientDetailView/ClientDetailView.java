package GUI.ClientDetailView;

import GUI.DialogsGenerator;
import GUI.InventoryListView.InventoryListView;
import GUI.InventoryListViewSetter;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Database.DBManager;
import Model.Database.Database;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientDetailView implements Initializable {

    // MARK: - UI Components

    @FXML private TextField nameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField dniTextField;
    @FXML private TextField telephoneTextField;
    @FXML private TextField addressTextField;

    @FXML private Button cancelButton;
    @FXML private Button editButton;

    @FXML private Label idLabel;

    @FXML private InventoryListView<BingoCard> inventoryListViewController;

    // MARK: - Data

    private Client client;

    private ObservableList<BingoCard> bingos;

    private boolean isEditing = false;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InventoryListViewSetter.prepareBingosInventory(inventoryListViewController);
        inventoryListViewController.getAddButton().setVisible(false);
    }

    // MARK: - Internal

    @FXML private void editButtonPress() {
        if (isEditing) {
            saveData();
            isEditing = false;
            setEditable(false);
            editButton.setText("Editar");
            cancelButton.setDisable(true);
        } else {
            isEditing = true;
            setEditable(true);
            editButton.setText("Guardar");
            cancelButton.setDisable(false);
        }
    }

    @FXML private void cancelButtonPress() {
        Optional<Boolean> shouldSave = Optional.of(false);

        // Cancela la operación
        if (dataHasChanged()) {
            shouldSave = DialogsGenerator.askShouldSave();
            if (!shouldSave.isPresent()) {
                return;
            }
        }

        if (shouldSave.get()) {
            saveData();
        } else {
            setFieldsData();
        }

        isEditing = false;
        setEditable(false);
        editButton.setText("Editar");
        cancelButton.setDisable(true);
    }

    /**
     * Indica si se ha modificado algún dato
     */
    private boolean dataHasChanged() {
        return  !nameTextField.getText().equals(client.getName()) ||
                !lastNameTextField.getText().equals(client.getLastName()) ||
                !dniTextField.getText().equals(client.getDni()) ||
                !addressTextField.getText().equals(client.getAddress()) ||
                !telephoneTextField.getText().equals(client.getTelephone());
    }

    /**
     * Restaura la inforamción de los campos de texto al estado
     * de la base de datos
     */
    private void setFieldsData() {
        idLabel.setText(client.getId());
        nameTextField.setText(client.getName());
        lastNameTextField.setText(client.getLastName());
        dniTextField.setText(client.getDni());
        addressTextField.setText(client.getAddress());
        telephoneTextField.setText(client.getTelephone());
    }

    /**
     * Guarda los datos en la base de datos
     */
    private void saveData() {
        // Actualiza la información
        client.setName(nameTextField.getText());
        client.setLastName(lastNameTextField.getText());
        client.setDni(dniTextField.getText());
        client.setAddress(addressTextField.getText());
        client.setTelephone(telephoneTextField.getText());

        // Guarda la información
        DBManager.getInstance().saveData(client);
    }

    /**
     * Configura los campos de textos para ser editables
     * @param editable True si se quiere hacer editable los campos de texto
     */
    private void setEditable(boolean editable) {
        nameTextField.setEditable(editable);
        lastNameTextField.setEditable(editable);
        dniTextField.setEditable(editable);
        addressTextField.setEditable(editable);
        telephoneTextField.setEditable(editable);
    }

    // MARK: - Setters

    public void setClient(Client client) {
        this.client = client;

        this.setFieldsData();

        Database database = DBManager.getInstance().getCurrentDatabase();
        bingos = database.retrieveClientBingsCards(client.getId());
        inventoryListViewController.setModel(bingos);
    }
}
