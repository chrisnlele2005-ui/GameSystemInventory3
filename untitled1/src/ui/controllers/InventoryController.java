package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Armor;
import model.Consumable;
import model.Item;
import model.Weapon;
import services.ExportService;
import services.InventoryService;
import services.SearchService;
import services.StatisticsService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryController {

    // Services
    private InventoryService inventoryService = new InventoryService();
    private SearchService searchService = new SearchService();
    private StatisticsService statisticsService = new StatisticsService();
    private ExportService exportService = new ExportService();

    // ObservableList til TableView
    private ObservableList<Item> observableInventory = FXCollections.observableArrayList();
    private ObservableList<Item> fullInventory = FXCollections.observableArrayList();

    // FXML Components - Table
    @FXML private TableView<Item> inventoryTable;
    @FXML private TableColumn<Item, Number> idColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, Number> weightColumn;
    @FXML private TableColumn<Item, Number> quantityColumn;

    // Input fields
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField weightField;
    @FXML private TextField quantityField;
    
    // Weapon-specific fields
    @FXML private ComboBox<String> weaponTypeComboBox;
    @FXML private TextField damageField;
    
    // Armor-specific fields
    @FXML private TextField armorTypeField;
    @FXML private TextField defenseField;
    
    // Consumable-specific fields
    @FXML private TextField effectField;
    @FXML private TextField effectValueField;

    // Search/Filter fields
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategoryComboBox;
    @FXML private TextField minWeightField;
    @FXML private TextField maxWeightField;
    @FXML private TextField minDamageField;
    @FXML private TextField minDefenseField;
    @FXML private ComboBox<String> weaponTypeFilterComboBox;

    // Statistics labels
    @FXML private Label totalItemsLabel;
    @FXML private Label totalWeightLabel;
    @FXML private Label weightPercentageLabel;
    @FXML private Label usedSlotsLabel;
    @FXML private Label categoryBreakdownLabel;

    @FXML
    public void initialize() {
        // Bind table columns
        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        categoryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        weightColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getWeight()));
        quantityColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()));

        // Bind data til tabellen, så vi kan sortere/organisere via kolonneklik
        inventoryTable.setItems(observableInventory);

        // Setup combo boxes
        categoryComboBox.getItems().addAll("Weapon", "Armor", "Consumable");
        weaponTypeComboBox.getItems().addAll("Dual-Wield", "Off-Hand", "Two-Handed");
        filterCategoryComboBox.getItems().addAll("All", "Weapon", "Armor", "Consumable");
        weaponTypeFilterComboBox.getItems().addAll("All", "Dual-Wield", "Off-Hand", "Two-Handed");
        
        filterCategoryComboBox.setValue("All");
        weaponTypeFilterComboBox.setValue("All");
        
        // Enable multi-select
        inventoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Update stats when selection changes
        inventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateStatistics());
        
        // Load initial data
        loadInventory();
    }

    @FXML
    public void addItem() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String category = categoryComboBox.getValue();
            double weight = Double.parseDouble(weightField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (category == null || name.isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            Item item;
            switch (category.toLowerCase()) {
                case "weapon":
                    String weaponTypeStr = weaponTypeComboBox.getValue() != null ? 
                        weaponTypeComboBox.getValue() : "Dual-Wield";
                    int damage = damageField.getText().isEmpty() ? 0 : 
                        Integer.parseInt(damageField.getText());
                    
                    Weapon.WeaponHandType handType = Weapon.WeaponHandType.DUAL_WIELD;
                    if (weaponTypeStr.equals("Off-Hand")) {
                        handType = Weapon.WeaponHandType.OFF_HAND;
                    } else if (weaponTypeStr.equals("Two-Handed")) {
                        handType = Weapon.WeaponHandType.TWO_HANDED;
                    }
                    item = new Weapon(id, name, weight, quantity, handType, damage);
                    break;
                    
                case "armor":
                case "amor":
                case "armour":
                    String armorType = armorTypeField.getText().isEmpty() ? 
                        "Unknown" : armorTypeField.getText();
                    int defense = defenseField.getText().isEmpty() ? 0 : 
                        Integer.parseInt(defenseField.getText());
                    item = new Armor(id, name, weight, quantity, armorType, defense);
                    break;
                    
                case "consumable":
                    String effect = effectField.getText().isEmpty() ? 
                        "Unknown" : effectField.getText();
                    int effectValue = effectValueField.getText().isEmpty() ? 0 : 
                        Integer.parseInt(effectValueField.getText());
                    item = new Consumable(id, name, weight, quantity, effect, effectValue);
                    break;
                    
                default:
                    showAlert("Error", "Invalid category. Use Weapon, Armor, or Consumable.");
                    return;
            }

            // Brug InventoryService som single source of truth
            boolean added = inventoryService.addItem(item);
            if (added) {
                // Synkroniser UI-lister med den aktuelle inventory, så stacking m.m. vises korrekt
                fullInventory.clear();
                fullInventory.addAll(inventoryService.getInventory());
                observableInventory.clear();
                observableInventory.addAll(fullInventory);

                clearFields();
                updateStatistics();
                showAlert("Success", "Item added successfully.");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid number format. Please check your inputs.");
        } catch (Exception e) {
            showAlert("Error", "Error adding item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void removeItem() {
        ObservableList<Item> selectedItems = inventoryTable.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            showAlert("Error", "Please select at least one item to remove.");
            return;
        }

        // Confirm bulk delete
        if (selectedItems.size() > 1) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setContentText("Are you sure you want to delete " + selectedItems.size() + " items?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.CANCEL) {
                return;
            }
        }

        for (Item item : selectedItems) {
            inventoryService.removeById(item.getId());
            fullInventory.remove(item);
            observableInventory.remove(item);
        }
        
        updateStatistics();
        showAlert("Success", "Item(s) removed successfully.");
    }

    @FXML
    public void saveInventory() {
        inventoryService.saveToDB();
        showAlert("Success", "Inventory saved to database.");
    }

    @FXML
    public void loadInventory() {
        inventoryService.loadFromDB();
        fullInventory.clear();
        fullInventory.addAll(inventoryService.getInventory());
        observableInventory.clear();
        observableInventory.addAll(fullInventory);
        updateStatistics();
        showAlert("Success", "Inventory loaded from database.");
    }

    @FXML
    public void resetInventory() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Reset");
        confirm.setContentText("Are you sure you want to reset the entire inventory? This cannot be undone.");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.CANCEL) {
            return;
        }

        fullInventory.clear();
        observableInventory.clear();
        inventoryService.getInventory().clear();
        updateStatistics();
        showAlert("Success", "Inventory reset successfully.");
    }

    @FXML
    public void searchItem() {
        applyFilters();
    }

    @FXML
    public void applyFilters() {
        String name = searchField.getText();
        String category = filterCategoryComboBox.getValue();
        if ("All".equals(category)) category = null;
        
        Double minWeight = null;
        Double maxWeight = null;
        Integer minDamage = null;
        Integer minDefense = null;
        Weapon.WeaponHandType weaponType = null;
        
        try {
            if (!minWeightField.getText().isEmpty()) {
                minWeight = Double.parseDouble(minWeightField.getText());
            }
            if (!maxWeightField.getText().isEmpty()) {
                maxWeight = Double.parseDouble(maxWeightField.getText());
            }
            if (!minDamageField.getText().isEmpty()) {
                minDamage = Integer.parseInt(minDamageField.getText());
            }
            if (!minDefenseField.getText().isEmpty()) {
                minDefense = Integer.parseInt(minDefenseField.getText());
            }
            String weaponTypeStr = weaponTypeFilterComboBox.getValue();
            if (weaponTypeStr != null && !"All".equals(weaponTypeStr)) {
                if ("Dual-Wield".equals(weaponTypeStr)) {
                    weaponType = Weapon.WeaponHandType.DUAL_WIELD;
                } else if ("Off-Hand".equals(weaponTypeStr)) {
                    weaponType = Weapon.WeaponHandType.OFF_HAND;
                } else if ("Two-Handed".equals(weaponTypeStr)) {
                    weaponType = Weapon.WeaponHandType.TWO_HANDED;
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid number format in filter fields.");
            return;
        }

        List<Item> filtered = searchService.advancedSearch(
            fullInventory, name, category, minWeight, maxWeight, 
            minDamage, minDefense, weaponType, null
        );

        observableInventory.clear();
        observableInventory.addAll(filtered);
        updateStatistics();
    }

    @FXML
    public void clearFilters() {
        searchField.clear();
        filterCategoryComboBox.setValue("All");
        minWeightField.clear();
        maxWeightField.clear();
        minDamageField.clear();
        minDefenseField.clear();
        weaponTypeFilterComboBox.setValue("All");
        observableInventory.clear();
        observableInventory.addAll(fullInventory);
        updateStatistics();
    }

    @FXML
    public void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Inventory to CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showSaveDialog(getStage());
        
        if (file != null) {
            try {
                exportService.exportToCSV(fullInventory, file.getAbsolutePath());
                showAlert("Success", "Inventory exported to CSV successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void exportToJSON() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Inventory to JSON");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File file = fileChooser.showSaveDialog(getStage());
        
        if (file != null) {
            try {
                exportService.exportToJSON(fullInventory, file.getAbsolutePath());
                showAlert("Success", "Inventory exported to JSON successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateStatistics() {
        StatisticsService.InventoryStats stats = statisticsService.calculateStats(
            fullInventory,
            inventoryService.getMaxWeight(),
            inventoryService.getMaxSlots()
        );

        totalItemsLabel.setText("Total Items: " + stats.totalItems);
        totalWeightLabel.setText(String.format("Total Weight: %.2f / %.2f", 
            stats.totalWeight, stats.maxWeight));
        weightPercentageLabel.setText(String.format("Weight Usage: %.1f%%", 
            statisticsService.getWeightPercentage(fullInventory, stats.maxWeight)));
        usedSlotsLabel.setText(String.format("Slots: %d / %d", 
            stats.usedSlots, stats.maxSlots));

        // Category breakdown
        StringBuilder breakdown = new StringBuilder("Categories: ");
        for (Map.Entry<String, Integer> entry : stats.categoryCount.entrySet()) {
            breakdown.append(entry.getKey()).append(" (").append(entry.getValue()).append(") ");
        }
        categoryBreakdownLabel.setText(breakdown.toString());
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        categoryComboBox.setValue(null);
        weightField.clear();
        quantityField.clear();
        weaponTypeComboBox.setValue(null);
        damageField.clear();
        armorTypeField.clear();
        defenseField.clear();
        effectField.clear();
        effectValueField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) inventoryTable.getScene().getWindow();
    }
}
