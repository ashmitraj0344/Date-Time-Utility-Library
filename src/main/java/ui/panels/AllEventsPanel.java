package ui.panels;

import scheduler.EventScheduler;
import model.Event;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;
import ui.AddEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class AllEventsPanel extends JPanel implements ThemeManager.ThemeListener {
    private final EventScheduler scheduler;
    private final DashboardPanel.FrameNavigation navigator;

    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private JTextField searchField;
    private JComboBox<String> priorityFilter;
    private JTable eventTable;
    private DefaultTableModel tableModel;

    private ModernButton addEventBtn;
    private ModernButton editBtn;
    private ModernButton deleteBtn;

    public AllEventsPanel(EventScheduler scheduler, DashboardPanel.FrameNavigation navigator) {
        this.scheduler = scheduler;
        this.navigator = navigator;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshTableData();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private void initComponents() {
        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        headerTitle = new JLabel("All Events");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("View and manage all your scheduled events.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        titlePanel.add(headerTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(headerSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Actions toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbar.setOpaque(false);

        addEventBtn = new ModernButton("Add Event");
        addEventBtn.setPreferredSize(new Dimension(110, 35));
        addEventBtn.addActionListener(e -> navigator.openAddEventDialog());

        editBtn = new ModernButton("Edit");
        editBtn.setPreferredSize(new Dimension(80, 35));
        editBtn.setCustomColors(new Color(59, 130, 246), new Color(96, 165, 250), Color.WHITE);
        editBtn.addActionListener(e -> editSelectedEvent());

        deleteBtn = new ModernButton("Delete");
        deleteBtn.setPreferredSize(new Dimension(80, 35));
        deleteBtn.setCustomColors(ThemeManager.getDanger(), ThemeManager.getDanger().brighter(), Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelectedEvent());

        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(addEventBtn);

        headerPanel.add(toolbar, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Filter Panel & Table Container ---
        RoundedPanel container = new RoundedPanel(15);
        container.setLayout(new BorderLayout(15, 15));
        container.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filterBar = new JPanel(new BorderLayout(15, 0));
        filterBar.setOpaque(false);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Search events...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refreshTableData();
            }
        });

        priorityFilter = new JComboBox<>(new String[]{"All Priorities", "Low", "Medium", "High"});
        priorityFilter.setPreferredSize(new Dimension(140, 35));
        priorityFilter.addActionListener(e -> refreshTableData());

        filterBar.add(searchField, BorderLayout.CENTER);
        filterBar.add(priorityFilter, BorderLayout.EAST);
        container.add(filterBar, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Title", "Date", "Time", "Location", "Priority", "Reminder"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(tableModel);
        eventTable.setRowHeight(35);
        eventTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        eventTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        eventTable.getTableHeader().setReorderingAllowed(false);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        eventTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventTable.getColumnModel().getColumn(0).setWidth(0);

        // Priority rendering styling
        eventTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                if (value != null) {
                    String priority = (String) value;
                    if (isSelected) {
                        lbl.setForeground(Color.WHITE);
                    } else {
                        if ("High".equalsIgnoreCase(priority)) lbl.setForeground(ThemeManager.getDanger());
                        else if ("Medium".equalsIgnoreCase(priority)) lbl.setForeground(ThemeManager.getWarning());
                        else if ("Low".equalsIgnoreCase(priority)) lbl.setForeground(ThemeManager.getSuccess());
                    }
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(null);
        container.add(scrollPane, BorderLayout.CENTER);

        add(container, BorderLayout.CENTER);
    }

    public void refreshTableData() {
        tableModel.setRowCount(0);
        String query = searchField.getText().trim();
        String priorityFilterVal = (String) priorityFilter.getSelectedItem();

        List<Event> filtered = scheduler.searchEvents(query);
        for (Event e : filtered) {
            if (priorityFilterVal != null && !priorityFilterVal.equals("All Priorities")) {
                if (!e.getPriority().equalsIgnoreCase(priorityFilterVal)) {
                    continue;
                }
            }
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getTitle(),
                    e.getDate(),
                    e.getTime(),
                    e.getLocation(),
                    e.getPriority(),
                    e.isReminder() ? "🔔 " + e.getReminderTime() : "Off"
            });
        }
    }

    private void editSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String eventId = (String) tableModel.getValueAt(selectedRow, 0);
        Event eventToEdit = null;
        for (Event e : scheduler.getAllEvents()) {
            if (e.getId().equals(eventId)) {
                eventToEdit = e;
                break;
            }
        }

        if (eventToEdit != null) {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            AddEvent dialog = new AddEvent(parentWindow, scheduler, eventToEdit);
            dialog.setVisible(true);
            refreshTableData();
            navigator.navigateTo("Dashboard"); // Trigger dashboard updates
            navigator.navigateTo("All Events");
        }
    }

    private void deleteSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String eventId = (String) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the event \"" + title + "\"?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            scheduler.deleteEvent(eventId);
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Event deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();
        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        searchField.setBackground(ThemeManager.getSidebarBackground());
        searchField.setForeground(textPrimary);
        searchField.setCaretColor(textPrimary);
        searchField.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder()));

        priorityFilter.setBackground(ThemeManager.getSidebarBackground());
        priorityFilter.setForeground(textPrimary);

        eventTable.setBackground(ThemeManager.getCardBackground());
        eventTable.setForeground(textPrimary);
        eventTable.setGridColor(ThemeManager.getBorder());

        eventTable.getTableHeader().setBackground(ThemeManager.getSidebarBackground());
        eventTable.getTableHeader().setForeground(textPrimary);

        // Customize selected row color
        eventTable.setSelectionBackground(ThemeManager.getAccent());
        eventTable.setSelectionForeground(Color.WHITE);

        refreshTableData();
    }
}
