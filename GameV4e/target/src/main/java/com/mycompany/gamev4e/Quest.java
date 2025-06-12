
package com.mycompany.gamev4e;

public class Quest {
    public String title, description, type, target;
    public int needed, progress = 0;
    public boolean completed = false, rewardGiven = false;
    public Quest(String t, String d, String type, String target, int needed) {
        this.title = t; this.description = d; this.type = type; this.target = target; this.needed = needed;
    }
}
//Stores quest title, description, progress, type, target, required number, and reward status.
// Tracks if the quest is completed and if rewards have been given.