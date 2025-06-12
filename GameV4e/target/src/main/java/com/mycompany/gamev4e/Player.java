
package com.mycompany.gamev4e;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Player { 
    public String name, sect;
    public int sectIndex, realmIndex, exp, expToLevel = 30, silver = 50;
    public int baseHp = 100, baseQi = 60, baseAtk = 10, baseDef = 5;
    public int hp, qi;
    public Map<String, Integer> skillMastery = new HashMap<>();
    public Map<String, Integer> skillExp = new HashMap<>();
    public Map<String, Integer> skillCooldown = new HashMap<>();
    public List<String> skills = new ArrayList<>();
    public List<InventoryEntry> inventory = new ArrayList<>();
    public Map<String, Equipment> equipped = new HashMap<>();
    public List<Quest> activeQuests = new ArrayList<>();

    public void rest() {
        hp = getMaxHp();
        qi = getMaxQi();
        
        // Additional benefits from resting
        if (Math.random() < 0.3) {
            int skillIndex = new Random().nextInt(skills.size());
            if (skillIndex >= 0) {
                String randomSkill = skills.get(skillIndex);
                gainSkillExp(randomSkill, 2);
            }
        }
    }


    public String getRealm() { return GameV4e.REALMS[realmIndex]; }
    public int getMaxHp() { int mod = 0; for (var e : equipped.values()) mod += e.hp; return baseHp + realmIndex * 25 + mod; }
    public int getMaxQi() { int mod = 0; for (var e : equipped.values()) mod += e.qi; return baseQi + realmIndex * 15 + mod; }
    public int getAtk() { int mod = 0; for (var e : equipped.values()) mod += e.atk; return baseAtk + realmIndex * 4 + getHighestSkillMastery() * 2 + mod; }
    public int getDef() { int mod = 0; for (var e : equipped.values()) mod += e.def; return baseDef + realmIndex * 3 + getHighestSkillMastery() + mod; }
    public int getHighestSkillMastery() { return skillMastery.values().stream().max(Integer::compare).orElse(1); }
    public int getTotalWeight() { int w = 0; for (var ie : inventory) w += ie.item.weight * ie.count; return w; }

    public void showStatus() {
        System.out.println("\n--- Status ---");
        System.out.println("Name: " + name + "   Sect: " + sect + "   Realm: " + getRealm());
        System.out.println("HP: " + hp + "/" + getMaxHp() + "  Qi: " + qi + "/" + getMaxQi());
        System.out.println("Atk: " + getAtk() + "  Def: " + getDef() + "  Silver: " + silver + "  Weight: " + getTotalWeight());
        System.out.println("Skills:");
        for (String skill : skills) {
            System.out.println("- " + skill + " (Lv." + skillMastery.getOrDefault(skill, 1) + ") (CD:" + skillCooldown.get(skill) + ")");
        }
        System.out.print("Equipment: ");
        for (var eq : equipped.values()) {
            System.out.print(eq.name + " ");
        }
        System.out.println();
        System.out.print("Inventory: ");
        for (var ie : inventory) {
            System.out.print(ie.item.name + "(x" + ie.count + ") ");
        }
        System.out.println();
    }

    public void showQuests() {
        if (activeQuests.isEmpty()) {
            System.out.println("No active quests.");
            return;
        }
        System.out.println("Active Quests:");
        for (Quest q : activeQuests) {
            System.out.println("- " + q.title + (q.completed ? " (Completed)" : " (" + q.progress + "/" + q.needed + ")"));
            System.out.println("   " + q.description);
        }
    }

    public void practiceSkill() {
        if (skills.isEmpty()) {
            System.out.println("No skills to practice.");
            return;
        }
        System.out.println("Which skill?");
        for (int i = 0; i < skills.size(); i++) {
            System.out.println((i + 1) + ". " + skills.get(i) + " (Lv." + skillMastery.get(skills.get(i)) + ")");
        }
        int c = GameV4e.safeIntInput(1, skills.size()) - 1;
        String skill = skills.get(c);
        int mastery = skillMastery.get(skill);
        mastery++;
        skillMastery.put(skill, mastery);
        System.out.println("You practice " + skill + " and mastery improves to level " + mastery + "!");
        qi -= 5;
        if (qi < 0) qi = 0;
        GameV4e.achievements.add("Practiced " + skill);
    }

    public void inventoryMenu() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).item.name + " (x" + inventory.get(i).count + ") (" + inventory.get(i).item.type + ")");
        }
        System.out.println("Pick number to use/equip (0 to cancel):");
        int c = GameV4e.safeIntInput(0, inventory.size());
        if (c == 0) return;
        InventoryEntry ie = inventory.get(c - 1);
        if (ie.item.type.equals("usable")) {
            if (ie.item.name.equals("Qi Pill")) qi = Math.min(getMaxQi(), qi + 15);
            else if (ie.item.name.equals("Vitality Elixir")) hp = Math.min(getMaxHp(), hp + 20);
            else if (ie.item.name.equals("Wild Ginseng")) {
                hp = Math.min(getMaxHp(), hp + 10);
                qi = Math.min(getMaxQi(), qi + 10);
            }
            System.out.println("Used " + ie.item.name + "!");
            useItem(ie.item.name);
        } else if (ie.item.type.equals("equipment")) {
            for (Equipment eq : Equipment.shopEquipment) {
                if (eq.name.equals(ie.item.name)) equip(eq);
            }
            useItem(ie.item.name);
        } else {
            System.out.println("Can't use that now.");
        }
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println("[EXP +" + amount + "] (" + exp + "/" + expToLevel + ")");
        while (exp >= expToLevel) {
            exp -= expToLevel;
            levelUpRealm();
            expToLevel += 10;
        }
    }

    public void levelUpRealm() {
        if (realmIndex + 1 < GameV4e.REALMS.length) {
            realmIndex++;
            System.out.println("Ascended to " + getRealm() + "!");
            hp = getMaxHp();
            qi = getMaxQi();
            GameV4e.achievements.add("Realm: " + getRealm());
        }
    }

    public void gainSkillExp(String skill, int amount) {
        int cur = skillExp.getOrDefault(skill, 0) + amount;
        int masteryLv = skillMastery.getOrDefault(skill, 1);
        int nextLvExp = masteryLv * 10 + 10;
        while (cur >= nextLvExp) {
            cur -= nextLvExp;
            masteryLv++;
            skillMastery.put(skill, masteryLv);
            System.out.println(skill + " advanced to Mastery Lv. " + masteryLv + "!");
        }
        skillExp.put(skill, cur);
    }

    public void addItem(String name, int amt) {
        for (var ie : inventory) {
            if (ie.item.name.equals(name)) {
                ie.count += amt;
                return;
            }
        }
        for (var it : Item.allItems) {
            if (it.name.equals(name)) {
                inventory.add(new InventoryEntry(it, amt));
                return;
            }
        }
    }

    public boolean useItem(String name) {
        for (var ie : inventory) {
            if (ie.item.name.equals(name) && ie.count > 0) {
                ie.count--;
                if (ie.count == 0) inventory.remove(ie);
                return true;
            }
        }
        return false;
    }

    public int getItemCount(String name) {
        for (var ie : inventory) if (ie.item.name.equals(name)) return ie.count;
        return 0;
    }

    public void equip(Equipment eq) {
        equipped.put(eq.slot, eq);
        System.out.println("Equipped " + eq.name + " in " + eq.slot + " slot!");
    }
}
// Stores player information (name, sect, stats, skills, inventory, quests, equipment, etc.).
//Methods for status display, practicing skills, inventory management, resting, gaining experience, and leveling up.
//Handles skill mastery, skill experience, and quest progress.