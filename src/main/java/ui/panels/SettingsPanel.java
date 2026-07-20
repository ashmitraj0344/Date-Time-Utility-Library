package ui.panels;

import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel implements ThemeManager.ThemeListener {
    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private RoundedPanel themeCard;
    private RoundedPanel profileCard;
    private RoundedPanel infoCard;

    private JRadioButton darkRadio;
    private JRadioButton lightRadio;
    private JTextField nameInput;
    
    private final DashboardPanel dashboardPanel;

    public SettingsPanel(DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("Settings");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("Manage application preferences, profile settings, and themes.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridLayout(3, 1, 0, 15));
        content.setOpaque(false);

        themeCard = createThemeCard();
        profileCard = createProfileCard();
        infoCard = createInfoCard();

        content.add(themeCard);
        content.add(profileCard);
        content.add(infoCard);

        add(content, BorderLayout.CENTER);

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private RoundedPanel createThemeCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Theme Configuration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(title, gbc);

        JPanel radios = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        radios.setOpaque(false);

        darkRadio = new JRadioButton("Dark Mode Theme", ThemeManager.isDarkMode());
        lightRadio = new JRadioButton("Light Mode Theme", !ThemeManager.isDarkMode());
        ButtonGroup bg = new ButtonGroup();
        bg.add(darkRadio);
        bg.add(lightRadio);

        darkRadio.addActionListener(e -> ThemeManager.setDarkMode(true));
        lightRadio.addActionListener(e -> ThemeManager.setDarkMode(false));

        radios.add(darkRadio);
        radios.add(lightRadio);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(radios, gbc);

        return card;
    }

    private RoundedPanel createProfileCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(title, gbc);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JLabel nameLbl = new JLabel("Display Name: ");
        nameInput = new JTextField(dashboardPanel.getProfileName());
        nameInput.setPreferredSize(new Dimension(200, 32));

        ModernButton saveBtn = new ModernButton("Save Profile");
        saveBtn.setPreferredSize(new Dimension(110, 32));
        saveBtn.addActionListener(e -> {
            String name = nameInput.getText().trim();
            if (!name.isEmpty()) {
                dashboardPanel.setProfileName(name);
                JOptionPane.showMessageDialog(this, "Profile name updated to \"" + name + "\".", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        row.add(nameLbl);
        row.add(nameInput);
        row.add(saveBtn);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(row, gbc);

        return card;
    }

    private RoundedPanel createInfoCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Class & Group Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(title, gbc);

        JPanel details = new JPanel(new GridLayout(2, 1, 2, 2));
        details.setOpaque(false);

        JLabel groupLbl = new JLabel("Group ID: Group - 04");
        groupLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel courseLbl = new JLabel("Course: JAVA Programming with GenAI And System Design Live");
        courseLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        details.add(groupLbl);
        details.add(courseLbl);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(details, gbc);

        return card;
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();
        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        // Theme card colors
        for (Component c : themeCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textPrimary);
            }
            if (c instanceof JPanel) {
                for (Component r : ((JPanel) c).getComponents()) {
                    if (r instanceof JRadioButton) {
                        r.setForeground(textPrimary);
                        ((JRadioButton) r).setOpaque(false);
                    }
                }
            }
        }
        darkRadio.setSelected(isDark);
        lightRadio.setSelected(!isDark);

        // Profile card colors
        for (Component c : profileCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textPrimary);
            }
            if (c instanceof JPanel) {
                for (Component r : ((JPanel) c).getComponents()) {
                    if (r instanceof JLabel) {
                        r.setForeground(textPrimary);
                    }
                }
            }
        }
        nameInput.setBackground(ThemeManager.getSidebarBackground());
        nameInput.setForeground(textPrimary);
        nameInput.setCaretColor(textPrimary);
        nameInput.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder()));

        // Info card colors
        for (Component c : infoCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textPrimary);
            }
            if (c instanceof JPanel) {
                for (Component r : ((JPanel) c).getComponents()) {
                    if (r instanceof JLabel) {
                        r.setForeground(textSecondary);
                    }
                }
            }
        }
    }
}

