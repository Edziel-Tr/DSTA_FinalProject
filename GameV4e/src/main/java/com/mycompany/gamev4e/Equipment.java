
package com.mycompany.gamev4e;

import java.util.*;

public class Equipment {
    public String name, slot;
    public int atk, def, hp, qi;
    public Equipment(String n, String slot, int a, int d, int h, int q) {
        name = n; this.slot = slot; atk = a; def = d; hp = h; qi = q;
    }

    public static final List<Equipment> shopEquipment = Arrays.asList(
        new Equipment("Iron Sword", "weapon", 5, 0, 0, 0),
        new Equipment("Steel Armor", "armor", 0, 5, 20, 0),
        new Equipment("Heavenly Talisman", "accessory", 0, 0, 0, 10)
    );
}