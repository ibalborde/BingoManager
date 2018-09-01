package Model.Database;

import Model.Database.DBManagable;
import Model.Helper;
import Model.Matrix;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BingoCard implements DBManagable {

    // MARK: - Data

    private Matrix<Integer> bingo;
    private StringProperty id;
    private ObservableList<String> owners;
    private BooleanProperty payed;

    private BooleanProperty owned;

    // MARK: - Init

    /**
     * Crea una instancia de tarjeta de bingo
     * @param id Identificador único de bingo
     * @param bingo Matriz con los elementos del bingo
     * @param owners Propietarios de la tarjeta
     * @param selled Indica si fue pagado
     */
    public BingoCard(String id, Matrix<Integer> bingo, ObservableList<String> owners, boolean selled) {
        this.id = new SimpleStringProperty(id);
        this.bingo = bingo;
        this.owners = owners;
        this.owned = new SimpleBooleanProperty(isOwned());
        this.payed = new SimpleBooleanProperty(false);
    }

    /**
     * Crea una instancia de tarjeta de bingo
     * @param id Identificador único de bingo
     * @param bingo Matriz con los elementos del bingo
     * @param payed Indica si fue pagado
     */
    public BingoCard(String id, Matrix<Integer> bingo, boolean payed) {
        this(id, bingo, FXCollections.observableArrayList(), payed);
    }

    public BingoCard(JSONObject json) {
        this.id = new SimpleStringProperty(json.getString("id"));
        this.bingo = new Matrix<Integer>(json.getJSONObject("matrix"));
        this.owners = FXCollections.observableArrayList();
        json.getJSONArray("owners").forEach(it -> owners.add(it.toString()));
        this.owned = new SimpleBooleanProperty(isOwned());
        this.payed = new SimpleBooleanProperty(json.getBoolean("payed"));
    }

    // MARK: - Interface

    public void addOwner(String newOwner) {
        Helper.addUniqueElementToList(newOwner, this.owners);
        this.owned.set(isOwned());
    }

    public void removeOwner(String owner) {
        this.owners.remove(owner);
        this.owned.set(isOwned());
        if (owners.isEmpty()) {
            this.payed.set(false);
        }
    }

    public boolean isOwner(String possibleOwner) {
        return this.owners.contains(possibleOwner);
    }

    // MARK: - Getters

    public Matrix<Integer> getBingo() {
        return bingo;
    }

    public ObservableList<String> getOwnersIds() {
        return owners;
    }

    public ObservableList<Client> getOwners() {
        Database db = DBManager.getInstance().getCurrentDatabase();
        ObservableList<Client> result = FXCollections.observableArrayList();
        getOwnersIds().forEach(id -> {
            result.add(db.getClientsIndex().get(id));
        });
        return result;
    }

    public StringProperty idProperty() {
        return id;
    }

    public boolean isOwned() {
        return !owners.isEmpty();
    }

    public BooleanProperty ownedProperty() {
        return owned;
    }

    public boolean isPayed() {
        return payed.get();
    }

    public BooleanProperty payedProperty() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed.set(payed);
    }

    // MARK: - DBManagable

    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("id", id.get());
        result.put("matrix", bingo.toJSONObject());
        result.put("owners", owners);
        result.put("payed", payed.get());
        return result;
    }

    @Override
    public String getId() {
        return this.id.get();
    }

}
