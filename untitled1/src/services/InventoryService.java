package services;

import database.DAO.InventoryDAO;
import model.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    private final double MAX_WEIGHT = 50.0;
    private int maxSlots = 32;

    private List<Item> inventory = new ArrayList<>();
    private InventoryDAO inventoryDAO = new InventoryDAO();

    // ✅ Hent hele inventory
    public List<Item> getInventory() {
        return inventory;
    }

    // ✅ Maksimal vægt-kapacitet (bruges til UI/statistik)
    public double getMaxWeight() {
        return MAX_WEIGHT;
    }

    // ✅ Beregn samlet vægt
    public double getCurrentWeight() {
        double total = 0;
        for (Item item : inventory) {
            total += item.getWeight() * item.getQuantity();
        }
        return total;
    }

    // ✅ Tjek om der er plads til nyt item
    private boolean canAdd(Item item) {
        if (inventory.size() >= maxSlots) {
            System.out.println("Inventory er fuld (slots).");
            return false;
        }

        if (getCurrentWeight() + (item.getWeight() * item.getQuantity()) > MAX_WEIGHT) {
            System.out.println("Inventory er for tung.");
            return false;
        }

        return true;
    }

    // ✅ Tilføj item
    public boolean addItem(Item item) {
        // Først: håndter stakbare items (Consumables) så de bruger samme slot
        if (item instanceof model.Consumable) {
            for (Item existing : inventory) {
                if (existing instanceof model.Consumable
                        && existing.getName().equalsIgnoreCase(item.getName())) {

                    // Beregn ekstra vægt ved at tilføje til eksisterende stack
                    double addedWeight = item.getWeight() * item.getQuantity();
                    if (getCurrentWeight() + addedWeight > MAX_WEIGHT) {
                        System.out.println("Inventory er for tung (stacking consumable).");
                        return false;
                    }

                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    System.out.println("Consumable stack opdateret: " + existing.getName()
                            + " x" + existing.getQuantity());
                    return true;
                }
            }
        }

        // Hvis vi ikke kunne stacke, tilføjes som nyt item hvis der er plads
        if (!canAdd(item)) {
            return false;
        }

        inventory.add(item);
        System.out.println("Item tilføjet: " + item.getName());
        return true;
    }

    // ✅ Fjern item via ID (matcher ConsoleUI)
    public boolean removeById(int id) {
        for (Item item : inventory) {
            if (item.getId() == id) {
                inventory.remove(item);
                System.out.println("Item fjernet: " + item.getName());
                return true;
            }
        }
        return false;
    }

    // ✅ Gem i databasen
    public void saveToDB() {
        inventoryDAO.saveInventory(this);
    }

    // ✅ Load fra databasen
    public void loadFromDB() {
        inventory.clear(); // VIGTIGT: undgår dubletter
        inventoryDAO.loadInventory(this);
    }

    // ✅ Opgrader inventory slots (fx i spil)
    public void upgradeSlots(int newSlots) {
        if (newSlots > maxSlots && newSlots <= 192) {
            maxSlots = newSlots;
            System.out.println("Slot-kapacitet opgraderet til: " + maxSlots);
        }
    }

    // ✅ Hent slot info (valgfri, men nyttig)
    public int getMaxSlots() {
        return maxSlots;
    }

    public int getUsedSlots() {
        return inventory.size();
    }
}