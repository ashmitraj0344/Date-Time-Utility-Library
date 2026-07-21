package ui.panels;

import utility.DateUtility;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateUtilityPanel extends JPanel implements ThemeManager.ThemeListener {

    private JLabel headerTitle;
    private JLabel headerSubtitle;

    private JLabel currentDateDisplay;
    private JTextField currentDateFormatField;

    private JTextField diffDate1Field;
    private JTextField diffDate2Field;
    private JLabel diffResultLabel;

    private JTextField fmtIsoField;
    private JTextField fmtPatternField;
    private JLabel fmtResultLabel;


    private JTextField dowDateField;
    private JLabel dowResultLabel;

    private JTextField leapDateField;
    private JLabel leapResultLabel;

    private JTextField arithDateField;
    private JSpinner arithDaysSpinner;
    private JComboBox<String> arithUnitCombo;
    private JLabel arithResultLabel;


    public DateUtilityPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Header ────────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        headerTitle = new JLabel("Date Utilities");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        headerSubtitle = new JLabel("Useful tools to work with calendar dates.");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(headerSubtitle);

        add(headerPanel, BorderLayout.NORTH);

        // ── Body – 2 × 3 grid of cards ────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 3, 15, 15));
        grid.setOpaque(false);

        grid.add(createCurrentDateCard());
        grid.add(createDateDifferenceCard());
        grid.add(createFormatConverterCard());
        grid.add(createDayOfWeekCard());
        grid.add(createLeapYearCard());
        grid.add(createAddSubtractDaysCard());

        add(grid, BorderLayout.CENTER);

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());

        Timer liveTimer = new Timer(60_000, e -> refreshCurrentDate());
        liveTimer.setInitialDelay(0);
        liveTimer.start();
    }

    private RoundedPanel createCurrentDateCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Current Date");

        currentDateDisplay = new JLabel(DateUtility.getCurrentDate("EEEE, dd MMMM yyyy"));
        currentDateDisplay.setFont(new Font("Segoe UI", Font.BOLD, 17));
        currentDateDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        currentDateDisplay.setForeground(ThemeManager.getAccent());

        JPanel controls = new JPanel(new BorderLayout(5, 5));
        controls.setOpaque(false);

        currentDateFormatField = new JTextField("dd/MM/yyyy");
        currentDateFormatField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentDateFormatField.setToolTipText("Enter a date pattern, e.g. dd/MM/yyyy");

        ModernButton applyBtn = new ModernButton("Apply");
        applyBtn.addActionListener(e -> refreshCurrentDate());

        controls.add(currentDateFormatField, BorderLayout.CENTER);
        controls.add(applyBtn, BorderLayout.EAST);

        card.add(title, BorderLayout.NORTH);
        card.add(currentDateDisplay, BorderLayout.CENTER);
        card.add(controls, BorderLayout.SOUTH);
        return card;
    }

    private RoundedPanel createDateDifferenceCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Date Difference");

        JPanel body = new JPanel(new GridLayout(4, 1, 3, 3));
        body.setOpaque(false);

        JLabel lbl1 = new JLabel("Start date (yyyy-MM-dd):");
        lbl1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        diffDate1Field = new JTextField(LocalDate.now().minusYears(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        diffDate1Field.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lbl2 = new JLabel("End date (yyyy-MM-dd):");
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        diffDate2Field = new JTextField(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        diffDate2Field.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        body.add(lbl1);
        body.add(diffDate1Field);
        body.add(lbl2);
        body.add(diffDate2Field);

        diffResultLabel = new JLabel("Difference: —");
        diffResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);
        ModernButton calcBtn = new ModernButton("Calculate");
        calcBtn.addActionListener(e -> {
            String d1 = diffDate1Field.getText().trim();
            String d2 = diffDate2Field.getText().trim();
            long days = DateUtility.getDaysBetween(d1, d2);
            String readable = DateUtility.getReadableDifference(d1, d2);
            if (days == Long.MIN_VALUE) {
                diffResultLabel.setText("⚠ Invalid date(s)");
            } else {
                diffResultLabel.setText("<html><b>" + readable + "</b><br>(" + Math.abs(days) + " total days)</html>");
            }
        });
        south.add(diffResultLabel, BorderLayout.CENTER);
        south.add(calcBtn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private RoundedPanel createFormatConverterCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Format Converter");

        JPanel body = new JPanel(new GridLayout(4, 1, 3, 3));
        body.setOpaque(false);

        JLabel lbl1 = new JLabel("ISO Date (yyyy-MM-dd):");
        lbl1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fmtIsoField = new JTextField(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        fmtIsoField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lbl2 = new JLabel("Target pattern:");
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fmtPatternField = new JTextField("dd MMMM yyyy");
        fmtPatternField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        body.add(lbl1);
        body.add(fmtIsoField);
        body.add(lbl2);
        body.add(fmtPatternField);

        fmtResultLabel = new JLabel("Result: —");
        fmtResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        ModernButton convertBtn = new ModernButton("Convert");
        convertBtn.addActionListener(e -> {
            String result = DateUtility.formatDate(
                    fmtIsoField.getText().trim(),
                    fmtPatternField.getText().trim());
            fmtResultLabel.setText("Result: " + result);
        });

        JPanel south = new JPanel(new BorderLayout(5, 3));
        south.setOpaque(false);
        south.add(fmtResultLabel, BorderLayout.CENTER);
        south.add(convertBtn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private RoundedPanel createDayOfWeekCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Day of Week");

        JPanel body = new JPanel(new GridLayout(2, 1, 3, 3));
        body.setOpaque(false);

        JLabel lbl = new JLabel("Date (yyyy-MM-dd):");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dowDateField = new JTextField(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dowDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        body.add(lbl);
        body.add(dowDateField);

        dowResultLabel = new JLabel("Day: —");
        dowResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        dowResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dowResultLabel.setForeground(ThemeManager.getAccent());

        ModernButton checkBtn = new ModernButton("Check");
        checkBtn.addActionListener(e -> {
            String day = DateUtility.getDayOfWeek(dowDateField.getText().trim());
            dowResultLabel.setText(day);
        });

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);
        south.add(dowResultLabel, BorderLayout.CENTER);
        south.add(checkBtn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    private RoundedPanel createLeapYearCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Leap Year & Month Info");

        JPanel body = new JPanel(new GridLayout(2, 1, 3, 3));
        body.setOpaque(false);

        JLabel lbl = new JLabel("Date (yyyy-MM-dd):");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leapDateField = new JTextField(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        leapDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        body.add(lbl);
        body.add(leapDateField);

        leapResultLabel = new JLabel("—");
        leapResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        leapResultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ModernButton checkBtn = new ModernButton("Check");
        checkBtn.addActionListener(e -> {
            String date = leapDateField.getText().trim();
            if (!DateUtility.isValidDate(date)) {
                leapResultLabel.setText("<html><b>⚠ Invalid date</b></html>");
                leapResultLabel.setForeground(ThemeManager.getDanger());
                return;
            }
            boolean leap = DateUtility.isLeapYear(date);
            int days = DateUtility.getDaysInMonth(date);
            String leapText = leap ? "✔ Leap Year" : "✘ Not a Leap Year";
            Color leapColor = leap ? ThemeManager.getSuccess() : ThemeManager.getDanger();
            leapResultLabel.setText("<html><b>" + leapText + "</b><br>Days in month: " + days + "</html>");
            leapResultLabel.setForeground(leapColor);
        });

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);
        south.add(leapResultLabel, BorderLayout.CENTER);
        south.add(checkBtn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    /** Card 6 – adds or subtracts days / months / years from a date. */
    private RoundedPanel createAddSubtractDaysCard() {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = makeCardTitle("Add / Subtract");

        JPanel body = new JPanel(new GridLayout(4, 1, 3, 3));
        body.setOpaque(false);

        JLabel lbl = new JLabel("Base date (yyyy-MM-dd):");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        arithDateField = new JTextField(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        arithDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel spinnerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        spinnerRow.setOpaque(false);
        arithDaysSpinner = new JSpinner(new SpinnerNumberModel(30, -36500, 36500, 1));
        arithUnitCombo = new JComboBox<>(new String[]{"Days", "Months", "Years"});
        spinnerRow.add(arithDaysSpinner);
        spinnerRow.add(arithUnitCombo);

        body.add(lbl);
        body.add(arithDateField);
        body.add(new JLabel(" "));   // spacer row
        body.add(spinnerRow);

        arithResultLabel = new JLabel("Result: —");
        arithResultLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        ModernButton calcBtn = new ModernButton("Calculate");
        calcBtn.addActionListener(e -> {
            String date = arithDateField.getText().trim();
            long amount = ((Number) arithDaysSpinner.getValue()).longValue();
            String unit = (String) arithUnitCombo.getSelectedItem();
            String result;
            if ("Months".equals(unit)) {
                result = DateUtility.addMonths(date, amount);
            } else if ("Years".equals(unit)) {
                result = DateUtility.addYears(date, amount);
            } else {
                result = DateUtility.addDays(date, amount);
            }
            arithResultLabel.setText("Result: " + result);
        });

        JPanel south = new JPanel(new BorderLayout(5, 3));
        south.setOpaque(false);
        south.add(arithResultLabel, BorderLayout.CENTER);
        south.add(calcBtn, BorderLayout.SOUTH);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        return card;
    }

    
    private void refreshCurrentDate() {
        String pattern = currentDateFormatField.getText().trim();
        if (pattern.isEmpty()) pattern = "EEEE, dd MMMM yyyy";
        currentDateDisplay.setText(DateUtility.getCurrentDate(pattern));
    }

    /** Creates a standard bold card-title label. */
    private JLabel makeCardTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    // ThemeListener

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary   = ThemeManager.getTextPrimary();
        Color textSecondary = ThemeManager.getTextSecondary();
        Color accent        = ThemeManager.getAccent();
        Color bg            = ThemeManager.getSidebarBackground();
        Color border        = ThemeManager.getBorder();

        headerTitle.setForeground(textPrimary);
        headerSubtitle.setForeground(textSecondary);

        // Live date label
        if (currentDateDisplay != null) {
            currentDateDisplay.setForeground(accent);
        }

        // Day-of-week label
        if (dowResultLabel != null) {
            dowResultLabel.setForeground(accent);
        }

        // Style all visible JTextFields and JLabels recursively
        styleChildren(this, textPrimary, textSecondary, bg, border);
    }


    private void styleChildren(Container container,
                               Color textPrimary, Color textSecondary,
                               Color bg, Color border) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField tf) {
                tf.setBackground(bg);
                tf.setForeground(textPrimary);
                tf.setCaretColor(textPrimary);
                tf.setBorder(BorderFactory.createLineBorder(border));
            } else if (c instanceof JLabel lbl) {
                // Accent-coloured labels keep their colour; others get textPrimary
                Color fg = lbl.getForeground();
                if (!fg.equals(ThemeManager.getAccent())
                        && !fg.equals(ThemeManager.getSuccess())
                        && !fg.equals(ThemeManager.getDanger())
                        && !fg.equals(ThemeManager.getWarning())) {
                    lbl.setForeground(textPrimary);
                }
            } else if (c instanceof Container sub && !(c instanceof ModernButton)) {
                styleChildren(sub, textPrimary, textSecondary, bg, border);
            }
        }
    }
}
