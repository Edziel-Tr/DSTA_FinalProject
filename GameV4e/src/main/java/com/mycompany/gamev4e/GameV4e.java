package com.mycompany.gamev4e;

import java.util.*;

public class GameV4e {
        
    static Scanner scan = new Scanner(System.in);

    // --- Data moved from NewClass ---
    public static final String[] SECTS = {
        "Mount Hua Sect",
        "Southern Edge Sect",
        "Wudang Sect",
        "Shaolin Temple",
        "Kunlun Sect",
        "Emei Sect",
        "Beggar's Union",
        "Tang Clan",
        "Celestial Sword Sect"
    };

    public static final String[][] SECT_SKILLS = {
        {"Plum Blossom Sword Art", "Mount Hua Divine Sword", "Flowing Sword Technique", "Seven Sword Stance"},
        {"Southern Edge Sword", "Thousand Step Flow", "Moonlight Edge", "Falling Petal Slash"},
        {"Tai Chi Sword", "Wudang Cloud Steps", "Heavenly Qi Palm", "Waterflow Defense"},
        {"Arhat Fist", "Dragon Subduing Staff", "Shaolin Golden Bell", "Bodhi Palm"},
        {"Kunlun Sword", "Vast Sky Palm", "Cloud Ascending Steps", "Kunlun Barrier"},
        {"Emei Soft Sword", "Nine Yang Palm", "Jade Maiden Sword", "Emei Needle"},
        {"Dog Beating Staff", "Drunken Fist", "Big Bowl Palm", "Beggar's Secret Step"},
        {"Hidden Weapon Technique", "Poison Mist Art", "Tang's Needle Rain", "Shadow Blade"},
        {"Celestial Sword Art", "Sky Piercing Slash", "Sword Qi Storm", "Heavenly Sword Domain"}
    };

    public static final String[] REALMS = {
        "Qi Refining", "Foundation Establishment", "Core Formation", "Nascent Soul", "Soul Transformation",
        "Heaven Ascension", "Immortal"
    };

    public static final Map<String, String> SECT_LORE = Map.of(
            "Mount Hua Sect", "Once the greatest sword sect in the world, now rebuilding its former glory under the leadership of Chun Myung and the elders.",
            "Southern Edge Sect", "Mount Hua's greatest rivals. Proud, sharp, and relentless, their techniques are famed for speed and precision.",
            "Wudang Sect", "Famed for their internal arts and the legendary Tai Chi Sword, the Wudang are wise and mysterious.",
            "Shaolin Temple", "The ancient temple of martial monks, their fists and staffs are as unyielding as their discipline.",
            "Kunlun Sect", "Mountain masters blending sword and palm, with deep spiritual roots.",
            "Emei Sect", "Home to powerful sword maidens, their Nine Yang Palm and Jade Maiden Sword are feared.",
            "Beggar's Union", "The largest martial alliance, known for their Dog Beating Staff and wild, unorthodox techniques.",
            "Tang Clan", "Masters of poison and hidden weapons, the Tang keep outsiders at bay with deadly traps.",
            "Celestial Sword Sect", "A sect shrouded in myth, their sword arts pierce the heavens themselves."
    );

    public static final String[] MAIN_HALL_NPCS = {
        "Mount Hua Sect Leader", "Elder Hyun Jong", "Elder Hyun Sang", "Chun Myung (Senior Disciple)",
        "Baek Chun (Senior Disciple)", "Yu Iseol (Senior Disciple)", "Jin Geumryong (Senior Disciple)",
        "Yoon Jong (Junior Disciple)", "Jo Gul (Junior Disciple)"
    };

    public static final String[] LOC_NAMES = {
        "Main Hall", "Training Grounds", "Market", "Plum Blossom Forest", "Bandit Camp",
        "Southern Edge Sect", "Shaolin Temple", "Kunlun Mountains", "Emei Summit",
        "Tang Clan Compound", "Celestial Sword Valley"
    };

