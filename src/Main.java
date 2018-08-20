import Model.BingoGenerator;
import Model.Helper;
import Model.Matrix;
import Model.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/BingoCardView/BingoCardView.fxml"));
        primaryStage.setTitle("Bingo Manager");
        primaryStage.setScene(new Scene(root, 450, 200));
        primaryStage.show();

        /*for (Matrix<Integer> bingo: BingoGenerator.generateBingos(15)) {
            bingo.printMatrix();
            System.out.println();
        }*/
    }


    public static void main(String[] args) {
        //launch(args);
        test.testDB();
    }
}
