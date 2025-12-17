package model;

public class Weapon extends Item {
    // Weapon types: "Dual-Wield", "Off-Hand", "Two-Handed"
    public enum WeaponHandType {
        DUAL_WIELD("Dual-Wield"),
        OFF_HAND("Off-Hand"),
        TWO_HANDED("Two-Handed");
        
        private final String displayName;
        
        WeaponHandType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private WeaponHandType handType;
    private int damage;

    public Weapon(int id, String name, double weight, int quantity, WeaponHandType handType, int damage) {
        super(id, name, "Weapon", weight, quantity);
        this.handType = handType;
        this.damage = damage;
    }
    
    // Legacy constructor for backward compatibility
    public Weapon(int id, String name, double weight, int quantity, String weaponType, int damage) {
        super(id, name, "Weapon", weight, quantity);
        this.damage = damage;
        // Convert string to enum
        if (weaponType != null) {
            String normalized = weaponType.trim().toLowerCase();
            if (normalized.contains("dual") || normalized.contains("one-handed")) {
                this.handType = WeaponHandType.DUAL_WIELD;
            } else if (normalized.contains("off-hand") || normalized.contains("offhand")) {
                this.handType = WeaponHandType.OFF_HAND;
            } else if (normalized.contains("two-handed") || normalized.contains("twohanded")) {
                this.handType = WeaponHandType.TWO_HANDED;
            } else {
                this.handType = WeaponHandType.DUAL_WIELD; // default
            }
        } else {
            this.handType = WeaponHandType.DUAL_WIELD;
        }
    }

    public WeaponHandType getHandType() {
        return handType;
    }

    public void setHandType(WeaponHandType handType) {
        this.handType = handType;
    }
    
    public String getWeaponType() {
        return handType != null ? handType.getDisplayName() : "Unknown";
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", handType=" + (handType != null ? handType.getDisplayName() : "Unknown") +
                ", damage=" + damage +
                ", weight=" + getWeight() +
                ", quantity=" + getQuantity() +
                '}';
    }
    
    public boolean isTwoHanded() {
        return handType == WeaponHandType.TWO_HANDED;
    }
    
    public boolean isDualWield() {
        return handType == WeaponHandType.DUAL_WIELD;
    }
    
    public boolean isOffHand() {
        return handType == WeaponHandType.OFF_HAND;
    }
}

