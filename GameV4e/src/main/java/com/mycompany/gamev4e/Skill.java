/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev4e;

/**
 *
 * @author zelxs
 */
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
