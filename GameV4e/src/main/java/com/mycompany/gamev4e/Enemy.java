package com.mycompany.gamev4e;

import java.util.*;

public class Enemy {

    public String name, sect, skill;
    public int hp, qi, atk, def, realmIndex;

    public Enemy(Player p, String locName) {
        Random rand = new Random();
        String hostileSect = GameV4e.LOCATION_HOSTILE_SECT.getOrDefault(locName, null);
        if (hostileSect != null) {
            this.sect = hostileSect;
            String[] possibleNames = GameV4e.SECT_ENEMY_NAMES.get(hostileSect);
            this.name = possibleNames[rand.nextInt(possibleNames.length)];
            String[] skills = GameV4e.SECT_SKILL_MAP.getOrDefault(hostileSect, GameV4e.SECT_SKILLS[0]);
            this.skill = skills[rand.nextInt(skills.length)];
        } else {
            this.sect = GameV4e.SECTS[rand.nextInt(GameV4e.SECTS.length)];
            String[] possibleNames = GameV4e.SECT_ENEMY_NAMES.getOrDefault(this.sect, new String[]{"Wandering Cultivator"});
            this.name = possibleNames[rand.nextInt(possibleNames.length)];
            String[] skills = GameV4e.SECT_SKILL_MAP.getOrDefault(this.sect, GameV4e.SECT_SKILLS[0]);
            this.skill = skills[rand.nextInt(skills.length)];
        }
        int relDiff = (rand.nextInt(10) < 8) ? rand.nextInt(2) : rand.nextInt(5) + 2;
        this.realmIndex = Math.min(p.realmIndex + relDiff, GameV4e.REALMS.length - 1);
        int relGap = this.realmIndex - p.realmIndex;
        int baseHp = p.getMaxHp(), baseQi = p.getMaxQi(), baseAtk = p.getAtk(), baseDef = p.getDef();
        if (relGap <= 0) {
            this.hp = baseHp + rand.nextInt(20);
            this.qi = baseQi + rand.nextInt(20);
            this.atk = baseAtk + rand.nextInt(5);
            this.def = baseDef + rand.nextInt(5);
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

    public Enemy(Npc npc, Player p) {
        this.name = npc.name;
        this.sect = npc.role;  // or npc.sect/affiliation if you use a different field
        this.realmIndex = npc.realmIndex;
        // You can set skill as a default or random, or copy from NPC if NPC has a skill field
        this.skill = "Basic Attack"; // Set as needed

        // Set stats based on realmIndex difference (same logic as before)
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
