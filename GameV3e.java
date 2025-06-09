
package com.mycompany.gamev3e;

import java.util.*;

public class GameV3e {

    static Scanner scan = new Scanner(System.in);
    static Random rand = new Random();

    // 1. Arrays - fixed data
    static String[] sects = {
        "Mount Hua Sect", "Southern Edge Sect", "Wudang Sect"
    };

    static String[][] sectSkills = {
        {"Plum Blossom Sword Art", "Mount Hua Divine Sword", "Flowing Sword Technique", "Seven Sword Stance"},
        {"Southern Edge Sword", "Thousand Step Flow", "Moonlight Edge", "Falling Petal Slash"},
        {"Tai Chi Sword", "Wudang Cloud Steps", "Heavenly Qi Palm", "Waterflow Defense"}
    };

    static String[] mainLocations = {
        "Main Hall", "Training Grounds", "Market", "Plum Blossom Forest",
        "Phantom Valley", "Shraal Mountain", "Secret Tombs of Medicine Great Emperor"
    };
    
    static String [] realms = {
        "Qi Refining", "Foundation Establishment", "Core Formation", "Nascent Soul", "Soul Transformation",
        "Heaven Ascension", "Immortal"
    };

    // Player class
    static class Player {

        String name;
        String sect;
        String realm = "Outer Sect Disciple";
        int hp = 120;
        int qi = 80;
        int atk = 18;
        int def = 7;
        int silver = 50;

        // 2. ArrayList for skills and inventory
        ArrayList<String> skills = new ArrayList<>();
        ArrayList<String> inventory = new ArrayList<>();

        // 3. HashMap for skill mastery levels
        HashMap<String, Integer> skillMastery = new HashMap<>();

        void levelUpRealm() {
            int idx = Arrays.asList(realms).indexOf(realm);
            if (idx + 1 < realms.length) {
                realm = realms[idx + 1];
                atk += 5;
                def += 3;
                hp += 20;
                qi += 15;
                System.out.println("You have ascended to " + realm + "! Stats increased.");
            }
        }
    }

    // Enemy class
    static class Enemy {

        String name;
        String sect;
        int hp;
        int atk;
        int def;
        String skill;
    }

    // 4. Stack for recent battle actions
    static Stack<String> battleActions = new Stack<>();

    // 5. Queue for turn order in battle
    static Queue<String> turnQueue = new LinkedList<>();

