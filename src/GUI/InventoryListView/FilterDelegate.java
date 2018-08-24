package GUI.InventoryListView;

public interface FilterDelegate<T> {

    boolean shouldIncoude(T element, String pattern);

}