package GUI;

import GUI.CustomTextFields.NumberSpinner;
import Model.Database.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DialogsGenerator {

    /**
     * Muestra un cuadro de dialogo al usuario para saber
     * la cantidad de Bingos a crear
     * @return Valor opcional de la cantidad de bingos a crear.
     */
    public static Optional<Integer> askCountNewBingos() {
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
     * Comprueba los campos de textos de una lista tienen contendio
     * @param tfs Campos de texto a analizar
     * @return true si todos los campos de textos no están vacíos
     */
    private static boolean checkNotEmptuTextFields(List<TextField> tfs) {
        for (TextField tf: tfs) {
            if (tf.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Solicita al usuario la información necesaria para
     * Crear un nuevo usuario y lo retorna de ser posible
     */
    public static Optional<Client> askForNewClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Registrar Cliente");
        dialog.setHeaderText("Complete el siguiente formulario");

        // Configura los botones
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType createButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButton, createButtonType);

        // Busca el botón de aceptar
        Node okButton = dialog.getDialogPane().lookupButton(createButtonType);
        okButton.setDisable(true);

        // Elementos de comprobación de campos de texto
        List<TextField> tfs = new ArrayList<>();
        ChangeListener<String> cl = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                okButton.setDisable(!checkNotEmptuTextFields(tfs));
            }
        };

        // Crea los campos de texto
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Nombre");
        nameTextField.textProperty().addListener(cl);
        tfs.add(nameTextField);
        nameTextField.requestFocus();

        TextField lastNameTextField = new TextField();
        lastNameTextField.setPromptText("Apellido");
        lastNameTextField.textProperty().addListener(cl);
        tfs.add(lastNameTextField);

        TextField dniTextField = new TextField();
        dniTextField.setPromptText("DNI");
        dniTextField.textProperty().addListener(cl);
        tfs.add(dniTextField);

        TextField addressTextField = new TextField();
        addressTextField.setPromptText("Dirección");
        addressTextField.textProperty().addListener(cl);
        tfs.add(addressTextField);

        TextField telephoneTextField = new TextField();
        telephoneTextField.setPromptText("Teléfono");
        telephoneTextField.textProperty().addListener(cl);
        tfs.add(telephoneTextField);

        // Panel para agregar componentes
        GridPane form = new GridPane();
        form.setHgap(4);
        form.setVgap(8);
        form.add(new Label("Nombre"), 0, 0);
        form.add(nameTextField, 1, 0);
        form.add(new Label("Apellido"), 0, 1);
        form.add(lastNameTextField, 1, 1);
        form.add(new Label("DNI"), 0, 2);
        form.add(dniTextField, 1, 2);
        form.add(new Label("Dirección"), 0, 3);
        form.add(addressTextField, 1, 3);
        form.add(new Label("Teléfono"), 0, 4);
        form.add(telephoneTextField, 1, 4);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != createButtonType) {
                return null;
            }
            return new Client(  null,
                                nameTextField.getText(),
                                lastNameTextField.getText(),
                                dniTextField.getText(),
                                addressTextField.getText(),
                                telephoneTextField.getText()
                    );
        });

        return dialog.showAndWait();
    }
}