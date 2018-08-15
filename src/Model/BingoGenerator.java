package Model;

import java.lang.reflect.Array;
import java.util.*;

public class BingoGenerator {

    public static List<List<Integer>> generateBingos(int count) {
        List<Integer> options = generateOptions(90);

        HashMap<String, List<Integer>> bingos = new HashMap<>();
        while (bingos.size() < count) {
            List<Integer> option = generateSingleBingo(options);
            String key = generateBingoKey(option);
            bingos.putIfAbsent(key, option);
        }

        return  new ArrayList<List<Integer>>(bingos.values());
    }

    private static List<Integer> generateOptions(int maxValue) {
        ArrayList<Integer> options = new ArrayList<>();
        for(int i = 1; i <= maxValue; i++) {
            options.add(i);
        }
        return options;
    }

    private static List<Integer> generateSingleBingo(List<Integer> options) {
        Collections.shuffle(options);
        List<Integer> option = new ArrayList<>(options.subList(0,15));
        return option;
    }

    private static String generateBingoKey(List<Integer> opt) {
        ArrayList<Integer> option = new ArrayList<>(opt);
        option.sort((v1, v2) -> v1.compareTo(v2));

        String key = "";
        Iterator iterator = option.iterator();
        while (iterator.hasNext()) {
            key += iterator.next().toString() + "-";
        }
        return key;
    }

    /**
     * Genera una matrix que representa un bingo
     */
    public static Matrix<Integer> generateBingo() {
        Matrix<Integer> bingoCard = new Matrix<>(3, 9);
        int initDec = Helper.generateRandomInteger(0,7);

        for (int col = 0; col < bingoCard.getColumns(); col++) {
            int min = col * 10 + 1;
            int max = min + 8;

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

}
