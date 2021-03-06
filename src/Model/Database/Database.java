package Model.Database;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

import java.util.*;

public class Database {

    // MARK: - Data

    private ObservableList<BingoCard> bingos;
    private ObservableList<Client> clients;

    private HashMap<String, Client> clientsIndex;
    private HashMap<String, BingoCard> bingosIndex;

    // MARK: - Init

    /**
     * Crea una instancia de una base de datos
     * @param jsonClients Lista de JSONObjects con la infomración de los clientes
     * @param jsonBingos Lista de JSONObjects con la infomración de los bingos
     */
    public Database(List<JSONObject> jsonClients, List<JSONObject> jsonBingos) {
        this.clients = FXCollections.observableArrayList();
        this.clientsIndex = new HashMap<>();
        for (JSONObject json: jsonClients) {
            Client client = new Client(json);
            this.clients.add(client);
            this.clientsIndex.put(client.getId(), client);
        }

        this.bingos = FXCollections.observableArrayList();
        this.bingosIndex = new HashMap<>();
        for (JSONObject json: jsonBingos) {
            BingoCard bingo = new BingoCard(json);
            this.bingos.add(bingo);
            this.bingosIndex.put(bingo.getId(), bingo);
        }
    }

    // MARK: - Create

    /**
     * Dado cun conjunto de ids usados retorna un nuevo id único
     * @param currentIDs Ids actualmente en uso
     * @return Nuevo id
     */
    private String generateNewID(Set<String> currentIDs) {
        Random r = new Random();
        String newKey = "";
        do {
            newKey = "" + Math.abs(r.nextLong()) + Math.abs(r.nextLong());
        } while (currentIDs.contains(newKey));
        return newKey;
    }

    /**
     * Agrega un nuevo cliente a la base de datos (No guarda los cambios)
     * @return ID del objeto registrado
     */
    public String addNewClient(String name, String lastName, String dni, String address, String telephone) {
        Client newClient = new Client(null, name, lastName, dni, address, telephone);
        return addNewClient(newClient);
    }

    /**
     * Agrega un nuevo cliente a la base de datos
     * Si el objeto tiene un id no se agregará
     * @param client Objeto cliente a agregar
     * @return ID del nuevo objeto agregado
     */
    public String addNewClient(Client client) {
        if (client.getId() != null) {
            return null;
        }
        String newID = generateNewID(clientsIndex.keySet());
        client.setId(newID);

        DBManager.getInstance().saveData(client);

        this.clientsIndex.put(newID, client);
        this.clients.add(client);

        return newID;
    }

    /**
     * Agrega un nuevo bingo a la base de datos (No guarda los cambios)
     * @return ID del objeto registrado
     */
    public String addNewBingo() {
        String newID = null;
        do {
            Matrix<Integer> newBingo = BingoGenerator.generateBingo();
            newID = this.addNewBingo(newBingo);
        } while (newID == null);
        return newID;
    }

    /**
     * Agrega un nuevo bingo a la base de datos (No guarda los cambios)
     * @param bingoMatrix Matrix de Bingo a agregar
     * @return ID del objeto registrado si es que se agregó (null en caso contrario)
     */
    public String addNewBingo(Matrix<Integer> bingoMatrix) {
        for (BingoCard bingoCard: bingos) {
            if (bingoCard.getBingo().equals(bingoMatrix)) {
                return null;
            }
        }
        String newID = generateNewID(bingosIndex.keySet());
        BingoCard newBingo = new BingoCard(newID, bingoMatrix, false);

        DBManager.getInstance().saveData(newBingo);

        this.bingosIndex.put(newID, newBingo);
        this.bingos.add(newBingo);

        return newID;
    }

    /**
     * Agrega una cantidad de bingos nuevos
     * @param count Cantidad de bingoas a agregar
     * @return Lista de ids de los bingos agregados
     */
    public List<String> addNewBingos(int count) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
             result.add(this.addNewBingo());
        }
        return result;
    }

    /**
     * Agrega una venta a la base de datos
     * @param bingoID ID del bingo a vender
     * @param client ID del cliente
     */
    public void addNewSell(String bingoID, String client) {
        BingoCard bingo = bingosIndex.get(bingoID);
        bingo.addOwner(client);
        DBManager.getInstance().saveData(bingo);
    }

    // MARK: - Deletions

    /**
     * Elimina un cliente de una compra de bingo
     * Si el cliente es el único del cartón se elimina toda la venta
     * @param bingoID Identificador de Cartón
     * @param clientID Identificador del cliente
     */
    public void removeClientFromSell(String bingoID, String clientID) {
        BingoCard bingo = bingosIndex.get(bingoID);
        bingo.removeOwner(clientID);
        DBManager.getInstance().saveData(bingo);
    }

    /**
     * Elimina un cliente dado su ID (No guarda los cambios)
     */
    public void removeClient(String clientID) {
        DBManager dbManager = DBManager.getInstance();

        Client client = clientsIndex.remove(clientID);
        clients.remove(client);
        dbManager.deleteDataBaseItem(client);

        retrieveClientBingsCards(clientID).forEach( bingo -> {
            bingo.removeOwner(clientID);
            dbManager.deleteDataBaseItem(bingo);
        });
    }

    /**
     * Elimina un bingo dado su ID (No guarda los cambios)
     */
    public void removeBingo(String bingoID) {
        BingoCard bingoCard = bingosIndex.remove(bingoID);
        bingos.remove(bingoCard);
        DBManager.getInstance().deleteDataBaseItem(bingoCard);
    }

    // MARK: - Retrieving

    /**
     * Dado un id de cliente retorna los bingos que le pertenece
     * @param clientID Id de cliente
     * @return Lista de bingos del cliente
     */
    public ObservableList<BingoCard> retrieveClientBingsCards(String clientID) {
        ObservableList<BingoCard> bingos = FXCollections.observableArrayList();
        bingosIndex.forEach((id, bingo) -> {
            if (bingo.getOwnersIds().contains(clientID)) {
                bingos.add(bingo);
            }
        });
        return bingos;
    }

    public ObservableList<Client> retrieveBingoOwners(String bingoID) {
        return bingosIndex.get(bingoID).getOwners();
    }

    /**
     * Obtiene una lista de bingos adquiridos
     */
    public ObservableList<BingoCard> retrieveSelledBingos() {
        ObservableList<BingoCard> result = FXCollections.observableArrayList();
        for (BingoCard bingoCard: getBingos()) {
            if (bingoCard.isOwned()) {
                result.add(bingoCard);
            }
        }
        return result;
    }

    // Getters

    public ObservableList<BingoCard> getBingos() {
        return bingos;
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public HashMap<String, Client> getClientsIndex() {
        return clientsIndex;
    }

    public HashMap<String, BingoCard> getBingosIndex() {
        return bingosIndex;
    }
}