    public static final String[] LOC_LORES = {
        "The heart of Mount Hua, where disciples gather.",
        "Where swordsmen sharpen their skills.",
        "A lively market for goods and rumors.",
        "A peaceful place, haunted by legends.",
        "Nest of the notorious bandits!",
        "Your famous rival's stronghold.",
        "The monks renowned for their martial arts.",
        "Snowy peaks and mysterious auras.",
        "Sacred home of powerful sword maidens.",
        "Masters of poison and hidden weapons.",
        "A mystical land of ancient swords."
    };

    public static final Map<String, String> LOCATION_HOSTILE_SECT = Map.of(
            "Mount Hua Sect", "Mount Hua Sect",
            "Southern Edge Sect", "Southern Edge Sect",
            "Shaolin Temple", "Shaolin Temple",
            "Tang Clan Compound", "Tang Clan",
            "Celestial Sword Valley", "Celestial Sword Sect",
            "Bandit Camp", "Bandit Alliance"
    );

    public static final Map<String, String[]> SECT_ENEMY_NAMES = Map.of(
            "Mount Hua Sect", new String[]{"Mount Hua Disciple", "Mount Hua Elder", "Chun Myung"},
            "Southern Edge Sect", new String[]{"Southern Edge Disciple", "Southern Edge Elder", "Swift Edge"},
            "Wudang Sect", new String[]{"Wudang Disciple", "Wudang Elder", "Cloud Stepper"},
            "Shaolin Temple", new String[]{"Shaolin Monk", "Shaolin Elder", "Iron Arhat"},
            "Kunlun Sect", new String[]{"Kunlun Disciple", "Kunlun Elder", "Sky Palm"},
            "Emei Sect", new String[]{"Emei Maiden", "Emei Elder", "Jade Maiden"},
            "Beggar's Union", new String[]{"Beggar Chief", "Drunken Elder", "Dog Beater"},
            "Tang Clan", new String[]{"Tang Assassin", "Tang Elder", "Poison Hand"},
            "Celestial Sword Sect", new String[]{"Celestial Swordsman", "Celestial Elder", "Sky Piercer"},
            "Bandit Alliance", new String[]{"Green Forest Bandit", "Iron Spear Bandit", "Swift Wolf Bandit", "Crazed Axeman", "Red Scarf Bandit"}
    );

    public static final Map<String, String[]> SECT_SKILL_MAP = Map.of(
            "Mount Hua Sect", SECT_SKILLS[0],
            "Southern Edge Sect", SECT_SKILLS[1],
            "Wudang Sect", SECT_SKILLS[2],
            "Shaolin Temple", SECT_SKILLS[3],
            "Kunlun Sect", SECT_SKILLS[4],
            "Emei Sect", SECT_SKILLS[5],
            "Beggar's Union", SECT_SKILLS[6],
            "Tang Clan", SECT_SKILLS[7],
            "Celestial Sword Sect", SECT_SKILLS[8]
    );

    public static Set<String> achievements = new HashSet<>();

    // --- Locations ---
    public static final Location[] LOCATIONS = new Location[LOC_NAMES.length];

