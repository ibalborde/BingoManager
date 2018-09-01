package GUI;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;

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
     * Solicita al usuario confirmación para realizar tarea destructiva
     * @param message Mensaje
     * @param destructiveOption Nombre del botón de confirmación
     * @return true si se ha confirmado
     */
    public static boolean confirmDestructive(String message, String destructiveOption) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Antes de continuar");
        alert.setHeaderText("¿Está seguro que desea " + message);

        ButtonType deleteButton = new ButtonType(destructiveOption, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == deleteButton;
    }

    /**
     * Muestra un cuadro de diálogo modal
     * @param title Título del cuadro de díalogo
     * @param content Contenido del cuadro de diálogo
     */
    public static void showDialog(String title, Node content) {
        Dialog<Void> dialog = new Dialog<>();

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> dialog.close());

        dialog.setTitle(title);
        dialog.getDialogPane().setContent(content);
        dialog.show();

    }

}
