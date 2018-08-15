package Model;

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
        for (int row = 0; row < bingoCard.getRows(); row++) {
            List<Integer> randomIndexes = Helper.generateRandomUniqueNumbersList(0,8, 4);
            Iterator iterator = randomIndexes.iterator();
            while (iterator.hasNext()) {
                Integer col = (Integer) iterator.next();
                bingoCard.set(row, col, null);
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
