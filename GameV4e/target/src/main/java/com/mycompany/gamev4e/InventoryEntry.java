package com.mycompany.gamev4e;

// Represents an entry in the player's inventory
public class InventoryEntry {
    public Item item;   // The actual item (e.g., Qi Pill, weapon, etc.)
    public int count;   // How many of this item the player has

    // Constructor to create a new inventory entry
    public InventoryEntry(Item item, int count) {
        this.item = item;     // Set the item
        this.count = count;   // Set the quantity of the item
    }

}
// stores item and count it 



