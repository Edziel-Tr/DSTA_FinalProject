package com.mycompany.gamev4e;

import java.util.Scanner;

public class Battle {

    // Main battle loop between player and enemy
    public static void battle(Player p, Enemy e) {
        new BattleDialogue(null, p, e).setVisible(true);
        Scanner scan = new Scanner(System.in);
        boolean isDefending = false;

        System.out.println("\n--- BATTLE ---");
        System.out.println("Enemy: " + e.name + " | Affiliation: " + e.sect + " | Realm: " + GameV4e.REALMS[e.realmIndex]);

        // Battle loop continues while both combatants are alive
        while (p.hp > 0 && e.hp > 0) {

            // Reduce cooldowns on all player skills by 1 each turn
            for (String s : p.skills) {
                if (p.skillCooldown.get(s) > 0) {
                    p.skillCooldown.put(s, p.skillCooldown.get(s) - 1);
                }
            }

            // Display player and enemy stats
            System.out.println("\nYour HP: " + p.hp + " Enemy HP: " + e.hp + " Your Qi: " + p.qi);
            System.out.println("1. Attack  2. Use Skill  3. Defend  4. Use Item  5. Flee");

            // Get player input
            String a = scan.nextLine();

            // Option 5: Attempt to flee
            if (a.equals("5")) {
                if (Math.random() < 0.5) {
                    System.out.println("You fled successfully!");
                    return; // Escape ends the battle
                } else {
                    System.out.println("Failed to flee!");
                    int dmg = Math.max(1, e.atk - p.getDef());
                    p.hp -= dmg;
                    System.out.println(e.name + " attacks for " + dmg + "!");
                    continue; // Skip to next turn
                }

                // Option 1: Basic attack
            } else if (a.equals("1")) {
                if (Math.random() < 0.1) {
                    System.out.println("You missed!");
                } else {
                    int dmg = Math.max(1, p.getAtk() - e.def);
                    e.hp -= dmg;
                    System.out.println("You attack for " + dmg + "!");
                }
                isDefending = false;

                // Option 2: Use skill
            } else if (a.equals("2")) {
                System.out.println("Which skill?");
                for (int i = 0; i < p.skills.size(); i++) {
                    String skillName = p.skills.get(i);
                    System.out.println((i + 1) + ". " + skillName
                            + " (Lv." + p.skillMastery.get(skillName)
                            + ", CD:" + p.skillCooldown.get(skillName) + ")");
                }

                // Get chosen skill index
                int c = GameV4e.safeIntInput(1, p.skills.size()) - 1;
                String skill = p.skills.get(c);
                int mastery = p.skillMastery.getOrDefault(skill, 1);
                int skillPower = 10 + mastery * 5;

                // Check cooldown and Qi cost
                if (p.skillCooldown.get(skill) > 0) {
                    System.out.println("Skill on cooldown!");
                    continue;
                }
                if (p.qi < 10) {
                    System.out.println("Not enough Qi!");
                    continue;
                }

                // Execute skill attack
                int dmg = Math.max(1, p.getAtk() + skillPower - e.def);
                e.hp -= dmg;
                System.out.println("You use " + skill + " for " + dmg + " damage!");
                p.gainSkillExp(skill, 5);        // Gain skill experience
                p.qi -= 10;                      // Consume Qi
                p.skillCooldown.put(skill, 2);   // Put skill on cooldown
                isDefending = false;

                // Option 3: Defend
            } else if (a.equals("3")) {
                System.out.println("You defend! Next enemy attack is halved.");
                isDefending = true;

                // Option 4: Use item
            } else if (a.equals("4")) {
                p.inventoryMenu(); // Open inventory and allow item usage
                isDefending = false;

                // Invalid input
            } else {
                System.out.println("Invalid.");
                isDefending = false;
            }

            // If enemy died from player action, skip enemy turn
            if (e.hp <= 0) {
                break;
            }

            // Enemy's turn to attack
            if (Math.random() < 0.1) {
                System.out.println(e.name + " missed!");
            } else {
                int dmg = Math.max(1, e.atk - p.getDef());
                if (isDefending) {
                    dmg = (dmg + 1) / 2; // Halve the damage when defending
                    System.out.println("You block! Damage halved.");
                    isDefending = false;
                }
                p.hp -= dmg;
                System.out.println(e.name + " attacks for " + dmg + "!");
            }
        }

        // Outcome of the battle
        if (p.hp <= 0) {
            System.out.println("You have been defeated...");
            p.hp = p.getMaxHp() / 2; // Revive player with half HP
        } else {
            System.out.println("Victory! You defeated " + e.name);
            p.gainExp(15 + 7 * e.realmIndex); // Grant experience based on enemy realm
            GameV4e.achievements.add("First Victory"); // Unlock achievement
            checkQuestProgress(p, "defeat", e.sect);   // Check and update relevant quests
        }
    }

    // Updates quest progress if the defeated enemy is part of an active quest
    static void checkQuestProgress(Player p, String type, String thing) {
        for (Quest q : p.activeQuests) {
            if (!q.completed && q.type.equals(type) && q.target.equals(thing)) {
                q.progress++;
                if (q.progress >= q.needed) {
                    q.completed = true;
                    if (!q.rewardGiven) {
                        p.silver += 30;
                        p.addItem("Qi Pill", 1);
                        q.rewardGiven = true;
                        GameV4e.achievements.add("Completed " + q.title);
                    }
                }
            }
        }
    }
}

// Manages the battle sequence (attack, use skill, defend, use item, flee).
// Applies effects such as damage calculation, skill cooldowns, and quest updates.
// Triggers rewards/penalties for victory or defeat.
