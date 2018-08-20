package Model;

import org.json.JSONObject;

public class BingoCard {

    // MARK: - Data

    private Matrix<Integer> bingo;
    private String id;

    // MARK: - Init

    public BingoCard(String id, Matrix<Integer> bingo) {
        this.id = id;
        this.bingo = bingo;
    }

    public BingoCard(JSONObject json) {
        this.id = json.getString("id");
        this.bingo = new Matrix<Integer>(json.getJSONObject("matrix"));
    }

    // MARK: - JSON Serialziation

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("matrix", bingo.toJSONObject());
        return result;
    }

    // MARK: - Getters

    public Matrix<Integer> getBingo() {
        return bingo;
    }

    public String getId() {
        return id;
    }
}
