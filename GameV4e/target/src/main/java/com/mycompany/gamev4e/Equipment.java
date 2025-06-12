package com.mycompany.gamev4e;

import java.util.List;
import java.util.Arrays;

public class Equipment {

    public String name, slot;
    public int atk, def, hp, qi;

    // Constructor to initialize equipment attributes
    public Equipment(String n, String slot, int a, int d, int h, int q) {
        name = n;           // Name of the equipment (e.g. "Iron Sword")
        this.slot = slot;   // Slot type: "weapon", "armor", or "accessory"
        atk = a;            // Bonus attack this equipment gives
        def = d;            // Bonus defense this equipment gives
        hp = h;             // Bonus HP this equipment gives
        qi = q;             // Bonus Qi this equipment gives
    }

    // Predefined list of items available in the shop
    public static final List<Equipment> shopEquipment = Arrays.asList(
            new Equipment("Iron Sword", "weapon", 5, 0, 0, 0), // Basic weapon, boosts ATK
            new Equipment("Steel Armor", "armor", 0, 5, 20, 0), // Basic armor, boosts DEF and HP
            new Equipment("Heavenly Talisman", "accessory", 0, 0, 0, 10) // Accessory, boosts Qi
    );

}
// Stores equipment name, stat bonuses, slot (e.g., weapon, armor), and type.
// Maintains a shop equipment list for available equipment.
