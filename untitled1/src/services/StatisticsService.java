package services;

import model.Armor;
import model.Consumable;
import model.Item;
import model.Weapon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    
    public static class InventoryStats {
        public int totalItems;
        public int totalQuantity;
        public double totalWeight;
        public double maxWeight;
        public int usedSlots;
        public int maxSlots;
        public Map<String, Integer> categoryCount;
        public Map<String, Integer> weaponTypeCount;
        public Map<String, Integer> armorTypeCount;
        public int totalDamage;
        public int totalDefense;
        public int consumableCount;
        
        public InventoryStats() {
            categoryCount = new HashMap<>();
            weaponTypeCount = new HashMap<>();
            armorTypeCount = new HashMap<>();
        }
    }
    
    public InventoryStats calculateStats(List<Item> inventory, double maxWeight, int maxSlots) {
        InventoryStats stats = new InventoryStats();
        stats.maxWeight = maxWeight;
        stats.maxSlots = maxSlots;
        
        for (Item item : inventory) {
            stats.totalItems++;
            stats.totalQuantity += item.getQuantity();
            stats.totalWeight += item.getWeight() * item.getQuantity();
            
            // Category count
            String category = item.getCategory();
            stats.categoryCount.put(category, 
                stats.categoryCount.getOrDefault(category, 0) + 1);
            
            // Type-specific stats
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                String handType = weapon.getHandType() != null ? 
                    weapon.getHandType().getDisplayName() : "Unknown";
                stats.weaponTypeCount.put(handType, 
                    stats.weaponTypeCount.getOrDefault(handType, 0) + 1);
                stats.totalDamage += weapon.getDamage() * item.getQuantity();
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                String armorType = armor.getArmorType();
                stats.armorTypeCount.put(armorType, 
                    stats.armorTypeCount.getOrDefault(armorType, 0) + 1);
                stats.totalDefense += armor.getDefense() * item.getQuantity();
            } else if (item instanceof Consumable) {
                stats.consumableCount += item.getQuantity();
            }
        }
        
        stats.usedSlots = inventory.size();
        return stats;
    }
    
    public double getWeightPercentage(List<Item> inventory, double maxWeight) {
        double currentWeight = 0;
        for (Item item : inventory) {
            currentWeight += item.getWeight() * item.getQuantity();
        }
        return maxWeight > 0 ? (currentWeight / maxWeight) * 100 : 0;
    }
    
    public double getSlotPercentage(List<Item> inventory, int maxSlots) {
        return maxSlots > 0 ? ((double) inventory.size() / maxSlots) * 100 : 0;
    }
}


