package Model;

import java.util.ArrayList;

public class Matrix<Element> {

    private int rows, columns;
    private ArrayList<ArrayList<Element>> data;

    public Matrix(int rows, int cols) {
        assert rows > 0;
        assert cols > 0;

        this.rows = rows;
        this.columns = cols;

        this.data = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            ArrayList<Element> temp = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                temp.add(null);
            }
            this.data.add(temp);
        }
    }

    public Element get(int row, int column) {
        assert row >= 0 && row < this.rows;
        assert column >= 0 && column < this.columns;

        return this.data.get(row).get(column);
    }

    public Element set(int row, int column, Element e) {
        assert row >= 0 && row < this.rows;
        assert column >= 0 && column < this.columns;

        return this.data.get(row).set(column, e);
    }


    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

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

    public void printMatrix() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                System.out.print(get(row, col) + "  ");
            }
            System.out.println();
        }
    }
}
