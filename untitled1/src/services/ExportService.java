package services;

import model.Armor;
import model.Consumable;
import model.Item;
import model.Weapon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportService {
    
    public void exportToCSV(List<Item> inventory, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Header
            writer.append("ID,Name,Category,Weight,Quantity");
            writer.append(",WeaponType,Damage");
            writer.append(",ArmorType,Defense");
            writer.append(",Effect,EffectValue\n");
            
            // Data rows
            for (Item item : inventory) {
                writer.append(String.valueOf(item.getId())).append(",");
                writer.append(escapeCSV(item.getName())).append(",");
                writer.append(item.getCategory()).append(",");
                writer.append(String.valueOf(item.getWeight())).append(",");
                writer.append(String.valueOf(item.getQuantity())).append(",");
                
                if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    writer.append(weapon.getHandType() != null ? 
                        weapon.getHandType().getDisplayName() : "").append(",");
                    writer.append(String.valueOf(weapon.getDamage())).append(",");
                    writer.append(",").append(","); // Armor fields empty
                } else {
                    writer.append(",").append(","); // Weapon fields empty
                }
                
                if (item instanceof Armor) {
                    Armor armor = (Armor) item;
                    writer.append(armor.getArmorType()).append(",");
                    writer.append(String.valueOf(armor.getDefense())).append(",");
                } else {
                    writer.append(",").append(","); // Armor fields empty
                }
                
                if (item instanceof Consumable) {
                    Consumable consumable = (Consumable) item;
                    writer.append(escapeCSV(consumable.getEffect())).append(",");
                    writer.append(String.valueOf(consumable.getEffectValue()));
                } else {
                    writer.append(",").append(","); // Consumable fields empty
                }
                
                writer.append("\n");
            }
        }
    }
    
    public void exportToJSON(List<Item> inventory, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("{\n  \"inventory\": [\n");
            
            for (int i = 0; i < inventory.size(); i++) {
                Item item = inventory.get(i);
                writer.append("    {\n");
                writer.append("      \"id\": ").append(String.valueOf(item.getId())).append(",\n");
                writer.append("      \"name\": \"").append(escapeJSON(item.getName())).append("\",\n");
                writer.append("      \"category\": \"").append(item.getCategory()).append("\",\n");
                writer.append("      \"weight\": ").append(String.valueOf(item.getWeight())).append(",\n");
                writer.append("      \"quantity\": ").append(String.valueOf(item.getQuantity())).append(",\n");
                
                if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    writer.append("      \"weaponType\": \"").append(
                        weapon.getHandType() != null ? weapon.getHandType().getDisplayName() : "").append("\",\n");
                    writer.append("      \"damage\": ").append(String.valueOf(weapon.getDamage())).append("\n");
                } else if (item instanceof Armor) {
                    Armor armor = (Armor) item;
                    writer.append("      \"armorType\": \"").append(escapeJSON(armor.getArmorType())).append("\",\n");
                    writer.append("      \"defense\": ").append(String.valueOf(armor.getDefense())).append("\n");
                } else if (item instanceof Consumable) {
                    Consumable consumable = (Consumable) item;
                    writer.append("      \"effect\": \"").append(escapeJSON(consumable.getEffect())).append("\",\n");
                    writer.append("      \"effectValue\": ").append(String.valueOf(consumable.getEffectValue())).append("\n");
                }
                
                writer.append("    }");
                if (i < inventory.size() - 1) {
                    writer.append(",");
                }
                writer.append("\n");
            }
            
            writer.append("  ]\n}");
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String escapeJSON(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}


