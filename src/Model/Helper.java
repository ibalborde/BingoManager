package Model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

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

        if (list.contains(element)) {
            return false;
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

    /**
     * Convierte un JSONArray a una lista de Strings
     */
    public static List<String> JSONArrayToStringList(JSONArray arr) {
        if (arr == null) {
            return null;
        }
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            result.add(arr.optString(i));
        }
        return result;
    }

    // MARK: - String Matching

    /**
     * Comprueba si dos Strings matchean
     * @param pattern String base para el match
     * @param text Texto a comprobar
     * @return True si matchea
     */
    public static boolean match(String pattern, String text) {
        pattern = StringUtils.stripAccents(pattern.trim()).toLowerCase();
        text = StringUtils.stripAccents(text.trim()).toLowerCase();

        int i;
        int j = 0;

        for (i = 0; i < pattern.length() && j < text.length() && j >= 0 && i >= 0; i++) {
            char pChar = pattern.charAt(i);
            if (pChar == ' ') {
                i = consumeChar(' ', pattern, i) - 1;
                j = consumeChar(' ', text, j);
                continue;
            }
            j = consumeChar(pChar, text, j);
        }
        return i >= pattern.length();
    }

    /**
     * Retorna el siguiente índice luego de un caracter
     * @param character caracter patrón
     * @param string String a procesar
     * @param index Índice del cual partir
     * @return Siguiente índice luego del caracter encontrado. Puede superar el rango del String o ser negativo
     */
    private static int consumeChar(char character, String string, int index) {
        int i = string.indexOf(character, index);
        if (i == -1) {
            return -1;
        }
        if (character != ' ') {
            return i + 1;
        }
        while (i + 1 < string.length() && string.charAt(i+1) == ' ') {
            i += 1;
        }
        return i + 1;
    }

    /**
     * Comprueba si una lista de Strings está en una matríz
     * @param matrix Matriz a comprobar
     * @param patterns Lista de patrones a utilizar
     */
    public static <T> boolean matrixContains(Matrix<T> matrix, List<String> patterns) {
        Set<String> content = new HashSet<>();
        for (int r = 0; r < matrix.getRows(); r++){
            for (int c = 0; c < matrix.getColumns(); c++){
                T item = matrix.get(r, c);
                if (item != null) {
                    content.add(item.toString());
                }
            }
        }
        return content.containsAll(patterns);
    }
}
