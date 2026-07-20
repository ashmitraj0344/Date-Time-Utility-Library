package ui.panels;

import utility.AgeCalculator;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;

public class AgeCalculatorPanel extends JPanel implements ThemeManager.ThemeListener {
    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private JTextField dobField;
    private JLabel resultAgeLabel;
    private JLabel daysLabel;
    private JLabel weeksLabel;
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JLabel secondsLabel;
    private JLabel nextBirthdayLabel;

    private RoundedPanel inputCard;
    private RoundedPanel ageResultCard;
    private RoundedPanel statsCard;
    private RoundedPanel birthdayCard;

    public AgeCalculatorPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("Age Calculator");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("Calculate your exact age and track upcoming birthday milestones.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Body Content
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Side: Inputs (width weight 0.35)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 20);
        inputCard = createInputCard();
        body.add(inputCard, gbc);

        // Right Side: Results (width weight 0.65)
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel resultsPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        resultsPanel.setOpaque(false);

        ageResultCard = createAgeResultCard();
        statsCard = createStatsCard();
        birthdayCard = createBirthdayCard();

        resultsPanel.add(ageResultCard);
        resultsPanel.add(statsCard);
        resultsPanel.add(birthdayCard);
        body.add(resultsPanel, gbc);

        add(body, BorderLayout.CENTER);

        // Initial calculation
        performCalculation();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private RoundedPanel createInputCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Enter Date of Birth");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(title, gbc);

        JLabel label = new JLabel("DOB (yyyy-MM-dd):");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(label, gbc);

        dobField = new JTextField("2000-01-01");
        dobField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dobField.setPreferredSize(new Dimension(150, 35));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(dobField, gbc);

        ModernButton calcBtn = new ModernButton("Calculate Age");
        calcBtn.setPreferredSize(new Dimension(150, 40));
        calcBtn.addActionListener(e -> performCalculation());
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(calcBtn, gbc);

        // Spacer to push everything to top
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        card.add(Box.createVerticalGlue(), gbc);

        return card;
    }

    private RoundedPanel createAgeResultCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Precise Age");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));

        resultAgeLabel = new JLabel("0 Years, 0 Months, 0 Days");
        resultAgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        resultAgeLabel.setForeground(ThemeManager.getAccent());
        resultAgeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(title, BorderLayout.NORTH);
        card.add(resultAgeLabel, BorderLayout.CENTER);
        return card;
    }

    private RoundedPanel createStatsCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Life Statistics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 5, 10, 0));
        grid.setOpaque(false);

        daysLabel = createStatItem(grid, "Total Days", "0");
        weeksLabel = createStatItem(grid, "Total Weeks", "0");
        hoursLabel = createStatItem(grid, "Total Hours", "0");
        minutesLabel = createStatItem(grid, "Total Minutes", "0");
        secondsLabel = createStatItem(grid, "Total Seconds", "0");

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStatItem(JPanel container, String title, String value) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(vLbl);
        p.add(Box.createRigidArea(new Dimension(0, 3)));
        p.add(tLbl);
        p.add(Box.createVerticalGlue());

        container.add(p);
        return vLbl;
    }

    private RoundedPanel createBirthdayCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Countdown to Next Birthday");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));

        nextBirthdayLabel = new JLabel("0 Months, 0 Days");
        nextBirthdayLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nextBirthdayLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(title, BorderLayout.NORTH);
        card.add(nextBirthdayLabel, BorderLayout.CENTER);
        return card;
    }

    private void performCalculation() {
        String dob = dobField.getText().trim();
        AgeCalculator.AgeResult res = AgeCalculator.calculateAge(dob);
        if (res != null) {
            resultAgeLabel.setText(res.toString());
            daysLabel.setText(String.format("%,d", res.totalDays));
            weeksLabel.setText(String.format("%,d", res.totalWeeks));
            hoursLabel.setText(String.format("%,d", res.totalHours));
            minutesLabel.setText(String.format("%,d", res.totalMinutes));
            secondsLabel.setText(String.format("%,d", res.totalSeconds));
            nextBirthdayLabel.setText(AgeCalculator.getNextBirthdayCountdown(dob));
        } else {
            resultAgeLabel.setText("Invalid Date or Date is in future");
            daysLabel.setText("-");
            weeksLabel.setText("-");
            hoursLabel.setText("-");
            minutesLabel.setText("-");
            secondsLabel.setText("-");
            nextBirthdayLabel.setText("-");
        }
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();
        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        // Input card text coloring
        for (Component c : inputCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textPrimary);
            }
        }
        dobField.setBackground(ThemeManager.getSidebarBackground());
        dobField.setForeground(textPrimary);
        dobField.setCaretColor(textPrimary);
        dobField.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder()));

        // Result coloring
        for (Component c : ageResultCard.getComponents()) {
            if (c instanceof JLabel && c != resultAgeLabel) {
                c.setForeground(textSecondary);
            }
        }
        resultAgeLabel.setForeground(ThemeManager.getAccent());

        // Birthday coloring
        for (Component c : birthdayCard.getComponents()) {
            if (c instanceof JLabel && c != nextBirthdayLabel) {
                c.setForeground(textSecondary);
            }
        }
        nextBirthdayLabel.setForeground(ThemeManager.getSuccess());

        // Stats labels coloring
        for (Component c : statsCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textSecondary);
            }
            if (c instanceof JPanel) {
                JPanel grid = (JPanel) c;
                for (Component cell : grid.getComponents()) {
                    if (cell instanceof JPanel) {
                        JPanel cellP = (JPanel) cell;
                        for (Component lbl : cellP.getComponents()) {
                            if (lbl instanceof JLabel) {
                                JLabel l = (JLabel) lbl;
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
    }
}
