import Model.BingoGenerator;
import Model.Helper;
import Model.Matrix;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/welcome.fxml"));
        primaryStage.setTitle("Bingo Manager");
        primaryStage.setScene(new Scene(root, 450, 200));
        //primaryStage.show();
//        for (List<Integer> l: BingoGenerator.generateBingos(100)) {
//            System.out.println(l);
//        }

        for (int i = 0; i < 100; i++) {
            BingoGenerator.generateBingo().printMatrix();
            System.out.println();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
