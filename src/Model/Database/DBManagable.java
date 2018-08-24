package Model.Database;

import javafx.beans.property.StringProperty;
import org.json.JSONObject;

public interface DBManagable {

    public JSONObject toJSONObject();

    public StringProperty getId();
}
