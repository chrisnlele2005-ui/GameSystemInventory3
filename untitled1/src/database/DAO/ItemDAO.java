package database.DAO;

import database.DatabaseManager;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // Gemmer et item - alle kolonner sættes, nogle kan være NULL afhængigt af kategori
    public void saveItem(Item item) {
        String sql = "REPLACE INTO items (id, name, category, weight, quantity, " +
                "weapon_type, damage, armor_type, armor_defense, effect, effect_value) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getCategory());
            stmt.setDouble(4, item.getWeight());
            stmt.setInt(5, item.getQuantity());

            // Default values
            stmt.setNull(6, Types.VARCHAR); // weapon_type
            stmt.setNull(7, Types.INTEGER); // damage
            stmt.setNull(8, Types.VARCHAR); // armor_type
            stmt.setNull(9, Types.INTEGER); // armor_defense
            stmt.setNull(10, Types.VARCHAR); // effect
            stmt.setNull(11, Types.INTEGER); // effect_value

            if (item instanceof Weapon) {
                Weapon w = (Weapon)item;
                stmt.setString(6, w.getWeaponType());
                stmt.setInt(7, w.getDamage());
            } else if (item instanceof Armor) {
                Armor a = (Armor)item;
                stmt.setString(8, a.getArmorType());
                stmt.setInt(9, a.getDefense());
            } else if (item instanceof Consumable) {
                Consumable c = (Consumable)item;
                stmt.setString(10, c.getEffect());
                stmt.setInt(11, c.getEffectValue());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hent alle items
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, name, category, weight, quantity, " +
                "weapon_type, damage, armor_type, armor_defense, effect, effect_value FROM items";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String category = rs.getString("category");
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double weight = rs.getDouble("weight");
                int quantity = rs.getInt("quantity");

                switch (category) {
                    case "Weapon": {
                        String weaponType = rs.getString("weapon_type");
                        int damage = rs.getInt("damage");
                        // OBS: Weapon constructor er (id, name, weight, quantity, weaponType, damage)
                        items.add(new Weapon(id, name, weight, quantity, weaponType, damage));
                        break;
                    }
                    case "Armor": {
                        String armorType = rs.getString("armor_type");
                        int defense = rs.getInt("armor_defense");
                        items.add(new Armor(id, name, weight, quantity, armorType, defense));
                        break;
                    }
                    case "Consumable": {
                        String effect = rs.getString("effect");
                        int effectValue = rs.getInt("effect_value");
                        items.add(new Consumable(id, name, weight, quantity, effect, effectValue));
                        break;
                    }
                    default:
                        // Hvis ukendt kategori, gem som generisk Item (kan ikke, fordi Item er abstract)
                        // Vi kan vælge at ignorere eller logge.
                        System.err.println("Ukendt kategori for item id=" + id + ": " + category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
