package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Database {

    // MARK: - Data

    private ObservableList<BingoCard> bingos;
    private ObservableList<Client> clients;

    private HashMap<String, List<String>> sells;
    private HashMap<String, Client> clientsIndex;
    private HashMap<String, BingoCard> bingosIndex;

    // MARK: - Init

    public Database(JSONArray jsonClients, JSONArray jsonBingos, JSONArray jsonSells) {
        this.clients = FXCollections.observableArrayList();
        this.clientsIndex = new HashMap<>();
        for (Object o: jsonClients) {
            Client client = new Client((JSONObject) o);
            this.clients.add(client);
            this.clientsIndex.put(client.getId(), client);
        }

        this.bingos = FXCollections.observableArrayList();
        this.bingosIndex = new HashMap<>();
        for (Object o: jsonBingos) {
            BingoCard bingo = new BingoCard((JSONObject) o);
            this.bingos.add(bingo);
            this.bingosIndex.put(bingo.getId(), bingo);
        }

        this.sells = new HashMap<>();
        for (Object o: jsonSells) {
            JSONObject sell = (JSONObject) o;
            String bingoID = sell.getString("bingo_id");
            this.sells.put(bingoID, Helper.JSONArrayToStringList(sell.getJSONArray("clients_ids")));
        }
    }

    // MARK: - JSON Serialization

    public JSONArray clientsToJSONObject() {
        Object[] temp = clients.stream().map(Client::toJSONObject).toArray();
        return new JSONArray(temp);
    }

    public JSONArray bingosToJSONObject() {
        Object[] temp = bingos.stream().map(BingoCard::toJSONObject).toArray();
        return new JSONArray(temp);
    }

    public JSONArray sellsToJSONObject() {
        Object[] temp = sells.keySet().stream().map(bingoID -> {
            JSONObject sell = new JSONObject();
            sell.put("bingo_id", bingoID);
            sell.put("clients_ids", sells.get(bingoID));
            return sell;
        }).toArray();
        return new JSONArray(temp);
    }

    // Insert

    /**
     * Dado cun conjunto de ids usados retorna un nuevo id único
     * @param currentIDs Ids actualmente en uso
     * @return Nuevo id
     */
    private String generateNewID(Set<String> currentIDs) {
        Random r = new Random();
        String newKey = "";
        do {
            newKey = "" + r.nextLong() + r.nextLong();
        } while (currentIDs.contains(newKey));
        return newKey;
    }

    /**
     * Agrega un nuevo cliente a la base de datos (No guarda los cambios)
     * @return ID del objeto registrado
     */
    public String addNewClient(String name, String lastName, String dni, String address, String telephone) {
        String newID = generateNewID(clientsIndex.keySet());
        Client newClient = new Client(newID, name, lastName, dni, address, telephone);
        this.clientsIndex.put(newID, newClient);
        this.clients.add(newClient);
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
        BingoCard newBingo = new BingoCard(newID, bingoMatrix);

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
        if (sells.containsKey(bingoID)) {
            List<String> clients = sells.get(bingoID);
            clients.add(client);
        } else {
            List<String> clients = new ArrayList<>();
            clients.add(client);
            sells.put(bingoID, clients);
        }
    }

    /**
     * Elimina una venta
     * @param bingoID ID del bingo a eliminar ventas
     */
    public void removeSellEntry(String bingoID) {
        sells.remove(bingoID);
    }

    /**
     * Elimina un cliente de una compra de bingo
     * Si el cliente es el único del cartón se elimina toda la venta
     * @param bingoID Identificador de Cartón
     * @param clientID Identificador del cliente
     */
    public void removeClientFromSell(String bingoID, String clientID) {
        List<String> clients = sells.get(bingoID);
        if (clients == null) {
            return;
        }
        if (clients.size() == 1) {
            sells.remove(bingoID);
        } else {
            clients.remove(clientID);
        }
    }

    // Getters

    public ObservableList<BingoCard> getBingos() {
        return bingos;
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public HashMap<String, List<String>> getSells() {
        return sells;
    }

    public HashMap<String, Client> getClientsIndex() {
        return clientsIndex;
    }

    public HashMap<String, BingoCard> getBingosIndex() {
        return bingosIndex;
    }
}
