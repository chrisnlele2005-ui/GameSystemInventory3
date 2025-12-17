package services;

import model.Armor;
import model.Consumable;
import model.Item;
import model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    // Søg efter navn
    public List<Item> searchByName(List<Item> inventory, String name) {
        if (name == null || name.trim().isEmpty()) {
            return inventory;
        }
        String searchTerm = name.toLowerCase();
        return inventory.stream()
                .filter(item -> item.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    // Søg efter kategori
    public List<Item> searchByCategory(List<Item> inventory, String category) {
        if (category == null || category.trim().isEmpty()) {
            return inventory;
        }
        String searchCategory = category.trim();
        return inventory.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(searchCategory))
                .collect(Collectors.toList());
    }

    // Søg efter maksimal vægt
    public List<Item> searchByMaxWeight(List<Item> inventory, double maxWeight) {
        return inventory.stream()
                .filter(item -> item.getWeight() <= maxWeight)
                .collect(Collectors.toList());
    }
    
    // Søg efter vægt range
    public List<Item> searchByWeightRange(List<Item> inventory, double minWeight, double maxWeight) {
        return inventory.stream()
                .filter(item -> item.getWeight() >= minWeight && item.getWeight() <= maxWeight)
                .collect(Collectors.toList());
    }
    
    // Søg efter minimum damage (weapons)
    public List<Item> searchByMinDamage(List<Item> inventory, int minDamage) {
        return inventory.stream()
                .filter(item -> item instanceof Weapon && ((Weapon) item).getDamage() >= minDamage)
                .collect(Collectors.toList());
    }
    
    // Søg efter minimum defense (armor)
    public List<Item> searchByMinDefense(List<Item> inventory, int minDefense) {
        return inventory.stream()
                .filter(item -> item instanceof Armor && ((Armor) item).getDefense() >= minDefense)
                .collect(Collectors.toList());
    }
    
    // Søg efter weapon hand type
    public List<Item> searchByWeaponType(List<Item> inventory, Weapon.WeaponHandType handType) {
        return inventory.stream()
                .filter(item -> item instanceof Weapon && ((Weapon) item).getHandType() == handType)
                .collect(Collectors.toList());
    }
    
    // Søg efter armor type
    public List<Item> searchByArmorType(List<Item> inventory, String armorType) {
        if (armorType == null || armorType.trim().isEmpty()) {
            return inventory;
        }
        String searchType = armorType.trim().toLowerCase();
        return inventory.stream()
                .filter(item -> item instanceof Armor && 
                    ((Armor) item).getArmorType().toLowerCase().contains(searchType))
                .collect(Collectors.toList());
    }
    
    // Advanced search combining multiple criteria
    public List<Item> advancedSearch(List<Item> inventory, String name, String category, 
                                     Double minWeight, Double maxWeight, 
                                     Integer minDamage, Integer minDefense,
                                     Weapon.WeaponHandType weaponType, String armorType) {
        return inventory.stream()
                .filter(item -> {
                    // Name filter
                    if (name != null && !name.trim().isEmpty()) {
                        if (!item.getName().toLowerCase().contains(name.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // Category filter
                    if (category != null && !category.trim().isEmpty()) {
                        if (!item.getCategory().equalsIgnoreCase(category.trim())) {
                            return false;
                        }
                    }
                    
                    // Weight filter
                    if (minWeight != null && item.getWeight() < minWeight) {
                        return false;
                    }
                    if (maxWeight != null && item.getWeight() > maxWeight) {
                        return false;
                    }
                    
                    // Weapon-specific filters
                    if (item instanceof Weapon) {
                        Weapon weapon = (Weapon) item;
                        if (minDamage != null && weapon.getDamage() < minDamage) {
                            return false;
                        }
                        if (weaponType != null && weapon.getHandType() != weaponType) {
                            return false;
                        }
                    }
                    
                    // Armor-specific filters
                    if (item instanceof Armor) {
                        Armor armor = (Armor) item;
                        if (minDefense != null && armor.getDefense() < minDefense) {
                            return false;
                        }
                        if (armorType != null && !armorType.trim().isEmpty()) {
                            if (!armor.getArmorType().toLowerCase().contains(armorType.toLowerCase())) {
                                return false;
                            }
                        }
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }
}

