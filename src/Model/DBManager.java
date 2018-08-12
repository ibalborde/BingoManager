package Model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
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

    private ObjectProperty<String> currentDatabaseName;
    private ObservableList<String> databasesNames;

    private DBManager() {
        String home = System.getProperty("user.home");

        this.basePath = FilenameUtils.concat(home, folderName);
        File baseFolder = new File(basePath);
        baseFolder.mkdirs();

        this.databasesPath = FilenameUtils.concat(basePath, dataBaseFolderName);
        File databasesFolder = new File(databasesPath);
        databasesFolder.mkdirs();

        this.currentDatabaseName = new SimpleObjectProperty<>();
        this.databasesNames = FXCollections.observableArrayList();
        File[] possibleDatabases = databasesFolder.listFiles();
        for (File f: possibleDatabases) {
            String name = f.getName();
            if (name.endsWith(".json")) {
                String dbName = FilenameUtils.removeExtension(name);
                databasesNames.add(dbName);
            }
        }

    }

    public void createDB(String name) {
        this.databasesNames.add(name);
        this.currentDatabaseName.setValue(name);
        File newFile = new File(getDatabasePath(name));
        try {
            FileUtils.writeStringToFile(newFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDatabasePath(String databaseName) {
        return FilenameUtils.concat(databasesPath, databaseName + ".json");
    }

    public ObservableList<String> getDatabasesNames() {
        return databasesNames;
    }

    public String getCurrentDatabaseName() {
        return currentDatabaseName.get();
    }

    public ObjectProperty<String> currentDatabaseNameProperty() {
        return currentDatabaseName;
    }
}
