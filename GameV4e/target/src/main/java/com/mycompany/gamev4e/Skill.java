
package com.mycompany.gamev4e;


public class Skill {
    public String name, type;
    public int power, cooldown;
    public Skill(String name, String type, int power, int cooldown) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.cooldown = cooldown;
    }
}
