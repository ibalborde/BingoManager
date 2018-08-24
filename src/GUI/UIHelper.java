package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class UIHelper {

    /**
     * Solicita al usuario que ingrese un texto
     * @param title Título de la ventana
     * @param header Header del cuadro de dialogo
     * @param question Pista de la información a solicitar
     * @return La información ingresada, null en caso de no haber nada.
     */
    public static String getUserTextInput(String title, String header, String question) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(question);

        Optional<String> result = dialog.showAndWait();

        return result.isPresent() ? result.get() : null;
    }

    /**
     * Solicita al usuario confirmación para eliminar un elemento
     * @param itemName Nombre del elemento a eliminar
     * @return true si se ha confirmado
     */
    public static boolean confirmDeletion(String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar " + itemName);

        ButtonType deleteButton = new ButtonType("Eliminar");
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == deleteButton;
    }

}
