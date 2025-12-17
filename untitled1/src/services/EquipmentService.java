package services;

import model.Item;
import model.Weapon;

import java.util.HashMap;
import java.util.Map;

public class EquipmentService {
    private Map<String, Item> equippedItems = new HashMap<>();

    // Equip et item i korrekt slot
    public boolean equip(Item item) {
        String slot = getSlotForItem(item);
        if (slot == null) return false;

        // Hvis slot allerede er taget af et tohåndsvåben, fjern det
        if (item instanceof Weapon && "MainHand".equals(slot)) {
            Item existing = equippedItems.get("OffHand");
            if (existing instanceof Weapon && ((Weapon) existing).getWeaponType().equals("Two-Handed")) {
                equippedItems.remove("OffHand");
            }
        }

        equippedItems.put(slot, item);
        return true;
    }

    // Fjern udstyr
    public void unequip(String slot) {
        equippedItems.remove(slot);
    }

    // Hent udstyret i slot
    public Item getEquipped(String slot) {
        return equippedItems.get(slot);
    }

    // Bestem slot baseret på item type
    private String getSlotForItem(Item item) {
        switch (item.getCategory()) {
            case "Weapon":
                Weapon w = (Weapon) item;
                if ("Off-Hand".equals(w.getWeaponType())) return "OffHand";
                else if ("Two-Handed".equals(w.getWeaponType())) return "MainHand";
                else return "MainHand";
            case "Armor":
                return "Body";
            case "Consumable":
                return null; // Consumables kan ikke udstyres
            default:
                return null;
        }
    }

    // Hent alle udstyrede items
    public Map<String, Item> getEquippedItems() {
        return equippedItems;
    }
}
