
package com.mycompany.gamev4e;

import java.util.List;
import java.util.ArrayList;

public class Location {
   public String name, lore;
    public List<Npc> npcs = new ArrayList<>();

     public Location(String name, String lore) {
        this.name = name;
        this.lore = lore;

        if (name.equals("Main Hall")) {
            // Main Hall NPCs, fixed realms based on novel
            npcs.add(new Npc("Mount Hua Sect Leader", "Sect Leader", "Welcome to Mount Hua, disciple.", true, 4)); // Soul Transformation
            npcs.add(new Npc("Elder Hyun Jong", "Elder", "Stay diligent and train hard.", true, 3)); // Nascent Soul
            npcs.add(new Npc("Elder Hyun Sang", "Elder", "Remember your teachings!", true, 3)); // Nascent Soul
            npcs.add(new Npc("Chun Myung (Senior Disciple)", "Senior Disciple", "Let's spar sometime.", true, 2)); // Core Formation
            npcs.add(new Npc("Baek Chun (Senior Disciple)", "Senior Disciple", "Mount Hua forever!", true, 2)); // Core Formation
            npcs.add(new Npc("Yu Iseol (Senior Disciple)", "Senior Disciple", "...", true, 2)); // Core Formation
            npcs.add(new Npc("Jin Geumryong (Senior Disciple)", "Senior Disciple", "Train with focus.", true, 2)); // Core Formation
            npcs.add(new Npc("Yoon Jong (Junior Disciple)", "Junior Disciple", "I'll do my best!", true, 1)); // Foundation
            npcs.add(new Npc("Jo Gul (Junior Disciple)", "Junior Disciple", "Let's grow together!", true, 1)); // Foundation
        }
        else if (name.equals("Dueling Arena")) {
            // Dueling Arena handled in game logic; no fixed NPCs here
        }
        else if (name.equals("Emei Summit")) {
            npcs.add(new Npc("Sword Maidens", "Emei Summit", "You are to go back where you came from Peasant!", true, 2)); // Core Formation
        }
        else if (name.equals("Southern Edge Sect")) {
            npcs.add(new Npc("Swift Edge", "Southern Edge Sect", "You are not welcome here!", true, 2)); // Core Formation
        }
        else if (name.equals("Shaolin Temple")) {
            npcs.add(new Npc("Iron Arhat", "Shaolin Temple", "Peace to you, traveler.", true, 3)); // Nascent Soul
        }
        else if (name.equals("Tang Clan Compound")) {
            npcs.add(new Npc("Poison Hand", "Tang Clan", "Beware our poisons.", true, 2)); // Core Formation
        }
        else if (name.equals("Celestial Sword Valley")) {
            npcs.add(new Npc("Sky Piercer", "Celestial Sword Sect", "The sky is my limit.", true, 4)); // Soul Transformation
        }
        else if (name.equals("Bandit Camp")) {
            npcs.add(new Npc("Bandit Chief", "Bandit", "I'm the Bandit Chief!", true, 2)); // Core Formation
        }
        else {
            // Default: random wandering cultivator (Foundation)
            npcs.add(Npc.randomNPC("Wandering Cultivator", "The world is vast, and so is your potential.", false, 1));
        }
    }
}