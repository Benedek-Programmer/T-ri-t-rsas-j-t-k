import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class DicePanel extends JPanel {
    private int value = 6;
    private Runnable onRollRequest;

    public DicePanel(Runnable onRollRequest) {
        this.onRollRequest = onRollRequest;
        setPreferredSize(new Dimension(110, 110));
        setMaximumSize(new Dimension(110, 110));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onRollRequest != null) {
                    onRollRequest.run();
                }
            }
        });
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Árnyék
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fill(new RoundRectangle2D.Float(12, 12, 90, 90, 24, 24));

        // Kocka teste
        g2.setColor(GameData.TEXT_LIGHT);
        g2.fill(new RoundRectangle2D.Float(8, 8, 90, 90, 24, 24));

        // Keret
        g2.setColor(GameData.ACCENT_GOLD);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Float(8, 8, 90, 90, 24, 24));

        // Pöttyök rajzolása
        g2.setColor(GameData.BG_DARK);
        int r = 14;
        int c1 = 26, c2 = 46, c3 = 66;

        if (value == 1 || value == 3 || value == 5) g2.fillOval(c2, c2, r, r);
        if (value >= 2) {
            g2.fillOval(c1, c1, r, r);
            g2.fillOval(c3, c3, r, r);
        }
        if (value >= 4) {
            g2.fillOval(c3, c1, r, r);
            g2.fillOval(c1, c3, r, r);
        }
        if (value == 6) {
            g2.fillOval(c1, c2, r, r);
            g2.fillOval(c3, c2, r, r);
        }
    }
}
