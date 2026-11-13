import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {
    private final List<Tile> tiles;
    private final int[] playerPositions;

    public BoardPanel(List<Tile> tiles, int[] playerPositions) {
        this.tiles = tiles;
        this.playerPositions = playerPositions;
        setBackground(GameData.BG_DARK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cols = 7;
        int rows = 7;
        int pad = 10;
        int w = getWidth() - pad * 2;
        int h = getHeight() - pad * 2;
        int cellW = w / cols;
        int cellH = h / rows;

        // Középső díszítő kártya
        int centerX = pad + cellW + 12;
        int centerY = pad + cellH + 12;
        int centerW = cellW * (cols - 2) - 24;
        int centerH = cellH * (rows - 2) - 24;

        g2.setColor(GameData.PANEL_DARK);
        g2.fill(new RoundRectangle2D.Float(centerX, centerY, centerW, centerH, 25, 25));
        g2.setColor(new Color(51, 65, 85));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Float(centerX, centerY, centerW, centerH, 25, 25));

        // Középső feliratok
        g2.setColor(GameData.ACCENT_GOLD);
        g2.setFont(new Font("Serif", Font.BOLD, 28));
        drawCenteredString(g2, "AZ ELSŐ VILÁGHÁBORÚ", centerX, centerY + 55, centerW);

        g2.setColor(GameData.TEXT_MUTED);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
        drawCenteredString(g2, "1914 – 1918 • 11. évfolyamos történelem", centerX, centerY + 85, centerW);

        // Jelmagyarázat középen
        int legendY = centerY + 135;
        drawLegendItem(g2, centerX + 40, legendY, new Color(30, 58, 138), "Kék mező: Történelmi kvízkérdés (+1 / -1 lépés)");
        drawLegendItem(g2, centerX + 40, legendY + 35, new Color(6, 95, 70), "Zöld mező: Hadisiker / Áttörés (+1 lépés)");
        drawLegendItem(g2, centerX + 40, legendY + 70, new Color(153, 27, 27), "Piros mező: Visszavonulás / Veszteség (-1 lépés)");
        drawLegendItem(g2, centerX + 40, legendY + 105, GameData.ACCENT_GOLD, "Arany mező: Start (1914) és Fegyverszünet (1918)");

        Point[] gridCoords = getPerimeterCoords(cols, rows);

        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            Point pt = gridCoords[i];
            int x = pad + pt.x * cellW + 4;
            int y = pad + pt.y * cellH + 4;
            int cw = cellW - 8;
            int ch = cellH - 8;

            Color tileBg = switch (tile.getType()) {
                case START, FINISH -> new Color(120, 85, 15);
                case QUIZ -> new Color(30, 48, 85);
                case EVENT_FORWARD -> new Color(16, 75, 58);
                case EVENT_BACK -> new Color(115, 29, 29);
            };

            g2.setColor(tileBg);
            g2.fill(new RoundRectangle2D.Float(x, y, cw, ch, 14, 14));

            g2.setColor(tile.getType() == TileType.START || tile.getType() == TileType.FINISH ? GameData.ACCENT_GOLD : new Color(71, 85, 105));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x, y, cw, ch, 14, 14));

            g2.setColor(GameData.TEXT_MUTED);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString("#" + (i + 1), x + 8, y + 16);

            g2.setColor(GameData.TEXT_LIGHT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            drawWrappedTitle(g2, tile.getTitle(), x + 8, y + 32, cw - 16);

            drawPlayersOnTile(g2, i, x, y, cw, ch);
        }
    }

    private void drawPlayersOnTile(Graphics2D g2, int tileIndex, int x, int y, int cw, int ch) {
        int[][] offsets = {
            {12, ch - 28},
            {cw - 32, ch - 28},
            {12, ch - 52},
            {cw - 32, ch - 52}
        };

        for (int p = 0; p < 4; p++) {
            if (playerPositions[p] == tileIndex) {
                int px = x + offsets[p][0];
                int py = y + offsets[p][1];

                g2.setColor(new Color(0, 0, 0, 140));
                g2.fillOval(px + 2, py + 2, 20, 20);

                g2.setColor(GameData.PLAYER_COLORS[p]);
                g2.fillOval(px, py, 20, 20);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(px, py, 20, 20);

                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                g2.drawString(String.valueOf(p + 1), px + 7, py + 14);
            }
        }
    }

    private Point[] getPerimeterCoords(int cols, int rows) {
        List<Point> coords = new ArrayList<>();
        for (int c = 0; c < cols; c++) coords.add(new Point(c, 0));
        for (int r = 1; r < rows; r++) coords.add(new Point(cols - 1, r));
        for (int c = cols - 2; c >= 0; c--) coords.add(new Point(c, rows - 1));
        for (int r = rows - 2; r >= 1; r--) coords.add(new Point(0, r));
        return coords.toArray(new Point[0]);
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int width) {
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (width - fm.stringWidth(text)) / 2;
        g2.drawString(text, tx, y);
    }

    private void drawWrappedTitle(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        if (fm.stringWidth(text) <= maxWidth) {
            g2.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int curY = y;
            for (String word : words) {
                if (fm.stringWidth(line + word) < maxWidth) {
                    line.append(word).append(" ");
                } else {
                    g2.drawString(line.toString(), x, curY);
                    curY += 14;
                    line = new StringBuilder(word + " ");
                }
            }
            g2.drawString(line.toString(), x, curY);
        }
    }

    private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
        g2.setColor(color);
        g2.fillRoundRect(x, y - 12, 18, 18, 6, 6);
        g2.setColor(GameData.TEXT_LIGHT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString(label, x + 28, y + 2);
    }
}
