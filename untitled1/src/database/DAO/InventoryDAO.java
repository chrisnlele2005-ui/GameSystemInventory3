package database.DAO;

import services.InventoryService;
import model.Item;

import java.util.List;

public class InventoryDAO {
    private ItemDAO itemDAO = new ItemDAO();

    public void saveInventory(InventoryService inventory) {
        for (Item item : inventory.getInventory()) {
            itemDAO.saveItem(item);
        }
    }

    public void loadInventory(InventoryService inventory) {
        List<Item> items = itemDAO.getAllItems();
        for (Item item : items) {
            inventory.addItem(item);
        }
    }
}

