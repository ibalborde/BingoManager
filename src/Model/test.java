package Model;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import sun.plugin.extension.ExtensionUtils;

import java.io.File;
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

}


