package Model.Database;

import Model.Database.DBManagable;
import Model.Helper;
import Model.Matrix;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BingoCard implements DBManagable {

    // MARK: - Data

    private Matrix<Integer> bingo;
    private StringProperty id;
    private List<String> owners;

    private BooleanProperty owned;

    // MARK: - Init

    /**
     * Crea una instancia de tarjeta de bingo
     * @param id Identificador único de bingo
     * @param bingo Matriz con los elementos del bingo
     * @param owners Propietarios de la tarjeta
     */
    public BingoCard(String id, Matrix<Integer> bingo, List<String> owners) {
        this.id = new SimpleStringProperty(id);
        this.bingo = bingo;
        this.owners = owners;
        this.owned = new SimpleBooleanProperty(isOwned());
    }

    /**
     * Crea una instancia de tarjeta de bingo
     * @param id Identificador único de bingo
     * @param bingo Matriz con los elementos del bingo
     */
    public BingoCard(String id, Matrix<Integer> bingo) {
        this(id, bingo, new ArrayList<>());
    }

    public BingoCard(JSONObject json) {
        this.id = new SimpleStringProperty(json.getString("id"));
        this.bingo = new Matrix<Integer>(json.getJSONObject("matrix"));
        this.owners = new ArrayList<>();
        json.getJSONArray("owners").forEach(it -> owners.add(it.toString()));
        this.owned = new SimpleBooleanProperty(isOwned());
    }

    // MARK: - Interface

    public void addOwner(String newOwner) {
        Helper.addUniqueElementToList(newOwner, this.owners);
        this.owned.set(isOwned());
    }

    public void removeOwner(String owner) {
        this.owners.remove(owner);
        this.owned.set(isOwned());
    }

    public boolean isOwner(String possibleOwner) {
        return this.owners.contains(possibleOwner);
    }

    // MARK: - Getters

    public Matrix<Integer> getBingo() {
        return bingo;
    }

    public List<String> getOwners() {
        return owners;
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

    // MARK: - DBManagable

    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("id", id.get());
        result.put("matrix", bingo.toJSONObject());
        result.put("owners", owners);
        return result;
    }

    @Override
    public StringProperty getId() {
        return this.id;
    }

}
