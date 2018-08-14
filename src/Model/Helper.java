package Model;

import java.util.*;

public class Helper {

    /**
     * Agrega un elemento a una lista si este no está presente
     * @param element Elemento a agregar
     * @param list lista a la cual agregar el nuevo elemento
     * @return true si el elemento fue agregado
     */
    public static <T> boolean addUniqueElementToList(T element, List<T> list) {
        if (list == null) {
            return false;
        }

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            T tempElement = (T) iterator.next();
            if (tempElement.equals(element)) {
                return false;
            }
        }

        list.add(element);
        return true;
    }

    /**
     * Genera un numero pseudoaleatorio entre dos valores
     * @param lowBound Valor mínimo (inclusivo)
     * @param highBound Valor máximo (inclusivo)
     * @return valor presudoaleatorio generado
     */
    public static int generateRandomInteger(int lowBound, int highBound) {
        Random r = new Random();
        return lowBound + r.nextInt((highBound - lowBound) + 1);
    }

    /**
     * Genera una lista de valores pseudoaleatorios
     * @param lowBound Valor mínimo (inclusivo)
     * @param highBound Valor máximo (inclusivo)
     * @param count cantidad de elementos que debe tener la lista (debe ser positivo)
     * @return lista generada
     */
    public static List<Integer> generateRandomUniqueNumbersList(int lowBound, int highBound, int count) {
        assert count > 0;
        assert lowBound < highBound;

        List<Integer> result = new ArrayList<>();
        while (result.size() < count) {
            addUniqueElementToList(generateRandomInteger(lowBound, highBound) , result);
        }
        return result;
    }
}
