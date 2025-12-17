package model;

public class Armor extends Item {
    // F.eks: Helmet, Chest, Legs, Shoulder
    private String armorType;
    private int defense;

    public Armor(int id, String name, double weight, int quantity, String armorType, int defense) {
        super(id, name, "Armor", weight, quantity);
        this.armorType = armorType;
        this.defense = defense;
    }

    public String getArmorType() {
        return armorType;
    }

    public void setArmorType(String armorType) {
        this.armorType = armorType;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public String toString() {
        return "Armor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", type='" + armorType + '\'' +
                ", defense=" + defense +
                ", weight=" + getWeight() +
                ", quantity=" + getQuantity() +
                '}';
    }
}

