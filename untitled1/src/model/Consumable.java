package model;

public class Consumable extends Item {
    private String effect;
    private int effectValue;

    public Consumable(int id, String name, double weight, int quantity, String effect, int effectValue) {
        super(id, name, "Consumable", weight, quantity);
        this.effect = effect;
        this.effectValue = effectValue;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(int effectValue) {
        this.effectValue = effectValue;
    }

    @Override
    public String toString() {
        return "Consumable{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", effect='" + effect + '\'' +
                ", value=" + effectValue +
                ", weight=" + getWeight() +
                ", quantity=" + getQuantity() +
                '}';
    }
}
