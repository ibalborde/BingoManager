package Model;

import com.sun.xml.internal.rngom.digested.DDataPattern;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import sun.plugin.extension.ExtensionUtils;

import java.io.File;
import java.io.IOException;
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

        //dbManager.createDB("test");

//        try {
//            dbManager.loadDatabase("test");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



//        Database currDB = dbManager.getCurrentDatabase();

       // String cid = currDB.addNewClient("Carlos", "Rod", "12","sasa", "435");
//        currDB.getClientsIndex().get(cid).getName());
//        dbManager.saveClientsDB();

//        String cid = currDB.getClients().get(0).getId();
//
//        currDB.addNewBingos(50);
//
//        String bid = currDB.getBingos().get(0).getId();
//
//        currDB.addNewSell(bid, cid);
//        currDB.getSells().forEach((k,v) -> {
//            System.out.println(v);
//        });


    }

}


