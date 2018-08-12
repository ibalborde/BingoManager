package Model;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import sun.plugin.extension.ExtensionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void asd() {
        String s = FilenameUtils.concat(System.getProperty("user.home"), "Desktop");
        File[] files = new File(s).listFiles();
        for (File f: files) {
            System.out.println(f.getAbsolutePath());
        }
    }

}


