
package com.mycompany.gamev4e;

import java.util.List;
import java.util.Arrays;

public class Item {
    public String name, type;
    public int weight;
    public Item(String n, String t, int w) { name = n; type = t; weight = w; }

    public static final List<Item> allItems = Arrays.asList(
        new Item("Qi Pill", "usable", 1),
        new Item("Vitality Elixir", "usable", 1),
        new Item("Wild Ginseng", "usable", 2),
        new Item("Bandit Badge", "quest", 1),
        new Item("Iron Sword", "equipment", 3),
        new Item("Steel Armor", "equipment", 4),
        new Item("Heavenly Talisman", "equipment", 1)
    );
}
// Stores item name, type, and stats. and Maintains a list of all possible items.
