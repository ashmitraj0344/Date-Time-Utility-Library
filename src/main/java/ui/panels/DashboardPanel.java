package ui.panels;

import scheduler.EventScheduler;
import model.Event;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardPanel extends JPanel implements ThemeManager.ThemeListener {
    private final EventScheduler scheduler;
    private final FrameNavigation navigator;

    private JLabel welcomeLabel;
    private JLabel subtitleLabel;
    private JLabel dateLabel;
    private JLabel clockLabel;

    private JLabel todayCountLabel;
    private JLabel upcomingCountLabel;
    private JLabel reminderCountLabel;

    private RoundedPanel welcomeCard;
    private RoundedPanel clockCard;
    private RoundedPanel todayCard;
    private RoundedPanel upcomingCard;
    private RoundedPanel reminderCard;
    private RoundedPanel quickActionsCard;

    private String profileName = "USER";

    public interface FrameNavigation {
        void navigateTo(String tabName);
        void openAddEventDialog();
    }

    public DashboardPanel(EventScheduler scheduler, FrameNavigation navigator) {
        this.scheduler = scheduler;
        this.navigator = navigator;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        startClock();
        updateEventCounts();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    public void setProfileName(String name) {
        this.profileName = name;
        welcomeLabel.setText("Welcome, " + profileName + " 👋");
    }

    public String getProfileName() {
        return profileName;
    }

    private void initComponents() {
        // --- TOP AREA: Welcome & Clock ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setOpaque(false);

        // Welcome panel
        welcomeCard = new RoundedPanel(20);
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
        welcomeCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        welcomeLabel = new JLabel("Welcome, " + profileName + " 👋");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        subtitleLabel = new JLabel("Smart tools to manage your time and events.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        welcomeCard.add(welcomeLabel);
        welcomeCard.add(Box.createRigidArea(new Dimension(0, 8)));
        welcomeCard.add(subtitleLabel);

        // Clock panel
        clockCard = new RoundedPanel(20);
        clockCard.setLayout(new BoxLayout(clockCard, BoxLayout.Y_AXIS));
        clockCard.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        dateLabel = new JLabel("Loading date...");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockLabel = new JLabel("00:00:00 AM");
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockCard.add(Box.createVerticalGlue());
        clockCard.add(dateLabel);
        clockCard.add(Box.createRigidArea(new Dimension(0, 5)));
        clockCard.add(clockLabel);
        clockCard.add(Box.createVerticalGlue());

        topPanel.add(welcomeCard);
        topPanel.add(clockCard);

        // --- MIDDLE AREA: Status Cards ---
        JPanel middlePanel = new JPanel(new GridLayout(1, 3, 20, 0));
        middlePanel.setOpaque(false);

        // Today's Events Card
        todayCard = new RoundedPanel(20);
        todayCard.setLayout(new BorderLayout());
        todayCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel todayTitle = new JLabel("Today's Events");
        todayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        todayCountLabel = new JLabel("0");
        todayCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        todayCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JButton todayBtn = createViewButton("View Today's Events ->");
        todayBtn.addActionListener(e -> navigator.navigateTo("Calendar View"));
        todayCard.add(todayTitle, BorderLayout.NORTH);
        todayCard.add(todayCountLabel, BorderLayout.CENTER);
        todayCard.add(todayBtn, BorderLayout.SOUTH);

        // Upcoming Events Card
        upcomingCard = new RoundedPanel(20);
        upcomingCard.setLayout(new BorderLayout());
        upcomingCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel upcomingTitle = new JLabel("Upcoming Events");
        upcomingTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        upcomingCountLabel = new JLabel("0");
        upcomingCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        JButton upcomingBtn = createViewButton("View Upcoming ->");
        upcomingBtn.addActionListener(e -> navigator.navigateTo("All Events"));
        upcomingCard.add(upcomingTitle, BorderLayout.NORTH);
        upcomingCard.add(upcomingCountLabel, BorderLayout.CENTER);
        upcomingCard.add(upcomingBtn, BorderLayout.SOUTH);

        // Reminders Card
        reminderCard = new RoundedPanel(20);
        reminderCard.setLayout(new BorderLayout());
        reminderCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel reminderTitle = new JLabel("Active Reminders");
        reminderTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reminderCountLabel = new JLabel("0");
        reminderCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        JButton reminderBtn = createViewButton("View Reminders ->");
        reminderBtn.addActionListener(e -> navigator.navigateTo("All Events"));
        reminderCard.add(reminderTitle, BorderLayout.NORTH);
        reminderCard.add(reminderCountLabel, BorderLayout.CENTER);
        reminderCard.add(reminderBtn, BorderLayout.SOUTH);

        middlePanel.add(todayCard);
        middlePanel.add(upcomingCard);
        middlePanel.add(reminderCard);

        // --- BOTTOM AREA: Quick Actions ---
        quickActionsCard = new RoundedPanel(20);
        quickActionsCard.setLayout(new BorderLayout(10, 10));
        quickActionsCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel actionsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        actionsGrid.setOpaque(false);

        ModernButton addEventBtn = new ModernButton("Add New Event");
        addEventBtn.addActionListener(e -> navigator.openAddEventDialog());

        ModernButton viewCalBtn = new ModernButton("View Calendar");
        viewCalBtn.setCustomColors(new Color(59, 130, 246), new Color(96, 165, 250), Color.WHITE);
        viewCalBtn.addActionListener(e -> navigator.navigateTo("Calendar View"));

        ModernButton dateCalcBtn = new ModernButton("Date Calculator");
        dateCalcBtn.setCustomColors(new Color(16, 185, 129), new Color(52, 211, 153), Color.WHITE);
        dateCalcBtn.addActionListener(e -> navigator.navigateTo("Date Utilities"));

        ModernButton stopwatchBtn = new ModernButton("Start Stopwatch");
        stopwatchBtn.setCustomColors(new Color(245, 158, 11), new Color(251, 191, 36), Color.WHITE);
        stopwatchBtn.addActionListener(e -> navigator.navigateTo("Time Utilities"));

        actionsGrid.add(addEventBtn);
        actionsGrid.add(viewCalBtn);
        actionsGrid.add(dateCalcBtn);
        actionsGrid.add(stopwatchBtn);

        quickActionsCard.add(actionsTitle, BorderLayout.NORTH);
        quickActionsCard.add(actionsGrid, BorderLayout.CENTER);

        // Assemble Dashboard
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);
        mainContent.add(topPanel, BorderLayout.NORTH);
        mainContent.add(middlePanel, BorderLayout.CENTER);
        mainContent.add(quickActionsCard, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createViewButton(String text) {
        JButton btn = new JButton(text);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
            clockLabel.setText(now.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        });
        timer.start();
    }

    public void updateEventCounts() {
        List<Event> all = scheduler.getAllEvents();
        String todayStr = LocalDate.now().toString();

        int todayCount = 0;
        int upcomingCount = 0;
        int reminderCount = 0;

        LocalDate today = LocalDate.now();

        for (Event e : all) {
            try {
                LocalDate eventDate = LocalDate.parse(e.getDate());
                if (e.getDate().equals(todayStr)) {
                    todayCount++;
                }
                if (eventDate.isAfter(today) || eventDate.isEqual(today)) {
                    upcomingCount++;
                }
                if (e.isReminder()) {
                    reminderCount++;
                }
            } catch (Exception ex) {
                // Ignore parsing errors
            }
        }

        todayCountLabel.setText(String.valueOf(todayCount));
        upcomingCountLabel.setText(String.valueOf(upcomingCount));
        reminderCountLabel.setText(String.valueOf(reminderCount));
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();

        welcomeLabel.setForeground(textPrimary);
        subtitleLabel.setForeground(textSecondary);
        dateLabel.setForeground(ThemeManager.getAccent());
        clockLabel.setForeground(textPrimary);

        for (Component c : todayCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(c == todayCountLabel ? textPrimary : textSecondary);
            } else if (c instanceof JButton) {
                c.setForeground(ThemeManager.getAccent());
            }
        }

        for (Component c : upcomingCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(c == upcomingCountLabel ? textPrimary : textSecondary);
            } else if (c instanceof JButton) {
                c.setForeground(ThemeManager.getAccent());
            }
        }

        for (Component c : reminderCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(c == reminderCountLabel ? textPrimary : textSecondary);
            } else if (c instanceof JButton) {
                c.setForeground(ThemeManager.getAccent());
            }
        }

        for (Component c : quickActionsCard.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(textPrimary);
            }
        }
    }
}