    public static void main(String[] args) {
        System.out.println("Return of the Mount Hua Sect (Inspired Game)");
        Player player = new Player();

        System.out.print("Enter your martial name: ");
        player.name = scan.nextLine();

        System.out.println("Choose your sect:");
        for (int i = 0; i < sects.length; i++) {
            System.out.println((i + 1) + ". " + sects[i]);
        }

        int sectChoice = safeIntInput(1, sects.length) - 1;
        player.sect = sects[sectChoice];

        for (String skill : sectSkills[sectChoice]) {
            player.skills.add(skill);
            player.skillMastery.put(skill, 1);
        }

        System.out.println("Welcome, " + player.name + " of the " + player.sect + "!");

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Status");
            System.out.println("2. Travel");
            System.out.println("3. Practice Sword Art");
            System.out.println("4. Inventory");
            System.out.println("5. Rest");
            System.out.println("6. Exit");
            System.out.print("Pick: ");
            String choice = scan.nextLine();

            switch (choice) {
                case "1" ->
                    showStatus(player);
                case "2" ->
                    travelMenu(player);
                case "3" ->
                    practiceSkill(player);
                case "4" ->
                    inventoryMenu(player);
                case "5" ->
                    rest(player);
                case "6" -> {
                    System.out.println("May your sword reach the peak of Mount Hua!");
                    return;
                }
                default ->
                    System.out.println("Invalid.");
            }
        }
    }

    static void showStatus(Player p) {
        System.out.println("\n--- Sword Disciple Status ---");
        System.out.println("Name: " + p.name);
        System.out.println("Sect: " + p.sect);
        System.out.println("Realm: " + p.realm);
        System.out.println("HP: " + p.hp + "/120");
        System.out.println("Qi: " + p.qi + "/80");
        System.out.println("Attack: " + p.atk + "  Defense: " + p.def);
        System.out.println("Silver: " + p.silver);
        System.out.println("Sword Arts:");
        for (String skill : p.skills) {
            System.out.println("- " + skill + " (Mastery Lv. " + p.skillMastery.getOrDefault(skill, 1) + ")");
        }
        System.out.println("Inventory: " + p.inventory);
    }

    static void travelMenu(Player player) {
        System.out.println("\nWhere do you wish to go?");
        for (int i = 0; i < mainLocations.length; i++) {
            System.out.println((i + 1) + ". " + mainLocations[i]);
        }
        int loc = safeIntInput(1, mainLocations.length) - 1;
        String place = mainLocations[loc];

        switch (place) {
            case "Main Hall" ->
                mainHallMenu(player);
            case "Training Grounds" ->
                trainingMenu(player);
            case "Market" ->
                marketMenu(player);
            case "Plum Blossom Forest" ->
                forestMenu(player);
            case "Phantom Valley" ->
                phantomMenu(player);
            case "Shraal Mountain" ->
                shraalMenu(player);
            case "Secret Tombs of Medicine Great Emperor" ->
                tombsMenu(player);
            default ->
                System.out.println("Nothing here yet.");
        }
    }

    static void mainHallMenu(Player player) {
        System.out.println("\n-- Mount Hua Main Hall --");
        System.out.println("The sect’s core: used for meetings, missions, and announcements.");
        System.out.println("The Sect Leader gives a rousing speech about restoring Mount Hua's former glory.");
    }

    static void trainingMenu(Player player) {
        System.out.println("\n-- Training Grounds --");
        player.qi = Math.min(80, player.qi + 8);
        player.atk++;
        System.out.println("Sword intent sharpens. (Attack +1, Qi +8)");

        if (rand.nextInt(3) == 0) {
            System.out.println("A senior disciple challenges you to a spar!");
            battle(player, randomEnemy());
        }
    }

    static void marketMenu(Player player) {
        System.out.println("\n-- Market Street --");
        System.out.println("1. Buy Qi Pill (10 silver)");
        System.out.println("2. Buy Bamboo Sword (18 silver)");
        System.out.println("3. Sell item");
        System.out.println("4. Leave");
        System.out.print("Pick: ");
        String c = scan.nextLine();

        switch (c) {
            case "1" -> {
                if (player.silver >= 10) {
                    player.silver -= 10;
                    player.inventory.add("Qi Pill");
                    System.out.println("You bought a Qi Pill!");
                } else {
                    System.out.println("Not enough silver.");
                }
            }
            case "2" -> {
                if (player.silver >= 18) {
                    player.silver -= 18;
                    player.atk += 2;
                    System.out.println("You bought a Bamboo Sword! (Attack +2)");
                } else {
                    System.out.println("Not enough silver.");
                }
            }
            case "3" -> {
                if (player.inventory.isEmpty()) {
                    System.out.println("You have nothing to sell.");
                } else {
                    for (int i = 0; i < player.inventory.size(); i++) {
                        System.out.println((i + 1) + ". " + player.inventory.get(i));
                    }
                    System.out.print("Pick item to sell: ");
                    int sell = safeIntInput(1, player.inventory.size()) - 1;
                    String item = player.inventory.remove(sell);
                    int sellValue = 4 + rand.nextInt(9);
                    player.silver += sellValue;
                    System.out.println("You sold " + item + " for " + sellValue + " silver.");
                }
            }
            default ->
                System.out.println("You leave the market.");
        }
    }

    static void forestMenu(Player player) {
        System.out.println("\n-- Plum Blossom Forest --");
        System.out.println("A peaceful forest where Mount Hua disciples once trained in sword arts.");
        int event = rand.nextInt(3);
        switch (event) {
            case 0 -> {
                System.out.println("You find a wild ginseng root under a tree!");
                player.inventory.add("Wild Ginseng");
            }
            case 1 -> {
                System.out.println("A rogue swordsman blocks your path!");
                battle(player, randomEnemy());
            }
            case 2 -> {
                System.out.println("You meditate, breathing the forest air. Qi +7.");
                player.qi = Math.min(80, player.qi + 7);
            }
        }
    }

    static void phantomMenu(Player player) {
        System.out.println("\n-- Phantom Valley --");
        System.out.println("Dark and mysterious, many say it’s cursed.");
        if (rand.nextInt(2) == 0) {
            System.out.println("A Phantom Valley Guardian appears!");
            battle(player, randomEnemy());
        } else {
            System.out.println("You find rare herbs for medicine.");
            player.inventory.add("Phantom Herb");
        }
    }

    static void shraalMenu(Player player) {
        System.out.println("\n-- Shraal Mountain --");
        System.out.println("A rocky, harsh environment perfect for intense training.");
        player.atk += 1;
        player.def += 1;
        System.out.println("Training improves your attack and defense by 1 each.");
    }

    static void tombsMenu(Player player) {
        System.out.println("\n-- Secret Tombs of Medicine Great Emperor --");
        System.out.println("A dangerous place filled with traps and treasures.");
        if (rand.nextInt(3) == 0) {
            System.out.println("You got ambushed by tomb guardians!");
            battle(player, randomEnemy());
        } else {
            System.out.println("You find ancient medicinal scrolls.");
            player.inventory.add("Ancient Scroll");
        }
    }

    static Enemy randomEnemy() {
        Enemy e = new Enemy();
        e.name = "Disciple Bandit";
        e.sect = sects[rand.nextInt(sects.length)];
        e.hp = 80 + rand.nextInt(20);
        e.atk = 10 + rand.nextInt(5);
        e.def = 3 + rand.nextInt(3);
        e.skill = sectSkills[rand.nextInt(sectSkills.length)][rand.nextInt(4)];
        return e;
    }

    static void battle(Player player, Enemy enemy) {
        System.out.println("\nBattle Start: " + player.name + " vs. " + enemy.name + " of " + enemy.sect);
        turnQueue.clear();
        turnQueue.add("Player");
        turnQueue.add("Enemy");

        while (player.hp > 0 && enemy.hp > 0) {
            String turn = turnQueue.poll();
            if ("Player".equals(turn)) {
                System.out.println("\nYour turn! HP: " + player.hp + ", Qi: " + player.qi);
                System.out.println("Choose an action:");
                System.out.println("1. Basic Attack");
                System.out.println("2. Use Qi Pill");
                System.out.println("3. Use Recover Qi Pill");
                System.out.println("4. Use Skill");
                String action = scan.nextLine();

                switch (action) {
                    case "1" -> {
                        int damage = player.atk - enemy.def;
                        damage = Math.max(damage, 1);
                        enemy.hp -= damage;
                        System.out.println("You attack dealing " + damage + " damage!");
                        battleActions.push("Player attacks " + damage);
                    }
                    case "2" -> {
                        if (player.inventory.remove("Qi Pill")) {
                            player.qi = Math.min(80, player.qi + 15);
                            System.out.println("You used a Qi Pill! Qi restored.");
                        } else {
                            System.out.println("No Qi Pill found.");
                        }
                    }
                    case "3" -> {
                        if (player.inventory.remove("Recover Qi Pill")) {
                            player.hp = Math.min(120, player.hp + 20);
                            System.out.println("You used a Recover Qi Pill! HP restored.");
                        } else {
                            System.out.println("No Recover Qi Pill found.");
                        }
                    }
                    case "4" -> {
                        if (player.skills.isEmpty()) {
                            System.out.println("No skills learned.");
                        } else {
                            System.out.println("Choose skill:");
                            for (int i = 0; i < player.skills.size(); i++) {
                                System.out.println((i + 1) + ". " + player.skills.get(i));
                            }
                            int skillChoice = safeIntInput(1, player.skills.size()) - 1;
                            String skill = player.skills.get(skillChoice);
                            int mastery = player.skillMastery.getOrDefault(skill, 1);
                            int skillDamage = player.atk + mastery * 3 - enemy.def;
                            skillDamage = Math.max(skillDamage, 1);
                            enemy.hp -= skillDamage;
                            player.qi -= 10;
                            System.out.println("You use " + skill + ", dealing " + skillDamage + " damage!");
                            battleActions.push("Player uses " + skill + " for " + skillDamage + " damage");
                        }
                    }
                    default -> {
                        System.out.println("Invalid action, turn skipped.");
                    }
                }
                turnQueue.add("Enemy");
            } else {
                // Enemy turn
                int damage = enemy.atk - player.def;
                damage = Math.max(damage, 1);
                player.hp -= damage;
                System.out.println("\nEnemy attacks dealing " + damage + " damage!");
                battleActions.push("Enemy attacks " + damage);
                turnQueue.add("Player");
            }
        }

        if (player.hp <= 0) {
            System.out.println("\nYou have been defeated...");
        } else {
            System.out.println("\nVictory! Enemy defeated.");
            player.silver += 15 + rand.nextInt(15);
            System.out.println("You gained some silver.");
            if (rand.nextInt(3) == 0) {
                System.out.println("You found a Qi Pill on the enemy.");
                player.inventory.add("Qi Pill");
            }
        }
    }

    static void practiceSkill(Player player) {
        if (player.skills.isEmpty()) {
            System.out.println("You have no skills to practice.");
            return;
        }
        System.out.println("Which skill to practice?");
        for (int i = 0; i < player.skills.size(); i++) {
            System.out.println((i + 1) + ". " + player.skills.get(i) + " (Mastery Lv. " + player.skillMastery.get(player.skills.get(i)) + ")");
        }
        int c = safeIntInput(1, player.skills.size()) - 1;
        String skill = player.skills.get(c);
        int mastery = player.skillMastery.get(skill);
        mastery++;
        player.skillMastery.put(skill, mastery);
        System.out.println("You practice " + skill + " and your mastery improves to level " + mastery + "!");
        player.qi -= 5;
        if (player.qi < 0) {
            player.qi = 0;
            System.out.println("You ran out of Qi.");
        }
    }

    static void inventoryMenu(Player player) {
        if (player.inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("Your Inventory:");
        for (int i = 0; i < player.inventory.size(); i++) {
            System.out.println((i + 1) + ". " + player.inventory.get(i));
        }
        System.out.println("Enter number to use item or 0 to cancel:");
        int c = safeIntInput(0, player.inventory.size());
        if (c == 0) {
            return;
        }
        String item = player.inventory.get(c - 1);
        switch (item) {
            case "Qi Pill" -> {
                player.qi = Math.min(80, player.qi + 15);
                player.inventory.remove(c - 1);
                System.out.println("You used a Qi Pill! Qi restored.");
            }
            case "Recover Qi Pill" -> {
                player.hp = Math.min(120, player.hp + 20);
                player.inventory.remove(c - 1);
                System.out.println("You used a Recover Qi Pill! HP restored.");
            }
            default ->
                System.out.println("Cannot use this item right now.");
        }
    }

    static void rest(Player player) {
        player.hp = 120;
        player.qi = 80;
        System.out.println("You rest and recover your health and Qi.");
    }

    // Safe int input helper
    static int safeIntInput(int min, int max) {
        int result = -1;
        while (true) {
            try {
                String line = scan.nextLine();
                result = Integer.parseInt(line);
                if (result >= min && result <= max) {
                    break;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return result;
    }
}
