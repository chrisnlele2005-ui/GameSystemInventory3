package database.DAO;

import database.DatabaseManager;
import model.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CharacterDAO {
    // Gemmer en karakter i databasen
    public void saveCharacter(Character character) {
        String sql = "INSERT INTO characters (id, name, max_weight) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, character.getId());
            stmt.setString(2, character.getName());
            stmt.setDouble(3, character.getMaxWeight());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Henter alle karakterer fra databasen
    public List<Character> getAllCharacters() {
        List<Character> characters = new ArrayList<>();
        String sql = "SELECT * FROM characters";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                characters.add(new Character(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return characters;
    }

    // Hent en karakter baseret på ID
    public Character getCharacterById(int id) {
        String sql = "SELECT * FROM characters WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                return new Character(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Slet karakter
    public void deleteCharacter(int id) {
        String sql = "DELETE FROM characters WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

