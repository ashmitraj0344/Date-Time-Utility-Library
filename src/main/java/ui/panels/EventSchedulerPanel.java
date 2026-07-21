package ui.panels;

import scheduler.EventScheduler;
import model.Event;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EventSchedulerPanel extends JPanel implements ThemeManager.ThemeListener {
    private final EventScheduler scheduler;
    private final DashboardPanel.FrameNavigation navigator;

    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private JTextField titleField;
    private JTextArea descArea;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField locField;
    private JComboBox<String> priorityCombo;
    private JCheckBox reminderCheckbox;
    private JComboBox<String> reminderTimeCombo;

    private RoundedPanel formCard;

    public EventSchedulerPanel(EventScheduler scheduler, DashboardPanel.FrameNavigation navigator) {
        this.scheduler = scheduler;
        this.navigator = navigator;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("Event Scheduler - Add Event");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("Create a new event and configure notification reminders.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Body Form (using RoundedPanel for card layout)
        formCard = new RoundedPanel(15);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Row 1: Event Title & Description
        JPanel r1 = new JPanel(new GridLayout(1, 2, 20, 0));
        r1.setOpaque(false);

        JPanel titleCol = new JPanel(new BorderLayout(0, 5));
        titleCol.setOpaque(false);
        JLabel titleLbl = new JLabel("Event Title*");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(0, 35));
        titleCol.add(titleLbl, BorderLayout.NORTH);
        titleCol.add(titleField, BorderLayout.CENTER);

        JPanel descCol = new JPanel(new BorderLayout(0, 5));
        descCol.setOpaque(false);
        JLabel descLbl = new JLabel("Description");
        descLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descArea = new JTextArea(2, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(0, 50));
        descCol.add(descLbl, BorderLayout.NORTH);
        descCol.add(descScroll, BorderLayout.CENTER);

        r1.add(titleCol);
        r1.add(descCol);
        gbc.gridy = 0;
        formCard.add(r1, gbc);

        // Row 2: Date & Time
        JPanel r2 = new JPanel(new GridLayout(1, 2, 20, 0));
        r2.setOpaque(false);

        JPanel dateCol = new JPanel(new BorderLayout(0, 5));
        dateCol.setOpaque(false);
        JLabel dateLbl = new JLabel("Date (yyyy-MM-dd)*");
        dateLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateField = new JTextField(LocalDate.now().toString());
        dateField.setPreferredSize(new Dimension(0, 35));
        dateCol.add(dateLbl, BorderLayout.NORTH);
        dateCol.add(dateField, BorderLayout.CENTER);

        JPanel timeCol = new JPanel(new BorderLayout(0, 5));
        timeCol.setOpaque(false);
        JLabel timeLbl = new JLabel("Time (HH:mm)*");
        timeLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setPreferredSize(new Dimension(0, 35));
        timeCol.add(timeLbl, BorderLayout.NORTH);
        timeCol.add(timeField, BorderLayout.CENTER);

        r2.add(dateCol);
        r2.add(timeCol);
        gbc.gridy = 1;
        formCard.add(r2, gbc);

        // Row 3: Location & Priority
        JPanel r3 = new JPanel(new GridLayout(1, 2, 20, 0));
        r3.setOpaque(false);

        JPanel locCol = new JPanel(new BorderLayout(0, 5));
        locCol.setOpaque(false);
        JLabel locLbl = new JLabel("Location");
        locLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        locField = new JTextField();
        locField.setPreferredSize(new Dimension(0, 35));
        locCol.add(locLbl, BorderLayout.NORTH);
        locCol.add(locField, BorderLayout.CENTER);

        JPanel prioCol = new JPanel(new BorderLayout(0, 5));
        prioCol.setOpaque(false);
        JLabel prioLbl = new JLabel("Priority");
        prioLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityCombo.setSelectedIndex(1);
        priorityCombo.setPreferredSize(new Dimension(0, 35));
        prioCol.add(prioLbl, BorderLayout.NORTH);
        prioCol.add(priorityCombo, BorderLayout.CENTER);

        r3.add(locCol);
        r3.add(prioCol);
        gbc.gridy = 2;
        formCard.add(r3, gbc);

        // Row 4: Reminder Details
        JPanel r4 = new JPanel(new GridLayout(1, 2, 20, 0));
        r4.setOpaque(false);

        JPanel remChkCol = new JPanel(new BorderLayout(0, 5));
        remChkCol.setOpaque(false);
        JLabel remChkLbl = new JLabel("Set Reminder Alerts");
        remChkLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reminderCheckbox = new JCheckBox("Notify when event approaches");
        reminderCheckbox.setPreferredSize(new Dimension(0, 35));
        remChkCol.add(remChkLbl, BorderLayout.NORTH);
        remChkCol.add(reminderCheckbox, BorderLayout.CENTER);

        JPanel remTimeCol = new JPanel(new BorderLayout(0, 5));
        remTimeCol.setOpaque(false);
        JLabel remTimeLbl = new JLabel("Reminder Offset Time");
        remTimeLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reminderTimeCombo = new JComboBox<>(new String[]{
                "At time of event",
                "15 min before",
                "30 min before",
                "1 hour before",
                "1 day before"
        });
        reminderTimeCombo.setPreferredSize(new Dimension(0, 35));
        remTimeCol.add(remTimeLbl, BorderLayout.NORTH);
        remTimeCol.add(reminderTimeCombo, BorderLayout.CENTER);

        r4.add(remChkCol);
        r4.add(remTimeCol);
        gbc.gridy = 3;
        formCard.add(r4, gbc);

        // Row 5: Action Buttons
        JPanel r5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        r5.setOpaque(false);

        ModernButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(100, 38));
        cancelBtn.setCustomColors(new Color(148, 163, 184), new Color(203, 213, 225), Color.WHITE);
        cancelBtn.addActionListener(e -> resetForm());

        ModernButton saveBtn = new ModernButton("Save Event");
        saveBtn.setPreferredSize(new Dimension(120, 38));
        saveBtn.addActionListener(e -> saveNewEvent());

        r5.add(cancelBtn);
        r5.add(saveBtn);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        formCard.add(r5, gbc);

        // Add spacer to pull to top
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        formCard.add(Box.createVerticalGlue(), gbc);

        add(formCard, BorderLayout.CENTER);
    }

    private void resetForm() {
        titleField.setText("");
        descArea.setText("");
        dateField.setText(LocalDate.now().toString());
        timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        locField.setText("");
        priorityCombo.setSelectedIndex(1);
        reminderCheckbox.setSelected(false);
        reminderTimeCombo.setSelectedIndex(0);
    }

    private void saveNewEvent() {
        String title = titleField.getText().trim();
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate.parse(dateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date must match format yyyy-MM-dd (e.g. 2025-06-07).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalTime.parse(timeStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Time must match format HH:mm in 24h format (e.g. 14:30).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Event newEvent = new Event(
                UUID.randomUUID().toString(),
                title,
                descArea.getText().trim(),
                dateStr,
                timeStr,
                locField.getText().trim(),
                (String) priorityCombo.getSelectedItem(),
                reminderCheckbox.isSelected(),
                (String) reminderTimeCombo.getSelectedItem()
        );

        scheduler.addEvent(newEvent);
        JOptionPane.showMessageDialog(this, "Event successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        resetForm();

        // Refresh all panel counts & lists
        navigator.navigateTo("Dashboard");
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();
        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        Color textInputBg = ThemeManager.getSidebarBackground();
        Color borderCol = ThemeManager.getBorder();

        // Recurse color inputs
        styleComponent(titleField, textInputBg, textPrimary, borderCol);
        styleComponent(dateField, textInputBg, textPrimary, borderCol);
        styleComponent(timeField, textInputBg, textPrimary, borderCol);
        styleComponent(locField, textInputBg, textPrimary, borderCol);

        descArea.setBackground(textInputBg);
        descArea.setForeground(textPrimary);
        descArea.setCaretColor(textPrimary);
        descArea.setBorder(BorderFactory.createLineBorder(borderCol));

        priorityCombo.setBackground(textInputBg);
        priorityCombo.setForeground(textPrimary);

        reminderTimeCombo.setBackground(textInputBg);
        reminderTimeCombo.setForeground(textPrimary);

        reminderCheckbox.setOpaque(false);
        reminderCheckbox.setForeground(textPrimary);

        setPanelLabelsColor(formCard, textPrimary);
    }

    private void styleComponent(JComponent comp, Color bg, Color fg, Color border) {
        comp.setBackground(bg);
        comp.setForeground(fg);
        if (comp instanceof JTextField) {
            ((JTextField) comp).setCaretColor(fg);
        }
        comp.setBorder(BorderFactory.createLineBorder(border));
    }

    private void setPanelLabelsColor(Container c, Color color) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(color);
            } else if (comp instanceof Container) {
                setPanelLabelsColor((Container) comp, color);
            }
        }
    }
}
