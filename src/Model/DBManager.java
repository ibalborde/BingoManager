package Model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;


public class DBManager {

    public static final String folderName = "BingoManager";
    public static final String dataBaseFolderName = "databases";

    public static final String dbExtension = "db";
    public static final String clientsDBName = "clients";
    public static final String sellsDBName = "sells";
    public static final String cardsDBName = "bingos";

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
    private Database currentDatabase;

    // MARK: - Init

    /**
     * Crea una instancia de la clase
     */
    private DBManager() {
        this.createEnvironment();
        this.loadDatabasesNames();
    }

    /**
     * Crea el entorno de trabajo
     */
    private void createEnvironment() {
        String home = System.getProperty("user.home");

        this.basePath = FilenameUtils.concat(home, folderName);
        File baseFolder = new File(basePath);
        baseFolder.mkdirs();

        this.databasesPath = FilenameUtils.concat(basePath, dataBaseFolderName);
        File databasesFolder = new File(databasesPath);
        databasesFolder.mkdirs();
    }

    /**
     * Carga la lista de bases de datos
     */
    private void loadDatabasesNames() {
        this.currentDatabaseName = new SimpleObjectProperty<>();
        this.databasesNames = FXCollections.observableArrayList();

        File[] possibleDatabases = new File(databasesPath).listFiles();
        for (File f: possibleDatabases) {
            String name = f.getName();
            if (name.endsWith("." + dbExtension)) {
                String dbName = FilenameUtils.removeExtension(name);
                databasesNames.add(dbName);
            }
        }
    }

    // MARK: - Interface

    /**
     * Crea una nueva base de datos si esta no existe
     * @param name Nombre de la base de datos a agregar
     */
    public void createDB(String name) {
        if (databasesNames.contains(name)) {
            return;
        }
        try {
            new File(getDatabasePath(name)).mkdirs();
            FileUtils.writeStringToFile(new File(getClientsDBFilePath(name)), "[]");
            FileUtils.writeStringToFile(new File(getBingosDBFilePath(name)), "[]");
            FileUtils.writeStringToFile(new File(getSellsDBFilePath(name)), "[]");

            this.databasesNames.add(name);
            this.currentDatabaseName.setValue(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina una base de datos del sistema
     * @param name Nombre de la base de datos
     */
    public void deleteDataBase(String name) {
        try {
            FileUtils.deleteDirectory(new File(getDatabasePath(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una base de datos
     * @param name Nombre de la base de datos a cargar
     */
    public void loadDatabase(String name) throws IOException {
        String clientsDBPath = getClientsDBFilePath(name);
        String clientsContent = FileUtils.readFileToString(new File(clientsDBPath));
        JSONArray clientsArray = new JSONArray(clientsContent);

        String bingosDBPath = getBingosDBFilePath(name);
        String bingosContent = FileUtils.readFileToString(new File(bingosDBPath));
        JSONArray bingosArray = new JSONArray(bingosContent);

        String sellsDBPath = getSellsDBFilePath(name);
        String sellsContent = FileUtils.readFileToString(new File(sellsDBPath));
        JSONArray sellsArray = new JSONArray(sellsContent);

        this.currentDatabase = new Database(clientsArray, bingosArray, sellsArray);
        this.currentDatabaseName.setValue(name);
    }

    // MARK: - Saving Data

    /**
     * Guarda toda la información de la base de datos de clientes
     */
    public void saveClientsDB() {
        String path = getClientsDBFilePath(currentDatabaseName.get());
        String content = currentDatabase.clientsToJSONObject().toString();

        try {
            FileUtils.writeStringToFile(new File(path), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda toda la información de la base de datos de bingos
     */
    public void saveBingosDB() {
        String path = getBingosDBFilePath(currentDatabaseName.get());
        String content = currentDatabase.bingosToJSONObject().toString();

        try {
            FileUtils.writeStringToFile(new File(path), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda toda la información de la base de datos de ventas
     */
    public void saveSellsDB() {
        String path = getSellsDBFilePath(currentDatabaseName.get());
        String content = currentDatabase.sellsToJSONObject().toString();

        try {
            FileUtils.writeStringToFile(new File(path), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda toda la información de todas las bases de datos
     */
    public void saveAll() {
        this.saveClientsDB();
        this.saveBingosDB();
        this.saveSellsDB();
    }

    // MARK: - Internal Getters

    public String getDatabasePath(String databaseName) {
        return FilenameUtils.concat(databasesPath,  databaseName + "." + dbExtension);
    }

    public String getClientsDBFilePath(String databaseName) {
        String dbPath = getDatabasePath(databaseName);
        return FilenameUtils.concat(dbPath, clientsDBName + ".json");
    }

    public String getBingosDBFilePath(String databaseName) {
        String dbPath = getDatabasePath(databaseName);
        return FilenameUtils.concat(dbPath, cardsDBName + ".json");
    }

    public String getSellsDBFilePath(String databaseName) {
        String dbPath = getDatabasePath(databaseName);
        return FilenameUtils.concat(dbPath, sellsDBName + ".json");
    }

    // MARK: - Public Getters

    public String getCurrentDatabaseName() {
        return currentDatabaseName.get();
    }

    public ObjectProperty<String> currentDatabaseNameProperty() {
        return currentDatabaseName;
    }

    public ObservableList<String> getDatabasesNames() {
        return databasesNames;
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }
}
