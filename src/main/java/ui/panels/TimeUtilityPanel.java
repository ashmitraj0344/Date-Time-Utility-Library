package ui.panels;

import utility.TimeUtility;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtilityPanel extends JPanel implements ThemeManager.ThemeListener {
    private JLabel headerTitle;
    private JLabel headerSubtitle;

    // Current Time components
    private JLabel timeDisplay;
    private JRadioButton twelveHrRadio;
    private JRadioButton twentyFourHrRadio;

    // Stopwatch components
    private JLabel stopwatchDisplay;
    private DefaultListModel<String> lapListModel;
    private JList<String> lapList;
    private Timer stopwatchTimer;
    private long stopwatchStartTime = 0;
    private long stopwatchElapsedTime = 0;
    private boolean stopwatchRunning = false;

    // Countdown components
    private JSpinner hourSpinner;
    private JSpinner minSpinner;
    private JSpinner secSpinner;
    private JLabel countdownDisplay;
    private JProgressBar countdownProgress;
    private Timer countdownTimer;
    private int countdownTotalSeconds = 0;
    private int countdownRemainingSeconds = 0;
    private boolean countdownRunning = false;

    public TimeUtilityPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("Time Utilities");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerSubtitle = new JLabel("Useful tools to work with time.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Body Content (Grid of Cards)
        JPanel grid = new JPanel(new GridLayout(2, 3, 15, 15));
        grid.setOpaque(false);

        grid.add(createCurrentTimeCard());
        grid.add(createStopwatchCard());
        grid.add(createCountdownCard());
        grid.add(createTimeDifferenceCard());
        grid.add(createConverterCard());
        grid.add(createAddSubtractTimeCard());

        add(grid, BorderLayout.CENTER);

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

