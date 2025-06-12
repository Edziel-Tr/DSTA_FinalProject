package com.mycompany.gamev4e;

import java.util.Random;

public class Enemy {

    // Basic enemy attributes
    public String name, sect, skill;
    public int hp, qi, atk, def, realmIndex;

    // Constructor: generates an enemy based on the player's location
    public Enemy(Player p, String locName) {
        Random rand = new Random();

        // Check if the current location has a hostile sect
        String hostileSect = GameV4e.LOCATION_HOSTILE_SECT.getOrDefault(locName, null);

        if (hostileSect != null) {
            // Set sect and name based on hostile sect
            this.sect = hostileSect;
            String[] possibleNames = GameV4e.SECT_ENEMY_NAMES.get(hostileSect);
            this.name = possibleNames[rand.nextInt(possibleNames.length)];

            // Randomly choose a skill from the sect's skill set
            String[] skills = GameV4e.SECT_SKILL_MAP.getOrDefault(hostileSect, GameV4e.SECT_SKILLS[0]);
            this.skill = skills[rand.nextInt(skills.length)];
        } else {
            // No specific hostile sect: choose a random one
            this.sect = GameV4e.SECTS[rand.nextInt(GameV4e.SECTS.length)];
            String[] possibleNames = GameV4e.SECT_ENEMY_NAMES.getOrDefault(this.sect, new String[]{"Wandering Cultivator"});
            this.name = possibleNames[rand.nextInt(possibleNames.length)];

            // Get skills for the chosen sect
            String[] skills = GameV4e.SECT_SKILL_MAP.getOrDefault(this.sect, GameV4e.SECT_SKILLS[0]);
            this.skill = skills[rand.nextInt(skills.length)];
        }

        // Determine how much stronger or weaker the enemy is compared to the player
        int relDiff = (rand.nextInt(10) < 8) ? rand.nextInt(2) : rand.nextInt(5) + 2;
        this.realmIndex = Math.min(p.realmIndex + relDiff, GameV4e.REALMS.length - 1);

        // Calculate stat difference based on realm gap
        int relGap = this.realmIndex - p.realmIndex;
        int baseHp = p.getMaxHp(), baseQi = p.getMaxQi(), baseAtk = p.getAtk(), baseDef = p.getDef();

        if (relGap <= 0) {
            // Enemy is equal or weaker than player: slight random variation
            this.hp = baseHp + rand.nextInt(20);
            this.qi = baseQi + rand.nextInt(20);
            this.atk = baseAtk + rand.nextInt(5);
            this.def = baseDef + rand.nextInt(5);
        } else {
            // Enemy is stronger: scale up stats based on realm gap
            double scale = Math.pow(2, Math.max(relGap, 1));
            if (this.realmIndex == GameV4e.REALMS.length - 1) {
                scale *= 2; // Boss-tier boost if at highest realm
            }
            this.hp = (int) (baseHp * scale);
            this.qi = (int) (baseQi * scale);
            this.atk = (int) (baseAtk * scale);
            this.def = (int) (baseDef * scale);
        }
    }

    // Alternate constructor: creates an enemy using NPC data
    public Enemy(Npc npc, Player p) {
        this.name = npc.name;
        this.sect = npc.role; // You can change this if NPC has an actual "sect" field
        this.realmIndex = npc.realmIndex;

        // Skill can be taken from NPC or randomized; defaulted here
        this.skill = "Basic Attack";

        // Stat generation same as above
        int relGap = this.realmIndex - p.realmIndex;
        int baseHp = p.getMaxHp(), baseQi = p.getMaxQi(), baseAtk = p.getAtk(), baseDef = p.getDef();

        if (relGap <= 0) {
            this.hp = baseHp;
            this.qi = baseQi;
            this.atk = baseAtk;
            this.def = baseDef;
        } else {
            double scale = Math.pow(2, Math.max(relGap, 1));
            if (this.realmIndex == GameV4e.REALMS.length - 1) {
                scale *= 2;
            }
            this.hp = (int) (baseHp * scale);
            this.qi = (int) (baseQi * scale);
            this.atk = (int) (baseAtk * scale);
            this.def = (int) (baseDef * scale);
        }
    }
}