package Model;

import Model.Database.DBManager;
import Model.Database.Database;

import java.util.*;

public class test {

    public static void asd() {
//        String s = FilenameUtils.concat(System.getProperty("user.home"), "Desktop");
//        File[] files = new File(s).listFiles();
//        for (File f: files) {
//            System.out.println(f.getAbsolutePath());
//        }
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a.add("" + i);
        }
        a.set(3, null);
        System.out.println(a);
    }

    public static void testAdd() {
        ArrayList<Matrix<Integer>> a = new ArrayList();
        Matrix<Integer> m = new Matrix<>(2,2);
        Matrix<Integer> w = new Matrix<>(2,2);
        w.set(0,0,1);
        m.set(0,0,1);
        a.add(m);
        Helper.addUniqueElementToList(w, a);
        System.out.println(a.size());
    }

    public static void testDB() {

        DBManager dbManager = DBManager.getInstance();

        dbManager.createDB("test");

//        dbManager.currentDatabaseNameProperty().setValue("test");
//        Database currDB = dbManager.getCurrentDatabase();
//        System.out.println(currDB.getBingos().size());
//        currDB.addNewBingos(5);
//        System.out.println(currDB.getBingos().size());

    }

    public static void testSearch() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String pattern = scanner.nextLine();
            String text = scanner.nextLine();
            System.out.println(Helper.match(pattern, text));
        }
    }

}


