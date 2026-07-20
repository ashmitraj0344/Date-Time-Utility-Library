package ui.panels;

import ui.ThemeManager;
import ui.custom.RoundedPanel;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel implements ThemeManager.ThemeListener {
    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private RoundedPanel descriptionCard;
    private JLabel descTitle;
    private JTextArea descText;

    private JPanel teamGrid;

    private static class TeamMember {
        String initials;
        String name;
        String role;
        String email;
        String mobile;

        TeamMember(String initials, String name, String role, String email, String mobile) {
            this.initials = initials;
            this.name = name;
            this.role = role;
            this.email = email;
            this.mobile = mobile;
        }
    }

    private final TeamMember[] members = {
        new TeamMember("AR", "Ashmit Raj", "Lead UI Architect & App Coordinator", "ashmitr0344@gmail.com", "7463853747"),
        new TeamMember("DB", "Dinesh Bagariya", "Date Utility logic & Views", "dineshchoudhary5762@gmail.com", "7878983196"),
        new TeamMember("DS", "Deepti Singh", "Time Utility logic & Views", "deepti.singh142001@gmail.com", "9431506823"),
        new TeamMember("AR", "Aniket Raj", "Age Calculator Specialist", "aniketttt012@gmail.com", "9006478446"),
        new TeamMember("BS", "Bhumika Suryawanshi", "Data Model & Storage Manager", "bhumikasuryawanshi5@gmail.com", "8080053660"),
        new TeamMember("SM", "Soumili Mandal", "Event Scheduler Controller & Alerts", "mendesoumili2543@gmail.com", "7044899197"),
        new TeamMember("AK", "Aditya Kumar", "Calendar View Component Designer", "adityakumar2472006@gmail.com", "6207670098"),
        new TeamMember("AD", "Aman Dwivedi", "Forms, Tables & Layout Builder", "amandubey1089@gmail.com", "8887811385")
    };

    public AboutPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("About Project");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("Information about the application developers and architecture.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content (Description Card + Team Members Grid)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);

        // Description Card
        descriptionCard = new RoundedPanel(15);
        descriptionCard.setLayout(new BoxLayout(descriptionCard, BoxLayout.Y_AXIS));
        descriptionCard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        descTitle = new JLabel("Date & Time Utility Library + Event Scheduler v1.0.0");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        descText = new JTextArea("An advanced, high-performance desktop application designed to provide " +
                "seamless date calculations, time utility calculations, precise birthday milestones tracker, and a robust " +
                "local event scheduler. Built strictly using Java Standard libraries (Swing/AWT) for portability, with " +
                "custom styling to match modern application look-and-feels including support for Light and Dark modes.");
        descText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descText.setLineWrap(true);
        descText.setWrapStyleWord(true);
        descText.setEditable(false);
        descText.setOpaque(false);

        descriptionCard.add(descTitle);
        descriptionCard.add(Box.createRigidArea(new Dimension(0, 5)));
        descriptionCard.add(descText);

        contentPanel.add(descriptionCard, BorderLayout.NORTH);

        // Team Grid Title
        JPanel gridWrapper = new JPanel(new BorderLayout(0, 10));
        gridWrapper.setOpaque(false);

        JLabel teamTitle = new JLabel("Development Team - Group 04");
        teamTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        teamTitle.setForeground(ThemeManager.getAccent());
        gridWrapper.add(teamTitle, BorderLayout.NORTH);

        // Grid (2 rows, 4 columns)
        teamGrid = new JPanel(new GridLayout(2, 4, 12, 12));
        teamGrid.setOpaque(false);

        for (TeamMember m : members) {
            teamGrid.add(createMemberCard(m));
        }
        gridWrapper.add(teamGrid, BorderLayout.CENTER);

        contentPanel.add(gridWrapper, BorderLayout.CENTER);

        // Wrap inside a ScrollPane
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private RoundedPanel createMemberCard(TeamMember member) {
        RoundedPanel card = new RoundedPanel(12);
        card.setCustomBgColor(ThemeManager.getSidebarBackground());
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Avatar circle
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getAccent());
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(member.initials)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(member.initials, x, y);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setMinimumSize(new Dimension(40, 40));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.fill = GridBagConstraints.NONE;
        card.add(avatar, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        JLabel nameLbl = new JLabel(member.name, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 3, 0);
        card.add(nameLbl, gbc);

        // Role
        JLabel roleLbl = new JLabel(member.role, SwingConstants.CENTER);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        roleLbl.setForeground(ThemeManager.getTextSecondary());
        gbc.gridy = 2;
        card.add(roleLbl, gbc);

        // Email
        JLabel emailLbl = new JLabel(member.email, SwingConstants.CENTER);
        emailLbl.setFont(new Font("Segoe UI", Font.ITALIC, 9));
        emailLbl.setForeground(ThemeManager.getTextSecondary());
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 0, 1, 0);
        card.add(emailLbl, gbc);

        // Mobile
        JLabel mobLbl = new JLabel("📱 " + member.mobile, SwingConstants.CENTER);
        mobLbl.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        mobLbl.setForeground(ThemeManager.getTextSecondary());
        gbc.gridy = 4;
        card.add(mobLbl, gbc);

        return card;
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();

        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        descTitle.setForeground(textPrimary);
        descText.setForeground(textSecondary);

        // Re-color grid items
        for (Component c : teamGrid.getComponents()) {
            if (c instanceof RoundedPanel) {
                RoundedPanel card = (RoundedPanel) c;
                card.setCustomBgColor(ThemeManager.getSidebarBackground());
                for (Component sub : card.getComponents()) {
                    if (sub instanceof JLabel) {
                        JLabel l = (JLabel) sub;
                        if (l.getFont().isBold()) {
                            l.setForeground(textPrimary);
                        } else {
                            l.setForeground(textSecondary);
                        }
                    }
                }
            }
        }
    }
}
