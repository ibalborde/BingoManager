package Model;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public static final String folderName = "BingoManager";
    public static final String dataBaseFolderName = "databases";

    // MARK: - Instance

    /**
     * Singleton instance reference
     */
    private static DBManager instance = null;

    /**
     * Return the singleton reference
     */
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    // MARK: - Variables

    private String basePath;
    private String databasesPath;

    private List<String> databases;

    private DBManager() {
        String home = System.getProperty("user.home");

        this.basePath = FilenameUtils.concat(home, folderName);
        File baseFolder = new File(basePath);
        baseFolder.mkdirs();

        this.databasesPath = FilenameUtils.concat(basePath, dataBaseFolderName);
        File databasesFolder = new File(databasesPath);
        databasesFolder.mkdirs();


        this.databases = new ArrayList<>();
        File[] possibleDatabases = databasesFolder.listFiles();
        for (File f: possibleDatabases) {
            String name = f.getName();
            if (name.endsWith(".json")) {
                databases.add(FilenameUtils.removeExtension(name));
            }
        }

    }


    public List<String> getDatabases() {
        return databases;
    }
}
