package model;

public class Character {
    private int id;
    private String name;
    private double maxWeight = 50;

    public Character(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getMaxWeight() { return maxWeight; }
}
