package Model;

import org.json.JSONObject;

public class Client {

    private String id;

    private String name;
    private String lastName;
    private String dni;
    private String address;
    private String telephone;

    // MARK: - Init

    public Client(JSONObject json) {
        this.id = json.getString("id");
        this.name = json.getString("name");
        this.lastName = json.getString("last_name");
        this.dni = json.getString("dni");
        this.address = json.getString("address");
        this.telephone = json.getString("telephone");
    }

    public Client(String id, String name, String lastName, String dni, String address, String telephone) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.address = address;
        this.telephone = telephone;
    }

    // MARK: - Serialzation

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("last_name", this.lastName);
        json.put("dni", this.dni);
        json.put("address", this.address);
        json.put("telephone", this.telephone);
        return json;
    }

    // MARK: - Getters


    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDni() {
        return dni;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getId() {
        return id;
    }
}
