package GUI.BingoCardView;

import Model.Matrix;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class BingoCardView implements Initializable {

    @FXML
    private GridPane gridViews;

    private Matrix<Label> labelsMatrix;
    private Matrix<HBox> backgroundsMatrix;

    private Matrix<Integer> cardModel;

    // MARK: - Init

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Matrix<Label> labelsMatrix = new Matrix<>(3,9);
        Matrix<HBox> backgroundsMatrix = new Matrix<>(3,9);

        Iterator iterator = gridViews.getChildren().iterator();
        for (int row = 0; row < labelsMatrix.getRows(); row++) {
            for (int col = 0; col < labelsMatrix.getColumns(); col++) {
                HBox node = (HBox) iterator.next();
                Label label = (Label) node.getChildren().get(0);

                labelsMatrix.set(row, col, label);
                backgroundsMatrix.set(row, col, node);
            }
        }

        this.backgroundsMatrix = backgroundsMatrix;
        this.labelsMatrix = labelsMatrix;

        updateUI();
    }

    // MARK: - Internal

    /**
     * Update the user interface after editing the data matrix
     */
    private void updateUI() {
        if (cardModel == null) {
            for (int row = 0; row < labelsMatrix.getRows(); row++) {
                for (int col = 0; col < labelsMatrix.getColumns(); col++) {
                    labelsMatrix.get(row, col).setText(null);
                    backgroundsMatrix.get(row, col).setStyle(null);
                }
            }
            return;
        }
        for (int row = 0; row < labelsMatrix.getRows(); row++) {
            for (int col = 0; col < labelsMatrix.getColumns(); col++) {
                Integer value = cardModel.get(row,col);
                if (value == null) {
                    labelsMatrix.get(row, col).setText(null);
                    backgroundsMatrix.get(row, col).setStyle("-fx-background-color: black");
                } else {
                    labelsMatrix.get(row, col).setText("" + value);
                    backgroundsMatrix.get(row, col).setStyle(null);
                }
            }
        }
    }

    // MARK: - Getters & Setters

    public void setCardModel(Matrix<Integer> cardModel) {
        this.cardModel = cardModel;
        updateUI();
    }
}
