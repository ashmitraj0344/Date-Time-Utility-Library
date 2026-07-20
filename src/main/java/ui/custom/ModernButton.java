package ui.custom;

import ui.ThemeManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton implements ThemeManager.ThemeListener {
    private int cornerRadius = 8;
    private Color defaultBg = null;
    private Color hoverBg = null;
    private Color textCol = null;
    private boolean isHovered = false;
    private boolean isActive = false;

    public ModernButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 13));

        ThemeManager.addThemeListener(this);
        updateColors();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isActive = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isActive = false;
                repaint();
            }
        });
    }

    public void setCustomColors(Color bg, Color hoverBg, Color textCol) {
        this.defaultBg = bg;
        this.hoverBg = hoverBg;
        this.textCol = textCol;
        repaint();
    }

    private void updateColors() {
        if (textCol == null) {
            setForeground(ThemeManager.getTextPrimary());
        } else {
            setForeground(textCol);
        }
        repaint();
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        updateColors();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg;
        if (defaultBg != null) {
            bg = isHovered ? (hoverBg != null ? hoverBg : defaultBg.brighter()) : defaultBg;
            if (isActive) bg = bg.darker();
        } else {
            bg = isHovered ? ThemeManager.getAccentHover() : ThemeManager.getAccent();
            if (isActive) bg = bg.darker();
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Text color
        if (textCol != null) {
            g2.setColor(textCol);
        } else {
            g2.setColor(defaultBg != null ? Color.WHITE : ThemeManager.getTextPrimary());
        }

        // Center the text
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
    }
}

