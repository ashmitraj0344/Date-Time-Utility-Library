package ui.panels;

import scheduler.EventScheduler;
import model.Event;
import ui.ThemeManager;
import ui.custom.RoundedPanel;
import ui.custom.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class CalendarPanel extends JPanel implements ThemeManager.ThemeListener {
    private final EventScheduler scheduler;

    private JLabel monthYearLabel;
    private JPanel calendarGrid;
    private JPanel eventsListPanel;
    private JLabel selectedDateLabel;

    private LocalDate displayDate;
    private LocalDate selectedDate;

    private Color normalBtnColor;
    private Color eventDayColor = new Color(251, 191, 36); // Amber indicator
    private Color todayBorderColor = new Color(16, 185, 129); // Green border for Today

    public CalendarPanel(EventScheduler scheduler) {
        this.scheduler = scheduler;
        this.displayDate = LocalDate.now();
        this.selectedDate = LocalDate.now();

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        populateCalendarGrid();
        loadSelectedDayEvents();

        ThemeManager.addThemeListener(this);
        onThemeChanged(ThemeManager.isDarkMode());
    }

    private void initComponents() {
        // --- Left Side: Calendar Grid (weight 0.6) ---
        JPanel calendarPanel = new JPanel(new BorderLayout(10, 10));
        calendarPanel.setOpaque(false);

        // Header controls (Prev, Month/Year, Today, Next)
        JPanel controls = new JPanel(new BorderLayout(10, 10));
        controls.setOpaque(false);

        monthYearLabel = new JLabel("June 2025");
        monthYearLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel navBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        navBtns.setOpaque(false);

        ModernButton prevBtn = new ModernButton("<");
        prevBtn.setPreferredSize(new Dimension(40, 30));
        prevBtn.addActionListener(e -> {
            displayDate = displayDate.minusMonths(1);
            populateCalendarGrid();
        });

        ModernButton todayBtn = new ModernButton("Today");
        todayBtn.setPreferredSize(new Dimension(70, 30));
        todayBtn.addActionListener(e -> {
            displayDate = LocalDate.now();
            selectedDate = LocalDate.now();
            populateCalendarGrid();
            loadSelectedDayEvents();
        });

        ModernButton nextBtn = new ModernButton(">");
        nextBtn.setPreferredSize(new Dimension(40, 30));
        nextBtn.addActionListener(e -> {
            displayDate = displayDate.plusMonths(1);
            populateCalendarGrid();
        });

        navBtns.add(prevBtn);
        navBtns.add(todayBtn);
        navBtns.add(nextBtn);

        controls.add(monthYearLabel, BorderLayout.WEST);
        controls.add(navBtns, BorderLayout.EAST);
        calendarPanel.add(controls, BorderLayout.NORTH);

        // Day of week headers (Sun - Sat)
        JPanel gridContainer = new RoundedPanel(15);
        gridContainer.setLayout(new BorderLayout(5, 5));
        gridContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel weekHeader = new JPanel(new GridLayout(1, 7, 5, 5));
        weekHeader.setOpaque(false);
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            weekHeader.add(lbl);
        }
        gridContainer.add(weekHeader, BorderLayout.NORTH);

        // Actual day grid (6 rows, 7 columns)
        calendarGrid = new JPanel(new GridLayout(6, 7, 5, 5));
        calendarGrid.setOpaque(false);
        gridContainer.add(calendarGrid, BorderLayout.CENTER);

        calendarPanel.add(gridContainer, BorderLayout.CENTER);

        // --- Right Side: Event list for selected date (weight 0.4) ---
        RoundedPanel eventsPanel = new RoundedPanel(15);
        eventsPanel.setLayout(new BorderLayout(10, 10));
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        eventsPanel.setPreferredSize(new Dimension(280, 0));

        selectedDateLabel = new JLabel("Events on 07 June 2025");
        selectedDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        eventsListPanel = new JPanel();
        eventsListPanel.setLayout(new BoxLayout(eventsListPanel, BoxLayout.Y_AXIS));
        eventsListPanel.setOpaque(false);

        JScrollPane scrollEvents = new JScrollPane(eventsListPanel);
        scrollEvents.setOpaque(false);
        scrollEvents.getViewport().setOpaque(false);
        scrollEvents.setBorder(null);

        eventsPanel.add(selectedDateLabel, BorderLayout.NORTH);
        eventsPanel.add(scrollEvents, BorderLayout.CENTER);

        // Main Layout
        add(calendarPanel, BorderLayout.CENTER);
        add(eventsPanel, BorderLayout.EAST);
    }

    private void populateCalendarGrid() {
        calendarGrid.removeAll();

        YearMonth ym = YearMonth.from(displayDate);
        monthYearLabel.setText(displayDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + displayDate.getYear());

        LocalDate firstOfMonth = ym.atDay(1);
        int dayOfWeekVal = firstOfMonth.getDayOfWeek().getValue(); // 1 = Mon, 7 = Sun
        int leadEmptySlots = (dayOfWeekVal == 7) ? 0 : dayOfWeekVal; // adjust for Sunday start (0 = Sun, 1 = Mon)

        int daysInMonth = ym.lengthOfMonth();

        // Previous month filler slots
        for (int i = 0; i < leadEmptySlots; i++) {
            JLabel empty = new JLabel("");
            calendarGrid.add(empty);
        }

        // Current month slots
        LocalDate today = LocalDate.now();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate cellDate = ym.atDay(day);
            String cellDateStr = cellDate.toString();

            JButton dayBtn = new JButton(String.valueOf(day)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    boolean isSelected = cellDate.equals(selectedDate);
                    boolean isToday = cellDate.equals(today);
                    boolean hasEvents = !scheduler.getEventsForDate(cellDateStr).isEmpty();

                    if (isSelected) {
                        g2.setColor(ThemeManager.getAccent());
                        g2.fillOval((getWidth() - 32) / 2, (getHeight() - 32) / 2, 32, 32);
                        g2.setColor(Color.WHITE);
                    } else {
                        g2.setColor(ThemeManager.getCardBackground());
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setColor(ThemeManager.getTextPrimary());
                    }

                    if (isToday) {
                        g2.setColor(todayBorderColor);
                        g2.setStroke(new BasicStroke(2.0f));
                        g2.drawOval((getWidth() - 32) / 2, (getHeight() - 32) / 2, 32, 32);
                    }

                    if (hasEvents && !isSelected) {
                        // draw indicator dot at bottom center
                        g2.setColor(eventDayColor);
                        g2.fillOval(getWidth() / 2 - 3, getHeight() - 10, 6, 6);
                    }

                    // Draw text
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                    int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(getText(), tx, ty);
                    g2.dispose();
                }
            };

            dayBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dayBtn.setFocusPainted(false);
            dayBtn.setContentAreaFilled(false);
            dayBtn.setBorderPainted(false);
            dayBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final int dNum = day;
            dayBtn.addActionListener(e -> {
                selectedDate = ym.atDay(dNum);
                calendarGrid.repaint();
                loadSelectedDayEvents();
            });

            calendarGrid.add(dayBtn);
        }

        // Remaining empty slots to complete 42 cell grid
        int totalCells = leadEmptySlots + daysInMonth;
        for (int i = totalCells; i < 42; i++) {
            JLabel empty = new JLabel("");
            calendarGrid.add(empty);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    public void loadSelectedDayEvents() {
        eventsListPanel.removeAll();

        selectedDateLabel.setText("Events on " + selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        List<Event> dayEvents = scheduler.getEventsForDate(selectedDate.toString());
        if (dayEvents.isEmpty()) {
            JLabel noEvents = new JLabel("No events scheduled.", SwingConstants.CENTER);
            noEvents.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noEvents.setAlignmentX(Component.CENTER_ALIGNMENT);
            noEvents.setForeground(ThemeManager.getTextSecondary());
            eventsListPanel.add(Box.createVerticalStrut(20));
            eventsListPanel.add(noEvents);
        } else {
            for (Event ev : dayEvents) {
                RoundedPanel item = new RoundedPanel(10);
                item.setCustomBgColor(ThemeManager.getSidebarBackground());
                item.setLayout(new BorderLayout(5, 5));
                item.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                item.setMaximumSize(new Dimension(280, 65));

                // Left color band representing priority
                Color pColor = ThemeManager.getAccent();
                if ("High".equalsIgnoreCase(ev.getPriority())) pColor = ThemeManager.getDanger();
                else if ("Medium".equalsIgnoreCase(ev.getPriority())) pColor = ThemeManager.getWarning();
                else if ("Low".equalsIgnoreCase(ev.getPriority())) pColor = ThemeManager.getSuccess();

                JPanel leftBar = new JPanel();
                leftBar.setPreferredSize(new Dimension(4, 0));
                leftBar.setBackground(pColor);
                item.add(leftBar, BorderLayout.WEST);

                JPanel textPanel = new JPanel(new GridLayout(2, 1, 2, 2));
                textPanel.setOpaque(false);

                JLabel title = new JLabel(ev.getTitle());
                title.setFont(new Font("Segoe UI", Font.BOLD, 12));
                title.setForeground(ThemeManager.getTextPrimary());

                JLabel timeLoc = new JLabel(ev.getTime() + " | " + ev.getLocation());
                timeLoc.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                timeLoc.setForeground(ThemeManager.getTextSecondary());

                textPanel.add(title);
                textPanel.add(timeLoc);
                item.add(textPanel, BorderLayout.CENTER);

                eventsListPanel.add(item);
                eventsListPanel.add(Box.createVerticalStrut(8));
            }
        }

        eventsListPanel.revalidate();
        eventsListPanel.repaint();
    }

    public void refresh() {
        populateCalendarGrid();
        loadSelectedDayEvents();
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        Color textPrimary = ThemeManager.getTextPrimary();
        monthYearLabel.setForeground(textPrimary);
        selectedDateLabel.setForeground(textPrimary);

        // Weekly Header names coloring
        Component viewport = calendarGrid.getParent();
        if (viewport instanceof JPanel) {
            JPanel gridContainer = (JPanel) viewport;
            for (Component c : gridContainer.getComponents()) {
                if (c instanceof JPanel) {
                    JPanel p = (JPanel) c;
                    for (Component lbl : p.getComponents()) {
                        if (lbl instanceof JLabel) {
                            lbl.setForeground(ThemeManager.getTextSecondary());
                        }
                    }
                }
            }
        }

        refresh();
    }
}
