package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Matrix<Element> {

    // MARK: - Data
    private int rows, columns;
    private ArrayList<Element> data;

    // MARK: - Init

    /**
     * Crea una matriz
     * @param rows Cantidad de filas de la matriz
     * @param cols Cantidad de columnas de la matriz
     */
    public Matrix(int rows, int cols) {
        assert rows > 0;
        assert cols > 0;

        this.rows = rows;
        this.columns = cols;

        this.data = new ArrayList<>();
        for (int i = rows * cols; i > 0; i--) {
            this.data.add(null);
        }
    }

    /**
     * Crea una matriz
     * @param json Objeto json de donde se extraerá la información
     */
    public Matrix (JSONObject json) {
        int rows = json.getInt("rows");
        int cols = json.getInt("columns");

        assert rows > 0;
        assert cols > 0;

        this.rows = rows;
        this.columns = cols;
        this.data = new ArrayList<>();

        for (Object o: json.getJSONArray("values")) {
            this.data.add((Element) o);
        }
    }

    // MARK - Work with items

    public Element get(int row, int column) {
        assert row >= 0 && row < this.rows;
        assert column >= 0 && column < this.columns;

        return this.data.get(row * getColumns() + column);
    }

    public Element set(int row, int column, Element e) {
        assert row >= 0 && row < this.rows;
        assert column >= 0 && column < this.columns;

        return this.data.set(row * getColumns() + column, e);
    }

    // MARK: - Getters

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    // MARK: - Internal

    @Override
    public boolean equals(Object obj) {
        Matrix<Element> other;
        try {
            other = (Matrix<Element>) obj;
        } catch (Exception e) {
            return false;
        }

        if (this.getRows() != other.getRows()) {
            return false;
        }

        if (this.getColumns() != other.getColumns()) {
            return false;
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (this.get(row, col) != other.get(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }

    // MARK: - Interface

    public void printMatrix() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                System.out.print(get(row, col) + "  ");
            }
            System.out.println();
        }
    }

    // MARK: - JSON Generation

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("rows", this.getRows());
        result.put("columns", this.getColumns());

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                result.append("values", get(row, col));
            }
        }
        return result;
    }


}
