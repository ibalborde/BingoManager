package Model;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.*;

public class BingoGenerator {

    /**
     * Genera una lista de Bingos
     * @param count Cantidad de bingos a generar
     */
    public static List<Matrix<Integer>> generateBingos(int count) {
        ArrayList<Matrix<Integer>> bingos = new ArrayList<>();
        while (bingos.size() < count) {
            Helper.addUniqueElementToList(generateBingo(), bingos);
        }
        return bingos;
    }

    /**
     * Genera una matriz que representa un bingo
     */
    public static Matrix<Integer> generateBingo() {
        Matrix<Integer> bingoCard = new Matrix<>(3, 9);

        for (int col = 0; col < bingoCard.getColumns(); col++) {
            int min = getMinBoundForCol(col);
            int max = getMaxBoundForCol(col);

            List<Integer> numbers = Helper.generateRandomUniqueNumbersList(min, max, 3);
            Collections.sort(numbers);

            for (int row = 0; row < bingoCard.getRows(); row++) {
                bingoCard.set(row, col, numbers.get(row));
            }
        }

        // Elimina el contenido de algunos campos
        List<Pair<Integer, Integer>> indexes = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                indexes.add(new Pair<>(row, col));
            }
        }




        List<Integer> itemsInRow = new ArrayList<>(Collections.nCopies(3,9));
        List<Integer> itemsInCol = new ArrayList<>(Collections.nCopies(9,3));
        int inRow = 0;
        int inCol = 0;
        while (indexes.size() > 15) {
            int i = Helper.generateRandomInteger(0,indexes.size() - 1);
            Pair<Integer, Integer> pair = indexes.get(i);

            int row = pair.getKey();
            int col = pair.getValue();

            inRow = itemsInRow.get(row);
            inCol = itemsInCol.get(col);


            if (inRow > 5 && inCol > 1) {
                bingoCard.set(row, col, null);
                indexes.remove(i);
                itemsInRow.set(row, inRow - 1);
                itemsInCol.set(col, inCol - 1);
            }

        }

        return bingoCard;
    }

    // MARK: - Private Methods

    /**
     * Retorna el valor mímimo aceptable para una columna
     * @param col columna a trabajar
     */
    private static int getMinBoundForCol(int col) {
        return col == 0 ? 1 : col * 10;
    }

    /**
     * Retorna el valor máximo aceptable para una columna
     * @param col columna a trabajar
     */
    private static int getMaxBoundForCol(int col) {
        return col == 8 ? 90 : col * 10 + 9;
    }

}
