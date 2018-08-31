package GUI.BingoPlayView;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BingoPlayView implements Initializable {

    // MARK: - GUI


    // MARK: - Properties

    private BooleanProperty isPlaying;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isPlaying = new SimpleBooleanProperty();
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
