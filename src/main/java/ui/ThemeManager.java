package ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static boolean darkMode = true;
    private static final List<ThemeListener> listeners = new ArrayList<>();

    public interface ThemeListener {
        void onThemeChanged(boolean isDark);
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
        for (ThemeListener l : listeners) {
            l.onThemeChanged(darkMode);
        }
    }

    public static void addThemeListener(ThemeListener l) {
        listeners.add(l);
    }

    // Color definitions
    public static Color getBackground() {
        return darkMode ? new Color(15, 23, 42) : new Color(248, 250, 252);
    }

    public static Color getSidebarBackground() {
        return darkMode ? new Color(30, 41, 59) : new Color(241, 245, 249);
    }

    public static Color getCardBackground() {
        return darkMode ? new Color(30, 41, 59) : new Color(255, 255, 255);
    }

    public static Color getTextPrimary() {
        return darkMode ? new Color(248, 250, 252) : new Color(15, 23, 42);
    }

    public static Color getTextSecondary() {
        return darkMode ? new Color(148, 163, 184) : new Color(100, 116, 139);
    }

    public static Color getAccent() {
        return darkMode ? new Color(139, 92, 246) : new Color(124, 58, 237); // Royal Purple
    }

    public static Color getAccentHover() {
        return darkMode ? new Color(167, 139, 250) : new Color(109, 40, 217);
    }

    public static Color getBorder() {
        return darkMode ? new Color(51, 65, 85) : new Color(226, 232, 240);
    }

    public static Color getSuccess() {
        return darkMode ? new Color(16, 185, 129) : new Color(5, 150, 105);
    }

    public static Color getWarning() {
        return darkMode ? new Color(245, 158, 11) : new Color(217, 119, 6);
    }

    public static Color getDanger() {
        return darkMode ? new Color(239, 68, 68) : new Color(220, 38, 38);
    }
}
