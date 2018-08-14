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

}
