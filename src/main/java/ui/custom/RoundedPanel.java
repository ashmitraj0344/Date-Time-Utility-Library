package ui.custom;

import ui.ThemeManager;
import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel implements ThemeManager.ThemeListener {
    private int cornerRadius = 15;
    private Color customBgColor = null;
    private boolean drawBorder = true;
    private Color customBorderColor = null;

    public RoundedPanel() {
        this(15);
    }

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
        ThemeManager.addThemeListener(this);
        updateColors();
    }

    public void setCustomBgColor(Color color) {
        this.customBgColor = color;
        repaint();
    }

    public void setDrawBorder(boolean draw) {
        this.drawBorder = draw;
        repaint();
    }

    public void setCustomBorderColor(Color color) {
        this.customBorderColor = color;
        repaint();
    }

    private void updateColors() {
        if (customBgColor == null) {
            setBackground(ThemeManager.getCardBackground());
        } else {
            setBackground(customBgColor);
        }
        repaint();
    }

    @Override
    public void onThemeChanged(boolean isDark) {
        updateColors();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        graphics.setColor(customBgColor != null ? customBgColor : ThemeManager.getCardBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // Draw border
        if (drawBorder) {
            graphics.setColor(customBorderColor != null ? customBorderColor : ThemeManager.getBorder());
            graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }
}

