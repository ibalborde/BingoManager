import Model.Database.DBManager;
import Model.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/BingoManager/BingoManager.fxml"));
        primaryStage.setTitle("Bingo Manager");
        primaryStage.setScene(new Scene(root, 800, 400)); // 450 200
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(400);
        primaryStage.show();

        /*for (Matrix<Integer> bingo: BingoGenerator.generateBingos(15)) {
            bingo.printMatrix();
            System.out.println();
        }*/
    }


    public static void main(String[] args) {
        test.testDB();
        DBManager.getInstance().currentDatabaseNameProperty().setValue("test");
        launch(args);
    }
}
