package GUI.BingoManager;

import GUI.BingoPlayView.BingoPlayView;
import GUI.DialogsGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BingoManager implements Initializable {

    // MARK: - GUI

    @FXML private TabPane tabPane;

    @FXML private Tab clientsTab;
    @FXML private Tab bingosTab;
    @FXML private Tab playTab;

    // MARK: - Controllers

    @FXML private BingoPlayView bingoPlayViewController;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs,o,n)-> {
            if (n == playTab) {
                if (DialogsGenerator.askToPlay()) {
                    bingoPlayViewController.setIsPlaying(true);
                } else {
                    tabPane.getSelectionModel().select(o);
                }
            }
        });
        bingoPlayViewController.isPlayingProperty().addListener((obs, o, n) -> {
            bingosTab.setDisable(n);
            clientsTab.setDisable(n);
        });
    }
}
