
package com.mycompany.gamev4e;

import java.util.Scanner;

public class Npc {
    public String name, role, dialogue;
    public boolean givesQuest;
    public int favor = 0;
    public int realmIndex;

    // Main constructor for ALL NPCs (fixed or random)
    public Npc(String name, String role, String dialogue, boolean givesQuest, int realmIndex) {
        this.name = name;
        this.role = role;
        this.dialogue = dialogue;
        this.givesQuest = givesQuest;
        this.realmIndex = realmIndex;
    }

    // Optional: random NPC generator (for wandering cultivators, etc.)
    public static Npc randomNPC(String role, String dialogue, boolean givesQuest, int realmIndex) {
        return new Npc(randomName(), role, dialogue, givesQuest, realmIndex);
    }

    public void talkToNPC(Player p) {
        Scanner scan = new Scanner(System.in);
        System.out.println(name + ": \"" + dialogue + "\"");
        System.out.println("1. Compliment 2. Insult 3. Ask for quest");
        String c = scan.nextLine();
        int opt = 1;
        try { opt = Integer.parseInt(c); } catch (Exception e) { opt = 1; }
        if (opt == 1) {
            favor++;
            System.out.println("They smile. (Favor now: " + favor + ")");
        } else if (opt == 2) {
            favor--;
            System.out.println("They frown. (Favor now: " + favor + ")");
        } else if (opt == 3 && givesQuest) {
            Quest quest = new Quest("Defeat Hostile " + role, "Defeat 3 enemies of the " + role + ".", "defeat", role, 3);
            p.activeQuests.add(quest);
            System.out.println("Quest given: " + quest.title);
        }
    }

    private static String randomName() {
        String[] fn = {"Chung", "Baek", "Yu", "Jin", "Lee", "Seo", "Hwang", "Han", "Park", "Cho"};
        String[] ln = {"Myung", "Seok", "Hyun", "Woo", "Soo", "Jae", "Bin", "Ho", "Min", "Ju"};
        return fn[(int)(Math.random() * fn.length)] + " " + ln[(int)(Math.random() * ln.length)];
    }
}

// Stores NPC name and role.
// Method for interaction (e.g., talking to the player).