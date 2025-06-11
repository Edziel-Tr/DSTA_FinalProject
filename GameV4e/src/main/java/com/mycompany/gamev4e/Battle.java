
package com.mycompany.gamev4e;

import java.util.Scanner;

public class Battle {
    public static void battle(Player p, Enemy e) {
        Scanner scan = new Scanner(System.in);
        boolean isDefending = false;
        System.out.println("\n--- BATTLE ---");
        System.out.println("Enemy: " + e.name + " | Affiliation: " + e.sect + " | Realm: " + GameV4e.REALMS[e.realmIndex]);
        while (p.hp > 0 && e.hp > 0) {
            for (String s : p.skills) {
                if (p.skillCooldown.get(s) > 0) {
                    p.skillCooldown.put(s, p.skillCooldown.get(s) - 1);
                }
            }
            System.out.println("\nYour HP: " + p.hp + " Enemy HP: " + e.hp + " Your Qi: " + p.qi);
            System.out.println("1. Attack 2. Use Skill 3. Defend 4. Use Item 5. Flee");
            String a = scan.nextLine();
            if (a.equals("5")) {
                if (Math.random() < 0.5) {
                    System.out.println("You fled successfully!");
                    return;
                } else {
                    System.out.println("Failed to flee!");
                    int dmg = Math.max(1, e.atk - p.getDef());
                    p.hp -= dmg;
                    System.out.println(e.name + " attacks for " + dmg + "!");
                    continue;
                }
            } else if (a.equals("1")) {
                if (Math.random() < 0.1) {
                    System.out.println("You missed!");
                } else {
                    int dmg = Math.max(1, p.getAtk() - e.def);
                    e.hp -= dmg;
                    System.out.println("You attack for " + dmg + "!");
                }
                isDefending = false;
            } else if (a.equals("2")) {
                System.out.println("Which skill?");
                for (int i = 0; i < p.skills.size(); i++) {
                    System.out.println((i + 1) + ". " + p.skills.get(i) + " (Lv." + p.skillMastery.get(p.skills.get(i)) + ", CD:" + p.skillCooldown.get(p.skills.get(i)) + ")");
                }
                int c = GameV4e.safeIntInput(1, p.skills.size()) - 1;
                String skill = p.skills.get(c);
                // For simplicity, allSkills omitted. Use basic attack for now.
                int mastery = p.skillMastery.getOrDefault(skill, 1);
                int skillPower = 10 + mastery * 5;
                if (p.skillCooldown.get(skill) > 0) {
                    System.out.println("Skill on cooldown!");
                    continue;
                }
                if (p.qi < 10) {
                    System.out.println("Not enough Qi!");
                    continue;
                }
                int dmg = Math.max(1, p.getAtk() + skillPower - e.def);
                e.hp -= dmg;
                System.out.println("You use " + skill + " for " + dmg + " damage!");
                p.gainSkillExp(skill, 5);
                p.qi -= 10;
                p.skillCooldown.put(skill, 2);
                isDefending = false;
            } else if (a.equals("3")) {
                System.out.println("You defend! Next enemy attack is halved.");
                isDefending = true;
            } else if (a.equals("4")) {
                p.inventoryMenu();
                isDefending = false;
            } else {
                System.out.println("Invalid.");
                isDefending = false;
            }
            if (e.hp <= 0) break;
            if (Math.random() < 0.1) {
                System.out.println(e.name + " missed!");
            } else {
                int dmg = Math.max(1, e.atk - p.getDef());
                if (isDefending) {
                    dmg = (dmg + 1) / 2;
                    System.out.println("You block! Damage halved.");
                    isDefending = false;
                }
                p.hp -= dmg;
                System.out.println(e.name + " attacks for " + dmg + "!");
            }
        }
        if (p.hp <= 0) {
            System.out.println("You have been defeated...");
            p.hp = p.getMaxHp() / 2;
        } else {
            System.out.println("Victory! You defeated " + e.name);
            p.gainExp(15 + 7 * e.realmIndex);
            GameV4e.achievements.add("First Victory");
            checkQuestProgress(p, "defeat", e.sect);
        }
    }

    static void checkQuestProgress(Player p, String type, String thing) {
        for (Quest q : p.activeQuests) {
            if (!q.completed && q.type.equals(type) && q.target.equals(thing)) {
                q.progress++;
                if (q.progress >= q.needed) {
                    q.completed = true;
                    System.out.println("Quest completed: " + q.title);
                    if (!q.rewardGiven) {
                        p.silver += 30;
                        p.addItem("Qi Pill", 1);
                        q.rewardGiven = true;
                        System.out.println("You received 30 silver and a Qi Pill!");
                        GameV4e.achievements.add("Completed " + q.title);
                    }
                } else {
                    System.out.println("Quest updated: " + q.title + " (" + q.progress + "/" + q.needed + ")");
                }
            }
        }
    }
}