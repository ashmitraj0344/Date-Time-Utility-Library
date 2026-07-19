package ui;

import scheduler.EventScheduler;
import model.Event;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddEvent extends JDialog {
    private final EventScheduler scheduler;
    private final Event existingEvent; // Null if adding new
    private boolean saved = false;

    private JTextField titleField;
    private JTextArea descArea;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField locField;
    private JComboBox<String> priorityCombo;
    private JCheckBox reminderCheckbox;
    private JComboBox<String> reminderTimeCombo;

    public AddEvent(Window parent, EventScheduler scheduler, Event existingEvent) {
        super(parent, existingEvent == null ? "Add Event" : "Edit Event", ModalityType.APPLICATION_MODAL);
        this.scheduler = scheduler;
        this.existingEvent = existingEvent;

        setSize(420, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        initComponents();
        populateFields();
        applyTheme();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Title
        JLabel titleLbl = new JLabel("Event Title*");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 0;
        form.add(titleLbl, gbc);

        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 1;
        form.add(titleField, gbc);

        // Description
        JLabel descLbl = new JLabel("Description");
        descLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2;
        form.add(descLbl, gbc);

        descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        gbc.gridy = 3;
        form.add(descScroll, gbc);

        // Date & Time (Row side-by-side)
        JPanel dateTimeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        dateTimeRow.setOpaque(false);

        JPanel dateCol = new JPanel(new BorderLayout(0, 5));
        dateCol.setOpaque(false);
        JLabel dateLbl = new JLabel("Date (yyyy-MM-dd)*");
        dateLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateField = new JTextField(LocalDate.now().toString());
        dateField.setPreferredSize(new Dimension(0, 32));
        dateCol.add(dateLbl, BorderLayout.NORTH);
        dateCol.add(dateField, BorderLayout.CENTER);

        JPanel timeCol = new JPanel(new BorderLayout(0, 5));
        timeCol.setOpaque(false);
        JLabel timeLbl = new JLabel("Time (HH:mm)*");
        timeLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setPreferredSize(new Dimension(0, 32));
        timeCol.add(timeLbl, BorderLayout.NORTH);
        timeCol.add(timeField, BorderLayout.CENTER);

        dateTimeRow.add(dateCol);
        dateTimeRow.add(timeCol);

        gbc.gridy = 4;
        form.add(dateTimeRow, gbc);

        // Location
        JLabel locLbl = new JLabel("Location");
        locLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 5;
        form.add(locLbl, gbc);

        locField = new JTextField();
        locField.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 6;
        form.add(locField, gbc);

        // Priority & Reminder (Row side-by-side)
        JPanel priorityRemRow = new JPanel(new GridLayout(1, 2, 10, 0));
        priorityRemRow.setOpaque(false);

        JPanel priorityCol = new JPanel(new BorderLayout(0, 5));
        priorityCol.setOpaque(false);
        JLabel prioLbl = new JLabel("Priority");
        prioLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityCombo.setSelectedIndex(1); // Default Medium
        priorityCombo.setPreferredSize(new Dimension(0, 32));
        priorityCol.add(prioLbl, BorderLayout.NORTH);
        priorityCol.add(priorityCombo, BorderLayout.CENTER);

        JPanel remCol = new JPanel(new BorderLayout(0, 5));
        remCol.setOpaque(false);
        JLabel remLbl = new JLabel("Set Reminder");
        remLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reminderCheckbox = new JCheckBox("Notify me");
        reminderCheckbox.setPreferredSize(new Dimension(0, 32));
        remCol.add(remLbl, BorderLayout.NORTH);
        remCol.add(reminderCheckbox, BorderLayout.CENTER);

        priorityRemRow.add(priorityCol);
        priorityRemRow.add(remCol);

        gbc.gridy = 7;
        form.add(priorityRemRow, gbc);

        // Reminder Time Offset
        JLabel remOffsetLbl = new JLabel("Reminder Offset Time");
        remOffsetLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 8;
        form.add(remOffsetLbl, gbc);

        reminderTimeCombo = new JComboBox<>(new String[]{
                "At time of event",
                "15 min before",
                "30 min before",
                "1 hour before",
                "1 day before"
        });
        reminderTimeCombo.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 9;
        form.add(reminderTimeCombo, gbc);

        add(form, BorderLayout.CENTER);

        // --- Bottom Buttons Row ---
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btns.setOpaque(false);
        ModernButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(95, 35));
        cancelBtn.setCustomColors(new Color(148, 163, 184), new Color(203, 213, 225), Color.WHITE);
        cancelBtn.addActionListener(e -> dispose());

        ModernButton saveBtn = new ModernButton("Save Event");
        saveBtn.setPreferredSize(new Dimension(110, 35));
        saveBtn.addActionListener(e -> saveEvent());

        btns.add(cancelBtn);
        btns.add(saveBtn);
        add(btns, BorderLayout.SOUTH);
    }

    private void populateFields() {
        if (existingEvent == null) return;

        titleField.setText(existingEvent.getTitle());
        descArea.setText(existingEvent.getDescription());
        dateField.setText(existingEvent.getDate());
        timeField.setText(existingEvent.getTime());
        locField.setText(existingEvent.getLocation());
        priorityCombo.setSelectedItem(existingEvent.getPriority());
        reminderCheckbox.setSelected(existingEvent.isReminder());
        reminderTimeCombo.setSelectedItem(existingEvent.getReminderTime());
    }

    private void applyTheme() {
        Color bg = ThemeManager.getCardBackground();
        Color fg = ThemeManager.getTextPrimary();
        Color borderCol = ThemeManager.getBorder();
        Color textInputBg = ThemeManager.getSidebarBackground();

        getContentPane().setBackground(bg);

        // Style Labels, Text Fields, combos
        styleComponent(titleField, textInputBg, fg, borderCol);
        styleComponent(dateField, textInputBg, fg, borderCol);
        styleComponent(timeField, textInputBg, fg, borderCol);
        styleComponent(locField, textInputBg, fg, borderCol);

        descArea.setBackground(textInputBg);
        descArea.setForeground(fg);
        descArea.setCaretColor(fg);
        descArea.setBorder(BorderFactory.createLineBorder(borderCol));

        priorityCombo.setBackground(textInputBg);
        priorityCombo.setForeground(fg);

        reminderTimeCombo.setBackground(textInputBg);
        reminderTimeCombo.setForeground(fg);

        reminderCheckbox.setOpaque(false);
        reminderCheckbox.setForeground(fg);

        // Recurse set foreground for labels
        setPanelLabelsColor(getContentPane(), fg);
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

    private void saveEvent() {
        String title = titleField.getText().trim();
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate Date & Time format
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

        if (existingEvent == null) {
            // Create New Event
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
        } else {
            // Edit existing
            existingEvent.setTitle(title);
            existingEvent.setDescription(descArea.getText().trim());
            existingEvent.setDate(dateStr);
            existingEvent.setTime(timeStr);
            existingEvent.setLocation(locField.getText().trim());
            existingEvent.setPriority((String) priorityCombo.getSelectedItem());
            existingEvent.setReminder(reminderCheckbox.isSelected());
            existingEvent.setReminderTime((String) reminderTimeCombo.getSelectedItem());
            scheduler.updateEvent(existingEvent);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
