package GUI;

import GUI.InventoryListView.FilterDelegate;
import GUI.InventoryListView.InventoryListView;
import Model.Database.BingoCard;
import Model.Database.Client;
import Model.Helper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.List;

public class InventoryListViewSetter {


    /**
     * Delegate para filtrar tabla de clientes
     */
    private static FilterDelegate<Client> clientFilterDelegate = new FilterDelegate<Client>() {

        @Override
        public boolean shouldIncoude(Client element, String pattern) {
            return true;
        }

    };

    /**
     * Prapara la tabla una clientes
     */
    public static void prepareClientInventory(InventoryListView<Client> inventoryListViewController) {
        inventoryListViewController.setFiltrable(clientFilterDelegate);

        TableView<Client> table = inventoryListViewController.getTableView();

        TableColumn<Client, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(it -> it.getValue().idProperty());

        TableColumn<Client, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(it -> it.getValue().nameProperty());

        TableColumn<Client, String> lastNameCol = new TableColumn<>("Apellido");
        lastNameCol.setCellValueFactory(it -> it.getValue().lastNameProperty());

        TableColumn<Client, String> addressCol = new TableColumn<>("Dirección");
        addressCol.setCellValueFactory(it -> it.getValue().addressProperty());

        TableColumn<Client, String> telephoneCol = new TableColumn<>("Teléfono");
        telephoneCol.setCellValueFactory(it -> it.getValue().telephoneProperty());

        TableColumn<Client, String> dniCol = new TableColumn<>("Dni");
        dniCol.setCellValueFactory(it -> it.getValue().dniProperty());

        table.getColumns().addAll(nameCol, lastNameCol, dniCol, telephoneCol, addressCol, idCol);
        table.widthProperty().addListener((obs, o, n) -> {
            Double newWidth = n.doubleValue() / 6;
            idCol.setPrefWidth(newWidth);
            nameCol.setPrefWidth(newWidth);
            lastNameCol.setPrefWidth(newWidth);
            addressCol.setPrefWidth(newWidth);
            telephoneCol.setPrefWidth(newWidth);
            dniCol.setPrefWidth(newWidth);
        });
    }

    private static FilterDelegate<BingoCard> bingosFilterDelegate = new FilterDelegate<BingoCard>() {

        @Override
        public boolean shouldIncoude(BingoCard element, String pattern) {
            List<String> numbers = Arrays.asList(pattern.split("\\s+"));
            return Helper.matrixContains(element.getBingo(), numbers) ||
                    Helper.match(pattern, element.getId());
        }

    };

    /**
     * Prapara la tabla una bingos
     */
    public static void prepareBingosInventory(InventoryListView<BingoCard> inventoryListViewController) {
        inventoryListViewController.setFiltrable(bingosFilterDelegate);

        TableView<BingoCard> table = inventoryListViewController.getTableView();

        // Sell Status Column
        TableColumn<BingoCard, Boolean> bingoCardStatusColumn = new TableColumn<>("Vendido");
        bingoCardStatusColumn.setCellValueFactory(
                new PropertyValueFactory<BingoCard, Boolean>("owned")
        );
        bingoCardStatusColumn.setCellFactory(e -> new CheckBoxTableCell<>());

        // Bingo ID Column
        TableColumn<BingoCard, String> bingoIDColumn = new TableColumn<>("ID");
        bingoIDColumn.setCellValueFactory(
                new PropertyValueFactory<BingoCard, String>("id")
        );
        table.widthProperty().addListener((obs, o, width) -> {
            double w = width.doubleValue();
            bingoCardStatusColumn.setPrefWidth(w * 0.1);
            bingoIDColumn.setPrefWidth(w * 0.9);
        });

        table.getColumns().addAll(bingoCardStatusColumn, bingoIDColumn);
    }

}
