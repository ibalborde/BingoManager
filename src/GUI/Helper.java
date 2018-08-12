package GUI;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Helper {

    public static String getUserTextInput(String title, String header, String question) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(question);

        Optional<String> result = dialog.showAndWait();

        return result.isPresent() ? result.get() : null;
    }
}
