package com.mycompany.gamev4e;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.mycompany.gamev4e.Npc;
import java.util.Random;
import javax.swing.border.TitledBorder; 

public class BattleDialogue extends javax.swing.JFrame {

    private Player player;
    private Enemy enemy;
    private JTextArea battleLog;
    private JPanel actionPanel;
    private JPanel statsPanel;
    private boolean isDefending = false;

    public BattleDialogue(JFrame parent, Player player, Enemy enemy) {
        super("Battle");
        this.player = player;
        this.enemy = enemy;

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initCustomComponents();
    }

    private void initCustomComponents() {
        // Battle log area
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw battle background
                g.setColor(new Color(40, 20, 30));
                g.fillRect(0, 0, getWidth(), getHeight());

                // Add decorative elements
                g.setColor(new Color(255, 215, 0, 50));
                for (int i = 0; i < 20; i++) {
                    g.drawString("⚔",
                            new Random().nextInt(getWidth()),
                            new Random().nextInt(getHeight()));
                }
            }
        };

        // Battle log area with scroll
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setFont(new Font("KaiTi", Font.PLAIN, 16));
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBackground(new Color(20, 20, 40, 200));
        battleLog.setForeground(Color.WHITE);
        battleLog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 140)),
                "Battle Log",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("KaiTi", Font.BOLD, 14),
                new Color(255, 215, 0)
        ));
        // Stats panel with improved layout
        statsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        statsPanel.setOpaque(false);
        updateStats();

        // Action buttons with martial arts theme
        actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        String[] actions = {"Strike", "Technique", "Defend", "Item", "Retreat"};
        for (String action : actions) {
            JButton button = new JButton(action);
            button.setFont(new Font("KaiTi", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(70, 40, 50, 200));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 160, 140)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            button.addActionListener(this::handleBattleAction);
            actionPanel.add(button);
        }

        // Layout
        mainPanel.add(logScroll, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Initial battle message
        appendLog("\n⚔ BATTLE COMMENCES ⚔");
        appendLog("Opponent: " + enemy.name + " of " + enemy.sect);
        appendLog("Realm: " + GameV4e.REALMS[enemy.realmIndex]);
    }

    private void updateStats() {
        statsPanel.removeAll();

        // Player stats
        JPanel playerPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        playerPanel.setBorder(BorderFactory.createTitledBorder(player.name));

        JLabel playerHp = new JLabel("HP: " + player.hp + "/" + player.getMaxHp());
        JLabel playerQi = new JLabel("Qi: " + player.qi + "/" + player.getMaxQi());
        JLabel playerAtkDef = new JLabel("ATK: " + player.getAtk() + " DEF: " + player.getDef());
        JLabel playerSilver = new JLabel("Silver: " + player.silver);

        playerPanel.add(playerHp);
        playerPanel.add(playerQi);
        playerPanel.add(playerAtkDef);
        playerPanel.add(playerSilver);

        // Enemy stats
        JPanel enemyPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        enemyPanel.setBorder(BorderFactory.createTitledBorder(enemy.name));

        JLabel enemyHp = new JLabel("HP: " + enemy.hp);
        JLabel enemyAtkDef = new JLabel("ATK: " + enemy.atk + " DEF: " + enemy.def);
        JLabel enemyRealm = new JLabel("Realm: " + GameV4e.REALMS[enemy.realmIndex]);

        enemyPanel.add(enemyHp);
        enemyPanel.add(enemyAtkDef);
        enemyPanel.add(enemyRealm);

        statsPanel.add(playerPanel);
        statsPanel.add(enemyPanel);

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void handleBattleAction(ActionEvent e) {
        String action = ((JButton) e.getSource()).getText();

        // Reduce cooldowns
        player.skills.forEach(skill -> {
            if (player.skillCooldown.get(skill) > 0) {
                player.skillCooldown.put(skill, player.skillCooldown.get(skill) - 1);
            }
        });

        switch (action) {
            case "Attack":
                performBasicAttack();
                break;
            case "Use Skill":
                useSkill();
                break;
            case "Defend":
                defend();
                break;
            case "Use Item":
                useItem();
                return; // Skip enemy turn if using item
            case "Flee":
                attemptFlee();
                return; // Skip enemy turn if fleeing
        }

        if (enemy.hp <= 0) {
            battleWon();
            return;
        }

        enemyTurn();

        if (player.hp <= 0) {
            battleLost();
            return;
        }

        updateStats();
    }

    private void performBasicAttack() {
        if (Math.random() < 0.1) {
            appendLog("You missed!");
        } else {
            int dmg = Math.max(1, player.getAtk() - enemy.def);
            enemy.hp -= dmg;
            appendLog("You attack for " + dmg + " damage!");
        }
        isDefending = false;
    }

    private void useSkill() {
        if (player.skills.isEmpty()) {
            appendLog("No skills available!");
            return;
        }

        String skill = (String) JOptionPane.showInputDialog(
                this,
                "Select skill to use:",
                "Use Skill",
                JOptionPane.PLAIN_MESSAGE,
                null,
                player.skills.toArray(),
                player.skills.get(0));

        if (skill == null) {
            return;
        }

        int mastery = player.skillMastery.getOrDefault(skill, 1);
        int skillPower = 10 + mastery * 5;

        if (player.skillCooldown.get(skill) > 0) {
            appendLog(skill + " is on cooldown!");
            return;
        }

        if (player.qi < 10) {
            appendLog("Not enough Qi!");
            return;
        }

        int dmg = Math.max(1, player.getAtk() + skillPower - enemy.def);
        enemy.hp -= dmg;
        appendLog("You use " + skill + " for " + dmg + " damage!");
        player.gainSkillExp(skill, 5);
        player.qi -= 10;
        player.skillCooldown.put(skill, 2);
        isDefending = false;
    }

    private void defend() {
        appendLog("You take a defensive stance! Next attack will be halved.");
        isDefending = true;
    }

    private void useItem() {
        player.inventoryMenu();
        isDefending = false;
        updateStats();
    }

    private void attemptFlee() {
        if (Math.random() < 0.5) {
            appendLog("You fled successfully!");
            dispose();
        } else {
            appendLog("Failed to flee!");
        }
    }

    private void enemyTurn() {
        if (Math.random() < 0.1) {
            appendLog(enemy.name + " missed!");
        } else {
            int dmg = Math.max(1, enemy.atk - player.getDef());
            if (isDefending) {
                dmg = (dmg + 1) / 2;
                appendLog("You block! Damage halved.");
                isDefending = false;
            }
            player.hp -= dmg;
            appendLog(enemy.name + " attacks for " + dmg + " damage!");
        }
    }

    private void battleWon() {
        appendLog("Victory! You defeated " + enemy.name);
        player.gainExp(15 + 7 * enemy.realmIndex);
        GameV4e.achievements.add("First Victory");
        Battle.checkQuestProgress(player, "defeat", enemy.sect);

        JOptionPane.showMessageDialog(this,
                "You have defeated " + enemy.name + "!\nGained " + (15 + 7 * enemy.realmIndex) + " EXP.",
                "Victory",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void battleLost() {
        appendLog("You have been defeated...");
        player.hp = player.getMaxHp() / 2;

        JOptionPane.showMessageDialog(this,
                "You were defeated by " + enemy.name + ".\nYou wake up with half your HP.",
                "Defeat",
                JOptionPane.WARNING_MESSAGE);

        dispose();
    }

    private void appendLog(String text) {
        battleLog.append(text + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BattleDialogue.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(() -> {
            try {
                Player player = new Player();
                Enemy enemy = new Enemy(player, "");
                new BattleDialogue(null, player, enemy).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error initializing battle: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
