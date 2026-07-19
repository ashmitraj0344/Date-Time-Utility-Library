package ui;

import scheduler.EventScheduler;
import ui.panels.*;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends JFrame implements ThemeManager.ThemeListener, DashboardPanel.FrameNavigation {
    private final EventScheduler scheduler;

    private JPanel sidebar;
    private JPanel contentContainer;
    private CardLayout cardLayout;

    private JLabel appTitle;
    private JLabel groupLabel;

    private final Map<String, ModernButton> navButtons = new HashMap<>();
    private String activeTab = "Dashboard";

    // Sub-panels
    private DashboardPanel dashboardPanel;
    private DateUtilityPanel dateUtilityPanel;
    private TimeUtilityPanel timeUtilityPanel;
    private AgeCalculatorPanel ageCalculatorPanel;
    private EventSchedulerPanel eventSchedulerPanel;
    private CalendarPanel calendarPanel;
    private AllEventsPanel allEventsPanel;
    private SettingsPanel settingsPanel;
    private AboutPanel aboutPanel;

    public Dashboard() {
        this.scheduler = new EventScheduler();

        setTitle("Date & Time Utility Library + Event Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 600));

        initComponents();
        setupReminders();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- Sidebar (Left Panel) ---
        sidebar = new JPanel();
        sidebar.setLayout(new GridBagLayout());
        sidebar.setPreferredSize(new Dimension(265, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Sidebar top: Logo & title
        JPanel titlePanel = new JPanel(new BorderLayout(5, 5));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        appTitle = new JLabel("<html><body style='width: 195px;'><b>Date & Time Utility</b><br><font size=3 color='#94a3b8'>+ Event Scheduler</font></body></html>");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titlePanel.add(appTitle, BorderLayout.CENTER);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        sidebar.add(titlePanel, gbc);

        // Sidebar middle: Nav items stacked
        JPanel navContainer = new JPanel(new GridLayout(9, 1, 0, 8));
        navContainer.setOpaque(false);
        navContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        String[] tabs = {
                "Dashboard",
                "Date Utilities",
                "Time Utilities",
                "Age Calculator",
                "Event Scheduler",
                "Calendar View",
                "All Events",
                "Settings",
                "About"
        };

        for (String tab : tabs) {
            ModernButton btn = new ModernButton(tab);
            btn.setPreferredSize(new Dimension(0, 45));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(0, 15, 0, 0));
            btn.addActionListener(e -> navigateTo(tab));
            navButtons.put(tab, btn);
            navContainer.add(btn);
        }

        gbc.gridy = 1;
        sidebar.add(navContainer, gbc);

        // Sidebar bottom: spacer + group info
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        sidebar.add(Box.createVerticalGlue(), gbc);

        groupLabel = new JLabel("Group - 04 (Java Project)", SwingConstants.CENTER);
        groupLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 15, 0);
        sidebar.add(groupLabel, gbc);

        add(sidebar, BorderLayout.WEST);

        // --- Content Panel Container (Right Card Layout) ---
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setOpaque(true);

        // Instantiating all main sub-panels
        dashboardPanel = new DashboardPanel(scheduler, this);
        dateUtilityPanel = new DateUtilityPanel();
        timeUtilityPanel = new TimeUtilityPanel();
        ageCalculatorPanel = new AgeCalculatorPanel();
        eventSchedulerPanel = new EventSchedulerPanel(scheduler, this);
        calendarPanel = new CalendarPanel(scheduler);
        allEventsPanel = new AllEventsPanel(scheduler, this);
        settingsPanel = new SettingsPanel(dashboardPanel);
        aboutPanel = new AboutPanel();

        contentContainer.add(dashboardPanel, "Dashboard");
        contentContainer.add(dateUtilityPanel, "Date Utilities");
        contentContainer.add(timeUtilityPanel, "Time Utilities");
        contentContainer.add(ageCalculatorPanel, "Age Calculator");
        contentContainer.add(eventSchedulerPanel, "Event Scheduler");
        contentContainer.add(calendarPanel, "Calendar View");
        contentContainer.add(allEventsPanel, "All Events");
        contentContainer.add(settingsPanel, "Settings");
        contentContainer.add(aboutPanel, "About");

        add(contentContainer, BorderLayout.CENTER);

        // Set active nav highlight
        updateSidebarActiveHighlight();
    }

    private void updateSidebarActiveHighlight() {
        Color activeBg = ThemeManager.getAccent();
        Color activeFg = Color.WHITE;
        Color idleBg = new Color(0, 0, 0, 0);
        Color idleFg = ThemeManager.getTextPrimary();

        for (Map.Entry<String, ModernButton> entry : navButtons.entrySet()) {
            ModernButton btn = entry.getValue();
            if (entry.getKey().equals(activeTab)) {
                btn.setCustomColors(activeBg, activeBg.brighter(), activeFg);
            } else {
                btn.setCustomColors(idleBg, ThemeManager.getCardBackground(), idleFg);
            }
        }
    }

    private void setupReminders() {
        scheduler.addReminderListener(event -> SwingUtilities.invokeLater(() -> {
            String message = String.format(
                    "🔔 EVENT REMINDER!\n\nTitle: %s\nTime: %s\nLocation: %s\nPriority: %s\nDescription: %s",
                    event.getTitle(),
                    event.getTime(),
                    event.getLocation(),
                    event.getPriority(),
                    event.getDescription()
            );
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Upcoming Event: " + event.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }));
    }

    @Override
    public void navigateTo(String tabName) {
        this.activeTab = tabName;
        cardLayout.show(contentContainer, tabName);
        updateSidebarActiveHighlight();

        // Refresh panel specific data if needed
        if ("Dashboard".equals(tabName)) {
            dashboardPanel.updateEventCounts();
        } else if ("Calendar View".equals(tabName)) {
            calendarPanel.refresh();
        } else if ("All Events".equals(tabName)) {
            allEventsPanel.refreshTableData();
        }
    }

    @Override
    public void openAddEventDialog() {
        AddEvent dialog = new AddEvent(this, scheduler, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            dashboardPanel.updateEventCounts();
            allEventsPanel.refreshTableData();
            calendarPanel.refresh();
        }
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color sidebarBg = ThemeManager.getSidebarBackground();
        Color contentBg = ThemeManager.getBackground();
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();

        sidebar.setBackground(sidebarBg);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeManager.getBorder()));
        contentContainer.setBackground(contentBg);

        appTitle.setForeground(textPrimary);
        groupLabel.setForeground(textSecondary);

        updateSidebarActiveHighlight();

        // Standard JFrame title bar and menu configurations if needed
        UIManager.put("OptionPane.background", ThemeManager.getCardBackground());
        UIManager.put("OptionPane.messageForeground", textPrimary);
        UIManager.put("Panel.background", ThemeManager.getCardBackground());
    }

    @Override
    public void dispose() {
        scheduler.stop(); // Stop scheduler daemon threads
        super.dispose();
    }
}
