package com.mycompany.gamev4e;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.stream.Collectors;

public class GameWindow extends javax.swing.JFrame {

    private Player player;
    private JTextArea mainOutput;
    private JPanel actionPanel;
    private JPanel statsPanel;
    private ImageIcon backgroundImage;

    public GameWindow() {
        initCustomComponents();
        setTitle("Return of the Mount Hua Sect - Cultivation RPG");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/mount_hua_bg.jpg"));
        } catch (Exception e) {
            backgroundImage = null;
        }

        promptPlayerSetup();
    }

    private void initCustomComponents() {
        // Main container with layered panes for background and content
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900, 700));

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback background
                    g.setColor(new Color(20, 20, 40));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setBounds(0, 0, 900, 700);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBounds(0, 0, 900, 700);

        // Create UI components
        createMainOutputArea();
        createActionButtons();
        createStatsDisplay();

        // Add components to content panel
        contentPanel.add(mainOutput, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        contentPanel.add(statsPanel, BorderLayout.EAST);

        // Add to layered pane
        layeredPane.add(backgroundPanel, Integer.valueOf(0));
        layeredPane.add(contentPanel, Integer.valueOf(1));

        setContentPane(layeredPane);
    }

    private void createMainOutputArea() {
        mainOutput = new JTextArea();
        mainOutput.setEditable(false);
        mainOutput.setFont(new Font("KaiTi", Font.PLAIN, 14));
        mainOutput.setForeground(Color.WHITE);
        mainOutput.setOpaque(false);
        mainOutput.setLineWrap(true);
        mainOutput.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(mainOutput);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Wrap in a panel for styling
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setOpaque(false);
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add title label
        JLabel titleLabel = new JLabel("Cultivation Journey");
        titleLabel.setFont(new Font("KaiTi", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        outputPanel.add(titleLabel, BorderLayout.NORTH);

        this.mainOutput = mainOutput;
    }

       private void createActionButtons() {
        actionPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Updated button labels with cultivation theme
        String[] buttonLabels = {
            "Cultivation", "Travel", "Quests", "Train",
            "Inventory", "Meditate", "Achievements", "Exit"
        };

        for (String label : buttonLabels) {
            JButton button = createStyledButton(label);
            button.addActionListener(this::handleButtonAction);
            
            // Special styling for Meditate (Rest) button
            if (label.equals("Meditate")) {
                button.setBackground(new Color(80, 60, 40, 200));
                button.setForeground(new Color(255, 215, 0));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            
            actionPanel.add(button);
        }
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("KaiTi", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 100, 200));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 90, 120, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 100, 200));
            }
        });

        return button;
    }

    private void createStatsDisplay() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add title
        JLabel statsTitle = new JLabel("Cultivation Status");
        statsTitle.setFont(new Font("KaiTi", Font.BOLD, 16));
        statsTitle.setForeground(new Color(255, 215, 0));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(statsTitle);

        // Add separator
        statsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Stats will be updated dynamically
    }

    private void updateStatsDisplay() {
        statsPanel.removeAll();

        // Add title
        JLabel statsTitle = new JLabel("Cultivation Status");
        statsTitle.setFont(new Font("KaiTi", Font.BOLD, 18));
        statsTitle.setForeground(new Color(255, 215, 0));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(statsTitle);

        // Add separator with decorative element
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(180, 160, 140));
        statsPanel.add(sep);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (player != null) {
            // Player info with sect colors
            JLabel nameLabel = createStatLabel("Disciple: " + player.name, new Color(220, 220, 255));
            JLabel sectLabel = createStatLabel("Sect: " + player.sect, getSectColor(player.sect));
            JLabel realmLabel = createStatLabel("Realm: " + player.getRealm(), getRealmColor(player.realmIndex));

            statsPanel.add(nameLabel);
            statsPanel.add(sectLabel);
            statsPanel.add(realmLabel);
            statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            // Vital stats with progress bars
            statsPanel.add(createStatWithBar("HP: ", player.hp, player.getMaxHp(), Color.RED));
            statsPanel.add(createStatWithBar("Qi: ", player.qi, player.getMaxQi(), Color.CYAN));
            statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Combat stats
            JLabel atkLabel = createStatLabel("Attack: " + player.getAtk(), Color.WHITE);
            JLabel defLabel = createStatLabel("Defense: " + player.getDef(), Color.WHITE);
            statsPanel.add(atkLabel);
            statsPanel.add(defLabel);
            statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Progress
            JLabel expLabel = createStatLabel("Cultivation: " + player.exp + "/" + player.expToLevel, Color.WHITE);
            JLabel silverLabel = createStatLabel("Silver: " + player.silver + " taels", new Color(255, 215, 0));
            statsPanel.add(expLabel);
            statsPanel.add(silverLabel);
        }

        statsPanel.revalidate();
        statsPanel.repaint();
    }
    private Color getSectColor(String sect) {
        // Return different colors for different sects
        if (sect.contains("Hua")) return new Color(255, 100, 100);
        if (sect.contains("Union")) return new Color(100, 255, 100);
        if (sect.contains("Edge")) return new Color(100, 100, 255);
        return Color.WHITE;
    }
    private Color getRealmColor(int realmIndex) {
        // Color based on realm level
        return switch (realmIndex) {
            case 0 -> new Color(200, 200, 200); // Qi Refining
            case 1 -> new Color(150, 255, 150); // Foundation
            case 2 -> new Color(100, 200, 255); // Core Formation
            case 3 -> new Color(255, 150, 255); // Nascent Soul
            default -> new Color(255, 215, 0); // Higher realms
        };
    }

    private JPanel createStatWithBar(String label, int current, int max, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel textLabel = new JLabel(label + current + "/" + max);
        textLabel.setFont(new Font("KaiTi", Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);
        
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(current);
        bar.setForeground(color);
        bar.setBackground(new Color(50, 50, 70));
        bar.setBorder(BorderFactory.createEmptyBorder());
        bar.setPreferredSize(new Dimension(150, 10));
        
        panel.add(textLabel, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        
        return panel;
    }


    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("KaiTi", Font.PLAIN, 14));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void promptPlayerSetup() {
        // Name input
        String playerName = JOptionPane.showInputDialog(this,
                "Enter your martial name:",
                "Character Creation",
                JOptionPane.PLAIN_MESSAGE);

        if (playerName == null || playerName.isBlank()) {
            System.exit(0);
        }

        // Sect selection
        JPanel sectPanel = new JPanel(new BorderLayout());
        sectPanel.add(new JLabel("Choose your sect:"), BorderLayout.NORTH);

        JList<String> sectList = new JList<>(GameV4e.SECTS);
        sectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sectList.setLayoutOrientation(JList.VERTICAL);
        sectList.setVisibleRowCount(5);
        sectList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("KaiTi", Font.PLAIN, 14));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(sectList);
        sectPanel.add(scrollPane, BorderLayout.CENTER);

        // Lore display
        JTextArea loreArea = new JTextArea();
        loreArea.setEditable(false);
        loreArea.setLineWrap(true);
        loreArea.setWrapStyleWord(true);
        loreArea.setFont(new Font("KaiTi", Font.ITALIC, 12));
        loreArea.setBackground(new Color(240, 240, 240));
        loreArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedSect = sectList.getSelectedValue();
                loreArea.setText(GameV4e.SECT_LORE.getOrDefault(selectedSect, "No information available."));
            }
        });

        sectPanel.add(loreArea, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, sectPanel,
                "Choose Your Sect", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            System.exit(0);
        }

        String selectedSect = sectList.getSelectedValue();
        if (selectedSect == null) {
            selectedSect = GameV4e.SECTS[0];
        }

        // Initialize player
        player = new Player();
        player.name = playerName.trim();
        player.sect = selectedSect;
        player.sectIndex = java.util.Arrays.asList(GameV4e.SECTS).indexOf(player.sect);

        for (String skill : GameV4e.SECT_SKILLS[player.sectIndex]) {
            player.skills.add(skill);
            player.skillMastery.put(skill, 1);
            player.skillExp.put(skill, 0);
            player.skillCooldown.put(skill, 0);
        }

        appendOutput("Welcome, " + player.name + " of the " + player.sect + "!");
        appendOutput("\nYou begin your cultivation journey at the " + player.getRealm() + " realm.");
        appendOutput("May your sword reach the peak of Mount Hua!");

        updateStatsDisplay();
    }

     private void handleButtonAction(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();

        switch (command) {
            case "Cultivation":
                showPlayerStatus();
                break;
            case "Travel":
                showTravelMenu();
                break;
            case "Quests":
                showQuests();
                break;
            case "Train":
                practiceSkill();
                break;
            case "Inventory":
                showInventory();
                break;
            case "Meditate":
                player.rest();
                appendOutput("\nYou sit in meditation beneath the plum blossoms...");
                appendOutput("Your HP and Qi have been fully restored!");
                updateStatsDisplay();
                break;
            // [Other cases remain the same...]
        }
    }
    private void showPlayerStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Status ---\n");
        sb.append("Name: ").append(player.name)
                .append("   Sect: ").append(player.sect)
                .append("   Realm: ").append(player.getRealm()).append("\n");
        sb.append("HP: ").append(player.hp).append("/").append(player.getMaxHp())
                .append("  Qi: ").append(player.qi).append("/").append(player.getMaxQi()).append("\n");
        sb.append("Atk: ").append(player.getAtk()).append("  Def: ").append(player.getDef())
                .append("  Silver: ").append(player.silver).append("  Weight: ").append(player.getTotalWeight()).append("\n");

        sb.append("\nSkills:\n");
        for (String skill : player.skills) {
            sb.append("- ").append(skill)
                    .append(" (Lv.").append(player.skillMastery.getOrDefault(skill, 1))
                    .append(") (CD:").append(player.skillCooldown.get(skill)).append(")\n");
        }

        sb.append("\nEquipment: ");
        sb.append(player.equipped.values().stream()
                .map(eqt -> eqt.name)
                .collect(Collectors.joining(", ")));

        sb.append("\n\nInventory: ");
        sb.append(player.inventory.stream()
                .map(ie -> ie.item.name + "(x" + ie.count + ")")
                .collect(Collectors.joining(", ")));

        appendOutput(sb.toString());
    }

    private void showTravelMenu() {
        JDialog travelDialog = new JDialog(this, "Travel", true);
        travelDialog.setLayout(new BorderLayout());
        travelDialog.setSize(500, 400);
        travelDialog.setLocationRelativeTo(this);

        JPanel locationPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(locationPanel);

        for (Location loc : GameV4e.LOCATIONS) {
            JButton locButton = new JButton(loc.name);
            locButton.setFont(new Font("KaiTi", Font.PLAIN, 14));
            locButton.addActionListener(ev -> {
                travelDialog.dispose();
                visitLocation(loc);
            });

            JPanel locPanel = new JPanel(new BorderLayout());
            locPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            locPanel.setBackground(new Color(70, 70, 100, 150));

            JLabel nameLabel = new JLabel(loc.name);
            nameLabel.setFont(new Font("KaiTi", Font.BOLD, 14));
            nameLabel.setForeground(Color.WHITE);

            JTextArea descArea = new JTextArea(loc.lore);
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setFont(new Font("KaiTi", Font.PLAIN, 12));
            descArea.setForeground(Color.WHITE);
            descArea.setOpaque(false);
            descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            locPanel.add(nameLabel, BorderLayout.NORTH);
            locPanel.add(descArea, BorderLayout.CENTER);
            locPanel.add(locButton, BorderLayout.SOUTH);

            locationPanel.add(locPanel);
        }

        travelDialog.add(scrollPane, BorderLayout.CENTER);
        travelDialog.setVisible(true);
    }

    private void visitLocation(Location loc) {
        appendOutput("\n-- " + loc.name + " --");
        appendOutput(loc.lore);

        if (GameV4e.LOCATION_HOSTILE_SECT.containsKey(loc.name)) {
            String sect = GameV4e.LOCATION_HOSTILE_SECT.get(loc.name);
            if (new Random().nextBoolean()) {
                appendOutput("\nA hostile " + sect + " cultivator appears!");
                 Battle.battle(player, new Enemy(player, loc.name));
                updateStatsDisplay();
            }
        }

        if (!loc.npcs.isEmpty()) {
            appendOutput("\nNotable figures present:");
            for (Npc npc : loc.npcs) {
                appendOutput("- " + npc.name + " (" + npc.role + ")");
            }

            Object[] options = {"Talk", "Spar", "Request Quest", "Leave"};
            int choice = JOptionPane.showOptionDialog(this,
                    "How would you like to interact with " + loc.name + "?",
                    "Location Interaction",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) { // Talk
                Npc npc = loc.npcs.get(0); // For simplicity, interact with first NPC
                appendOutput("\n" + npc.name + ": \"" + npc.dialogue + "\"");
            } else if (choice == 1) { // Spar
                appendOutput("\nYou spar with a local cultivator!");
                Enemy npcEnemy = new Enemy(player, "");
                npcEnemy.name = loc.npcs.get(0).name;
                npcEnemy.sect = loc.npcs.get(0).role;
                BattleDialogue battle = new BattleDialogue(this, player, npcEnemy);
                battle.setVisible(true);
                updateStatsDisplay();
            } else if (choice == 2) { // Request Quest
                Quest quest = new Quest("Defeat Hostile " + loc.name,
                        "Defeat 3 enemies from " + loc.name,
                        "defeat", loc.name, 3);
                player.activeQuests.add(quest);
                appendOutput("\nReceived quest: " + quest.title);
            }
        }
    }

    private void showQuests() {
        if (player.activeQuests.isEmpty()) {
            appendOutput("No active quests.");
            return;
        }

        JDialog questDialog = new JDialog(this, "Active Quests", true);
        questDialog.setLayout(new BorderLayout());
        questDialog.setSize(600, 450);
        questDialog.setLocationRelativeTo(this);

        JPanel questPanel = new JPanel();
        questPanel.setLayout(new BoxLayout(questPanel, BoxLayout.Y_AXIS));
        questPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questPanel.setBackground(new Color(40, 40, 60));

        for (Quest q : player.activeQuests) {
            JPanel singleQuestPanel = new JPanel(new BorderLayout());
            singleQuestPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            singleQuestPanel.setBackground(new Color(60, 60, 80));

            // Quest title with completion status
            JLabel titleLabel = new JLabel(
                    "<html><b>" + q.title + "</b>"
                    + (q.completed ? " <font color='#00FF00'>(Completed)</font>"
                            : " <font color='#FFFF00'>(" + q.progress + "/" + q.needed + ")</font></html>"));
            titleLabel.setFont(new Font("KaiTi", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);

            // Quest description
            JTextArea descArea = new JTextArea(q.description);
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setFont(new Font("KaiTi", Font.PLAIN, 14));
            descArea.setForeground(Color.LIGHT_GRAY);
            descArea.setOpaque(false);
            descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Quest type and target
            JLabel detailsLabel = new JLabel(
                    "<html><i>Type:</i> " + q.type + " | <i>Target:</i> " + q.target + "</html>"
            );
            detailsLabel.setFont(new Font("KaiTi", Font.ITALIC, 12));
            detailsLabel.setForeground(new Color(200, 200, 255));

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setOpaque(false);
            infoPanel.add(titleLabel, BorderLayout.NORTH);
            infoPanel.add(descArea, BorderLayout.CENTER);
            infoPanel.add(detailsLabel, BorderLayout.SOUTH);

            // Reward indicator
            if (q.completed && !q.rewardGiven) {
                JButton claimButton = new JButton("Claim Reward");
                claimButton.setFont(new Font("KaiTi", Font.PLAIN, 12));
                claimButton.addActionListener(e -> {
                    q.rewardGiven = true;
                    player.silver += 30;
                    player.addItem("Qi Pill", 1);
                    appendOutput("Quest reward claimed: 30 silver and a Qi Pill!");
                    GameV4e.achievements.add("Completed " + q.title);
                    questDialog.dispose();
                    showQuests(); // Refresh
                    updateStatsDisplay();
                });
                singleQuestPanel.add(claimButton, BorderLayout.EAST);
            }

            singleQuestPanel.add(infoPanel, BorderLayout.CENTER);
            questPanel.add(singleQuestPanel);
            questPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(questPanel);
        scrollPane.getViewport().setBackground(new Color(40, 40, 60));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        questDialog.add(scrollPane, BorderLayout.CENTER);
        questDialog.setVisible(true);
    }

    private void practiceSkill() {
        if (player.skills.isEmpty()) {
            appendOutput("No skills to practice.");
            return;
        }

        JDialog practiceDialog = new JDialog(this, "Practice Skill", true);
        practiceDialog.setLayout(new BorderLayout());
        practiceDialog.setSize(400, 300);
        practiceDialog.setLocationRelativeTo(this);

        JPanel skillPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        for (String skill : player.skills) {
            JButton skillButton = new JButton(skill + " (Lv." + player.skillMastery.get(skill) + ")");
            skillButton.setFont(new Font("KaiTi", Font.PLAIN, 14));
            skillButton.addActionListener(ev -> {
                int mastery = player.skillMastery.get(skill) + 1;
                player.skillMastery.put(skill, mastery);
                player.qi = Math.max(0, player.qi - 5);
                GameV4e.achievements.add("Practiced " + skill);
                appendOutput("You practice " + skill + " and mastery improves to level " + mastery + "!");
                practiceDialog.dispose();
                updateStatsDisplay();
            });
            skillPanel.add(skillButton);
        }

        practiceDialog.add(new JScrollPane(skillPanel), BorderLayout.CENTER);
        practiceDialog.setVisible(true);
    }

    private void showInventory() {
        if (player.inventory.isEmpty()) {
            appendOutput("Inventory is empty.");
            return;
        }

        JDialog inventoryDialog = new JDialog(this, "Inventory", true);
        inventoryDialog.setLayout(new BorderLayout());
        inventoryDialog.setSize(500, 400);
        inventoryDialog.setLocationRelativeTo(this);

        JPanel itemPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        for (InventoryEntry ie : player.inventory) {
            JPanel entryPanel = new JPanel(new BorderLayout());
            entryPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
            entryPanel.setBackground(new Color(70, 70, 100, 150));

            JLabel nameLabel = new JLabel(ie.item.name + " (x" + ie.count + ")");
            nameLabel.setFont(new Font("KaiTi", Font.BOLD, 14));
            nameLabel.setForeground(Color.WHITE);

            JLabel typeLabel = new JLabel("Type: " + ie.item.type);
            typeLabel.setFont(new Font("KaiTi", Font.PLAIN, 12));
            typeLabel.setForeground(Color.WHITE);

            JButton useButton = new JButton("Use");
            useButton.setFont(new Font("KaiTi", Font.PLAIN, 12));
            useButton.addActionListener(ev -> {
                if (ie.item.type.equals("usable")) {
                    if (ie.item.name.equals("Qi Pill")) {
                        player.qi = Math.min(player.getMaxQi(), player.qi + 15);
                        appendOutput("Used Qi Pill. Qi restored by 15.");
                    } else if (ie.item.name.equals("Vitality Elixir")) {
                        player.hp = Math.min(player.getMaxHp(), player.hp + 20);
                        appendOutput("Used Vitality Elixir. HP restored by 20.");
                    } else if (ie.item.name.equals("Wild Ginseng")) {
                        player.hp = Math.min(player.getMaxHp(), player.hp + 10);
                        player.qi = Math.min(player.getMaxQi(), player.qi + 10);
                        appendOutput("Used Wild Ginseng. HP and Qi restored by 10.");
                    }
                    player.useItem(ie.item.name);
                    inventoryDialog.dispose();
                    showInventory(); // Refresh
                    updateStatsDisplay();
                } else if (ie.item.type.equals("equipment")) {
                    for (Equipment eq : Equipment.shopEquipment) {
                        if (eq.name.equals(ie.item.name)) {
                            player.equip(eq);
                            player.useItem(ie.item.name);
                            inventoryDialog.dispose();
                            updateStatsDisplay();
                            return;
                        }
                    }
                } else {
                    appendOutput("Can't use that now.");
                }
            });

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setOpaque(false);
            infoPanel.add(nameLabel);
            infoPanel.add(typeLabel);

            entryPanel.add(infoPanel, BorderLayout.CENTER);
            entryPanel.add(useButton, BorderLayout.EAST);

            itemPanel.add(entryPanel);
        }

        inventoryDialog.add(new JScrollPane(itemPanel), BorderLayout.CENTER);
        inventoryDialog.setVisible(true);
    }

    private void showAchievements() {
        if (GameV4e.achievements.isEmpty()) {
            appendOutput("No achievements yet.");
            return;
        }

        JDialog achievementDialog = new JDialog(this, "Achievements", true);
        achievementDialog.setLayout(new BorderLayout());
        achievementDialog.setSize(400, 300);
        achievementDialog.setLocationRelativeTo(this);

        JPanel achievementPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        for (String achievement : GameV4e.achievements) {
            JLabel achievementLabel = new JLabel("★ " + achievement);
            achievementLabel.setFont(new Font("KaiTi", Font.PLAIN, 14));
            achievementLabel.setForeground(new Color(255, 215, 0));
            achievementPanel.add(achievementLabel);
        }

        achievementDialog.add(new JScrollPane(achievementPanel), BorderLayout.CENTER);
        achievementDialog.setVisible(true);
    }

    private void appendOutput(String text) {
        mainOutput.append(text + "\n");
        mainOutput.setCaretPosition(mainOutput.getDocument().getLength());
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
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            // Set Chinese-style font if available
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.messageFont", new Font("KaiTi", Font.PLAIN, 14));
            UIManager.put("OptionPane.buttonFont", new Font("KaiTi", Font.PLAIN, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
