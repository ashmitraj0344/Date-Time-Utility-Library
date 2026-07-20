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
 private RoundedPanel createCurrentTimeCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Current Time");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        timeDisplay = new JLabel("00:00:00 AM");
        timeDisplay.setFont(new Font("Segoe UI", Font.BOLD, 28));
        timeDisplay.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controls.setOpaque(false);
        twelveHrRadio = new JRadioButton("12h", true);
        twentyFourHrRadio = new JRadioButton("24h");
        ButtonGroup group = new ButtonGroup();
        group.add(twelveHrRadio);
        group.add(twentyFourHrRadio);
        controls.add(twelveHrRadio);
        controls.add(twentyFourHrRadio);

        Timer t = new Timer(1000, e -> {
            String format = twelveHrRadio.isSelected() ? "hh:mm:ss a" : "HH:mm:ss";
            timeDisplay.setText(TimeUtility.getCurrentTime(format));
        });
        t.start();

        card.add(title, BorderLayout.NORTH);
        card.add(timeDisplay, BorderLayout.CENTER);
        card.add(controls, BorderLayout.SOUTH);
        return card;
    }

 private RoundedPanel createStopwatchCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Stopwatch");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        stopwatchDisplay = new JLabel("00:00:00.000");
        stopwatchDisplay.setFont(new Font("Segoe UI", Font.BOLD, 22));
        stopwatchDisplay.setHorizontalAlignment(SwingConstants.CENTER);

        lapListModel = new DefaultListModel<>();
        lapList = new JList<>(lapListModel);
        JScrollPane scroll = new JScrollPane(lapList);
        scroll.setPreferredSize(new Dimension(100, 60));
        scroll.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder()));

        JPanel btns = new JPanel(new GridLayout(1, 4, 2, 0));
        btns.setOpaque(false);
        ModernButton startBtn = new ModernButton("Start");
        ModernButton stopBtn = new ModernButton("Stop");
        ModernButton lapBtn = new ModernButton("Lap");
        ModernButton resetBtn = new ModernButton("Reset");

        stopwatchTimer = new Timer(10, e -> {
            long now = System.currentTimeMillis();
            long elapsed = stopwatchElapsedTime + (now - stopwatchStartTime);
            long ms = elapsed % 1000;
            long secs = (elapsed / 1000) % 60;
            long mins = (elapsed / 60000) % 60;
            long hrs = (elapsed / 3600000);
            stopwatchDisplay.setText(String.format("%02d:%02d:%02d.%03d", hrs, mins, secs, ms));
        });

        startBtn.addActionListener(e -> {
            if (!stopwatchRunning) {
                stopwatchStartTime = System.currentTimeMillis();
                stopwatchTimer.start();
                stopwatchRunning = true;
            }
        });

        stopBtn.addActionListener(e -> {
            if (stopwatchRunning) {
                stopwatchElapsedTime += (System.currentTimeMillis() - stopwatchStartTime);
                stopwatchTimer.stop();
                stopwatchRunning = false;
            }
        });

        lapBtn.addActionListener(e -> {
            if (stopwatchRunning) {
                lapListModel.addElement(stopwatchDisplay.getText());
            }
        });

        resetBtn.addActionListener(e -> {
            stopwatchTimer.stop();
            stopwatchRunning = false;
            stopwatchElapsedTime = 0;
            stopwatchDisplay.setText("00:00:00.000");
            lapListModel.clear();
        });

        btns.add(startBtn);
        btns.add(stopBtn);
        btns.add(lapBtn);
        btns.add(resetBtn);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setOpaque(false);
        centerPanel.add(stopwatchDisplay, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);

        card.add(title, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    private RoundedPanel createCountdownCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Countdown Timer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel setupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        setupPanel.setOpaque(false);
        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        minSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 59, 1));
        secSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        setupPanel.add(new JLabel("H:"));
        setupPanel.add(hourSpinner);
        setupPanel.add(new JLabel("M:"));
        setupPanel.add(minSpinner);
        setupPanel.add(new JLabel("S:"));
        setupPanel.add(secSpinner);

        countdownDisplay = new JLabel("00:05:00");
        countdownDisplay.setFont(new Font("Segoe UI", Font.BOLD, 22));
        countdownDisplay.setHorizontalAlignment(SwingConstants.CENTER);

        countdownProgress = new JProgressBar(0, 100);
        countdownProgress.setValue(100);

        countdownTimer = new Timer(1000, e -> {
            if (countdownRemainingSeconds > 0) {
                countdownRemainingSeconds--;
                int h = countdownRemainingSeconds / 3600;
                int m = (countdownRemainingSeconds % 3600) / 60;
                int s = countdownRemainingSeconds % 60;
                countdownDisplay.setText(String.format("%02d:%02d:%02d", h, m, s));
                
                int percent = (int) (((double) countdownRemainingSeconds / countdownTotalSeconds) * 100);
                countdownProgress.setValue(percent);
            } else {
                countdownTimer.stop();
                countdownRunning = false;
                countdownDisplay.setText("Finished!");
                countdownProgress.setValue(0);
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Countdown Finished!", "Alert", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btns = new JPanel(new GridLayout(1, 3, 5, 0));
        btns.setOpaque(false);
        ModernButton startBtn = new ModernButton("Start");
        ModernButton pauseBtn = new ModernButton("Pause");
        ModernButton resetBtn = new ModernButton("Reset");

        startBtn.addActionListener(e -> {
            if (!countdownRunning) {
                if (countdownRemainingSeconds <= 0) {
                    int h = (int) hourSpinner.getValue();
                    int m = (int) minSpinner.getValue();
                    int s = (int) secSpinner.getValue();
                    countdownTotalSeconds = h * 3600 + m * 60 + s;
                    countdownRemainingSeconds = countdownTotalSeconds;
                }
                if (countdownTotalSeconds > 0) {
                    countdownTimer.start();
                    countdownRunning = true;
                }
            }
        });
