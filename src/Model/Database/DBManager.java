package Model.Database;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DBManager {

    public static final String folderName = "BingoManager";
    public static final String dataBaseFolderName = "databases";

    public static final String dbExtension = "db";
    public static final String clientsDBName = "clients";
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

    private ObjectProperty<String> lastSetDatabaseName;
    private ObservableList<String> databasesNames;

    private ObjectProperty<String> currentDatabaseName;
    private ObjectProperty<Database> currentDatabase;

    // MARK: - Init

    /**
     * Crea una instancia de la clase
     */
    private DBManager() {
        this.createEnvironment();
        this.setupProperties();
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
     * Configura las propiedades para manejar e interactuar con las bases de datos
     */
    private void setupProperties() {
        this.lastSetDatabaseName = new SimpleObjectProperty<>();
        this.currentDatabaseName = new SimpleObjectProperty<>();
        this.currentDatabase = new SimpleObjectProperty<>();
        this.databasesNames = FXCollections.observableArrayList();

        loadDatabasesNames();

        currentDatabaseName.addListener((obs, o, n) -> {
            if (n == null) {
                currentDatabase.setValue(null);
            } else {
                try {
                    loadDatabase(n);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    /**
     * Carga la lista de bases de datos
     */
    private void loadDatabasesNames() {
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
     * @return True si se ha creado la base de datos
     */
    public boolean createDB(String name) {
        if (databasesNames.contains(name)) {
            return false;
        }

        String dbPath = getDatabasePath(name);
        String clientsDBPath = getClientsDBFolderPath(dbPath);
        String bingosDBPath = getBingosDBFilderPath(dbPath);

        new File(dbPath).mkdirs();
        new File(clientsDBPath).mkdirs();
        new File(bingosDBPath).mkdirs();

        this.databasesNames.add(name);
        this.lastSetDatabaseName.setValue(name);
        return true;
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
        String dbPath = getDatabasePath(name);
        String clientsDBPath = getClientsDBFolderPath(dbPath);
        String bingosDBPath = getBingosDBFilderPath(dbPath);

        List<JSONObject> clients = new ArrayList<>();
        for (File file: new File(clientsDBPath).listFiles()) {
            if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
                continue;
            }

            try {
                String content = FileUtils.readFileToString(file);
                clients.add(new JSONObject(content));
            } catch (Exception e) {
                System.out.println("Error al leer el cliente " + file.getName());
            }
        }

        List<JSONObject> bingos = new ArrayList<>();
        for (File file: new File(bingosDBPath).listFiles()) {
            if (!FilenameUtils.getExtension(file.getName()).equals("json")) {
                continue;
            }
            try {
                String content = FileUtils.readFileToString(file);
                bingos.add(new JSONObject(content));
            } catch (Exception e) {
                System.out.println("Error al leer el bingo " + file.getName());
            }
        }

        this.currentDatabase.setValue(new Database(clients, bingos));
    }

    // MARK: - Saving Data

    private String getDBItemPath(DBManagable item) {
        String dbPath = getCurrentDatabasePath();

        String subDBPath;
        if (item instanceof Client) {
            subDBPath = getClientsDBFolderPath(dbPath);
        }
        else if (item instanceof BingoCard) {
            subDBPath = getBingosDBFilderPath(dbPath);
        }
        else {
            return null;
        }

        return getDatabaseItem(subDBPath, item.getId().get());
    }

    /**
     * Guarda información en la base de datos
     * @param item Item a guardar
     */
    public void saveData(DBManagable item) {
        String path = getDBItemPath(item);
        System.out.println(path);
        if (path == null) {
            return;
        }

        String content = item.toJSONObject().toString();

        try {
            FileUtils.writeStringToFile(new File(path), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param item
     */
    public void deleteDataBaseItem(DBManagable item) {
        Database db = getCurrentDatabase();
        if (db == null) {
            return;
        }

        String path = getDBItemPath(item);
        if (path == null) {
            return;
        }
        try {
            new File(path).delete();
        } catch (Exception e) {}
    }

    // MARK: - Internal Getters

    /**
     * Dado el nombre de una base dedatos retorna su ruta
     * @param databaseName Nombre de la base de datos
     */
    private String getDatabasePath(String databaseName) {
        return FilenameUtils.concat(databasesPath,  databaseName + "." + dbExtension);
    }

    /**
     * Retorna la ruta de la base de datos actual
     */
    private String getCurrentDatabasePath() {
        return getDatabasePath(currentDatabaseName.get());
    }

    // ----

    /**
     * Retorna la ruta de la carpeta contenedora de
     * la base de datos de clientes
     * @param databasePath Ruta de la base de datos
     */
    private String getClientsDBFolderPath(String databasePath) {
        return FilenameUtils.concat(databasePath, clientsDBName);
    }

    /**
     * Retorna la ruta de la carpeta contenedora de
     * la base de datos de bingos
     * @param databasePath Ruta de la base de datos
     */
    private String getBingosDBFilderPath(String databasePath) {
        return FilenameUtils.concat(databasePath, cardsDBName);
    }

    // ----

    /**
     * Genera la ruta de un item de la base de datos dada la categoría
     * @param subDatabasePath Ruta de la sub-base de datos
     * @param id Identificador del elemento
     */
    private String getDatabaseItem(String subDatabasePath, String id) {
        return FilenameUtils.concat(subDatabasePath, id + ".json");
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
        return currentDatabase.get();
    }

    public ObjectProperty<Database> currentDatabaseProperty() {
        return currentDatabase;
    }

    public String getLastSetDatabaseName() {
        return lastSetDatabaseName.get();
    }

    public ObjectProperty<String> lastSetDatabaseNameProperty() {
        return lastSetDatabaseName;
    }
}
