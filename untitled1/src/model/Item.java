package model;

public abstract class Item {
    private int id;
    private String name;
    private String category; // f.eks. "Weapon", "Armor", "Consumable"
    private double weight;
    private int quantity; // til stakbare items som potions eller pile

    // Constructor
    public Item(int id, String name, String category, double weight, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.quantity = quantity;
    }

    // Getters og Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Optional: ToString for debugging
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", weight=" + weight +
                ", quantity=" + quantity +
                '}';
    }
}
