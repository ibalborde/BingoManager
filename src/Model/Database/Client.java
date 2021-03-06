package Model.Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

public class Client implements DBManagable{

    private StringProperty id;
    private StringProperty fullName;

    private StringProperty name;
    private StringProperty lastName;
    private StringProperty dni;
    private StringProperty address;
    private StringProperty telephone;

    // MARK: - Init

    public Client(JSONObject json) {
        this(
            json.getString("id"),
            json.getString("name"),
            json.getString("last_name"),
            json.getString("dni"),
            json.getString("address"),
            json.getString("telephone")
        );
    }

    public Client(String id, String name, String lastName, String dni, String address, String telephone) {
        this.id = new SimpleStringProperty(id);
        this.fullName = new SimpleStringProperty(name + " " + lastName);
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.dni = new SimpleStringProperty(dni);
        this.address = new SimpleStringProperty(address);
        this.telephone = new SimpleStringProperty(telephone);

        // Agrega listener para cuando se modifica el nombre o apellido
        ChangeListener cl = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fullName.setValue(getName() + " " + getLastName());
            }
        };
        this.name.addListener(cl);
        this.lastName.addListener(cl);
    }

    // MARK: - Getters && Setters

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getDni() {
        return dni.get();
    }

    public StringProperty dniProperty() {
        return dni;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        if (this.getId() == null) {
            this.id.set(id);
        }
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setDni(String dni) {
        this.dni.set(dni);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    // MARK: - DBManagable

    public String getId() {
        return id.get();
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", this.id.get());
        json.put("name", this.name.get());
        json.put("last_name", this.lastName.get());
        json.put("dni", this.dni.get());
        json.put("address", this.address.get());
        json.put("telephone", this.telephone.get());
        return json;
    }
}
