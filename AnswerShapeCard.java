import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class AnswerShapeCard extends JPanel {
    private final String letterBadge;
    private final String answerText;
    private boolean selected = false;
    private boolean hovered = false;
    private Runnable onSelect;

    public AnswerShapeCard(String letterBadge, String answerText, Runnable onSelect) {
        this.letterBadge = letterBadge;
        this.answerText = answerText;
        this.onSelect = onSelect;
        setOpaque(false);
        setPreferredSize(new Dimension(480, 52));
        setMaximumSize(new Dimension(520, 52));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onSelect != null) {
                    onSelect.run();
                }
            }
        });
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Alakzat háttérszíne (semleges, NEM árulja el a jó választ!)
        Color bg = selected ? new Color(51, 65, 92) : (hovered ? GameData.CARD_HOVER : GameData.CARD_DARK);
        Color border = selected ? GameData.ACCENT_BLUE : (hovered ? new Color(100, 116, 139) : new Color(65, 78, 100));

        // Lekerekített téglalap alakzat
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 16, 16));

        g2.setColor(border);
        g2.setStroke(new BasicStroke(selected ? 2.5f : 1.5f));
        g2.draw(new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 16, 16));

        // Bal oldali betűjelvény alakzat (A, B, C, D)
        Color badgeBg = selected ? GameData.ACCENT_BLUE : new Color(30, 36, 48);
        Color badgeFg = selected ? GameData.BG_DARK : GameData.TEXT_LIGHT;

        g2.setColor(badgeBg);
        g2.fill(new RoundRectangle2D.Float(12, 10, 32, h - 20, 10, 10));

        g2.setColor(badgeFg);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fmBadge = g2.getFontMetrics();
        int bx = 12 + (32 - fmBadge.stringWidth(letterBadge)) / 2;
        int by = 10 + ((h - 20 - fmBadge.getHeight()) / 2) + fmBadge.getAscent();
        g2.drawString(letterBadge, bx, by);

        // Válasz szövege az alakzaton belül
        g2.setColor(GameData.TEXT_LIGHT);
        g2.setFont(new Font("SansSerif", selected ? Font.BOLD : Font.PLAIN, 13));
        FontMetrics fmText = g2.getFontMetrics();
        int ty = ((h - fmText.getHeight()) / 2) + fmText.getAscent();
        g2.drawString(answerText, 56, ty);
    }
}