    static {
        for (int i = 0; i < LOC_NAMES.length; i++) {
            LOCATIONS[i] = new Location(LOC_NAMES[i], LOC_LORES[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("Return of the Mount Hua Sect - RPG");
        Player player = new Player();
        System.out.print("Enter your martial name: ");
        player.name = scan.nextLine();
        System.out.println("Choose your sect:");
        for (int i = 0; i < SECTS.length; i++) {
            System.out.println((i + 1) + ". " + SECTS[i]);
        }
        int sectChoice = safeIntInput(1, SECTS.length) - 1;
        player.sect = SECTS[sectChoice];
        player.sectIndex = sectChoice;
        for (String skill : SECT_SKILLS[sectChoice]) {
            player.skills.add(skill);
            player.skillMastery.put(skill, 1);
            player.skillExp.put(skill, 0);
            player.skillCooldown.put(skill, 0);
        }
        System.out.println("Welcome, " + player.name + " of the " + player.sect + "!");
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Status 2. Travel 3. Quests 4. Practice Skill 5. Inventory 6. Rest 7. Achievements 8. Exit");
            System.out.print("Pick: ");
            switch (scan.nextLine()) {
                case "1" ->
                    player.showStatus();
                case "2" ->
                    travelMenu(player);
                case "3" ->
                    player.showQuests();
                case "4" ->
                    player.practiceSkill();
                case "5" ->
                    player.inventoryMenu();
                case "6" ->
                    player.rest();
                case "7" ->
                    showAchievements();
                case "8" -> {
                    System.out.println("May your sword reach the peak of Mount Hua!");
                    return;
                }
                default ->
                    System.out.println("Invalid.");
            }
        }
    }

    static void showAchievements() {
        System.out.println("Achievements: " + achievements);
    }

    static void travelMenu(Player p) {
        System.out.println("\nWhere to?");
        System.out.println("0. Leave");
        for (int i = 0; i < LOCATIONS.length; i++) {
            System.out.println((i + 1) + ". " + LOCATIONS[i].name + " - " + LOCATIONS[i].lore + (SECT_LORE.containsKey(LOCATIONS[i].name) ? " " + SECT_LORE.get(LOCATIONS[i].name) : ""));
        }
        int choice = safeIntInput(0, LOCATIONS.length);
        if (choice == 0) {
            return;
        }
        Location loc = LOCATIONS[choice - 1];
        if (LOCATION_HOSTILE_SECT.containsKey(loc.name)) {
            String sect = LOCATION_HOSTILE_SECT.get(loc.name);
            if (new Random().nextBoolean()) {
                System.out.println("A hostile " + sect + " cultivator appears!");
                playerBattle(p, new Enemy(p, loc.name));
            }
        }
        visitLocation(p, loc);
    }

    static void visitLocation(Player p, Location loc) {
        System.out.println("\n-- " + loc.name + " --");
        System.out.println(loc.lore + (SECT_LORE.containsKey(loc.name) ? " " + SECT_LORE.get(loc.name) : ""));
        for (int i = 0; i < loc.npcs.size(); i++) {
            System.out.println("NPC " + (i + 1) + ": " + loc.npcs.get(i).name + " (" + loc.npcs.get(i).role + ")");
        }
        System.out.println("0. Leave");
        int npcChoice = safeIntInput(0, loc.npcs.size());
        if (npcChoice == 0) {
            return;
        }
        Npc chosenNpc = loc.npcs.get(npcChoice - 1);
        System.out.println("Interact with " + chosenNpc.name + ": 1. Talk 2. Spar 3. Receive Quest 4. Back");
        String act = scan.nextLine();
        if (act.equals("1")) {
            chosenNpc.talkToNPC(p);
        } else if (act.equals("2")) {
            System.out.println("You spar with " + chosenNpc.name);
            Enemy npcEnemy = new Enemy(p, loc.name);
            npcEnemy.name = chosenNpc.name;
            npcEnemy.sect = chosenNpc.role;
            playerBattle(p, npcEnemy);
        } else if (act.equals("3")) {
            p.activeQuests.add(new Quest("Defeat Hostile " + chosenNpc.role, "Defeat 3 enemies of the " + chosenNpc.role + ".", "defeat", chosenNpc.role, 3));
            System.out.println("Received quest: Defeat Hostile " + chosenNpc.role);
        } else if (act.equals("4")) {
            return;
        } else {
            System.out.println("Invalid.");
        }
    }

    static void playerBattle(Player p, Enemy e) {
        Battle.battle(p, e);
    }

    static int safeIntInput(int min, int max) {
        int result = -1;
        while (true) {
            try {
                String line = scan.nextLine();
                result = Integer.parseInt(line);
                if (result >= min && result <= max) {
                    break;
                }
            } catch (Exception e) {
            }
            System.out.println("Enter number " + min + "-" + max);
        }
        return result;
    }
}
