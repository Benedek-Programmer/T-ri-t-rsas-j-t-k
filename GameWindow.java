import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameWindow extends JFrame {
    private final int[] playerPositions = {0, 0, 0, 0};
    private int currentPlayer = 0;
    private boolean isAnimating = false;

    private final List<Tile> tiles;
    private BoardPanel boardPanel;
    private DicePanel dicePanel;
    private JButton rollButton;
    private JLabel turnLabel;
    private JTextArea logArea;
    private final Random random = new Random();

    public GameWindow() {
        this.tiles = GameData.createTiles();

        setTitle("I. Világháború - Történelmi Társasjáték (11. osztályos tananyag)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GameData.BG_DARK);
        setLayout(new BorderLayout(15, 15));

        initUI();
    }

    private void initUI() {
        boardPanel = new BoardPanel(tiles, playerPositions);
        boardPanel.setBorder(new EmptyBorder(15, 15, 15, 5));
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(350, 800));
        controlPanel.setBackground(GameData.PANEL_DARK);
        controlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("A NAGY HÁBORÚ (1914–1918)");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(GameData.ACCENT_GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(titleLabel);

        JLabel subTitle = new JLabel("Interaktív Történelmi Társasjáték");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setForeground(GameData.TEXT_MUTED);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(subTitle);
        controlPanel.add(Box.createVerticalStrut(20));

        turnLabel = new JLabel("Következik: " + GameData.PLAYER_NAMES[0]);
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        turnLabel.setForeground(GameData.PLAYER_COLORS[0]);
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(turnLabel);
        controlPanel.add(Box.createVerticalStrut(15));

        JPanel playersCard = new JPanel(new GridLayout(4, 1, 5, 8));
        playersCard.setBackground(GameData.BG_DARK);
        playersCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        playersCard.setMaximumSize(new Dimension(310, 140));

        for (int i = 0; i < 4; i++) {
            JPanel pRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            pRow.setOpaque(false);
            final int playerIdx = i;
            JPanel colorDot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(GameData.PLAYER_COLORS[playerIdx]);
                    g2.fillOval(0, 2, 14, 14);
                }
            };
            colorDot.setPreferredSize(new Dimension(16, 18));
            colorDot.setOpaque(false);

            JLabel pName = new JLabel(GameData.PLAYER_NAMES[i]);
            pName.setForeground(GameData.TEXT_LIGHT);
            pName.setFont(new Font("SansSerif", Font.PLAIN, 13));

            pRow.add(colorDot);
            pRow.add(pName);
            playersCard.add(pRow);
        }
        controlPanel.add(playersCard);
        controlPanel.add(Box.createVerticalStrut(25));

        dicePanel = new DicePanel(this::startDiceRoll);
        dicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(dicePanel);
        controlPanel.add(Box.createVerticalStrut(15));

        rollButton = new JButton("DOBÁS A KOCKÁVAL (1–6)");
        rollButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        rollButton.setBackground(GameData.ACCENT_GOLD);
        rollButton.setForeground(GameData.BG_DARK);
        rollButton.setFocusPainted(false);
        rollButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rollButton.setMaximumSize(new Dimension(260, 45));
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollButton.addActionListener(e -> startDiceRoll());
        controlPanel.add(rollButton);
        controlPanel.add(Box.createVerticalStrut(20));

        JLabel logHeader = new JLabel("Hadijelentés / Eseménynapló:");
        logHeader.setFont(new Font("SansSerif", Font.BOLD, 12));
        logHeader.setForeground(GameData.TEXT_MUTED);
        logHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(logHeader);
        controlPanel.add(Box.createVerticalStrut(5));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(GameData.BG_DARK);
        logArea.setForeground(GameData.TEXT_LIGHT);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        logArea.setText("Üdvözöljük a fronton! Kattints a dobókockára vagy a gombra a kezdéshez.\n");

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        controlPanel.add(scrollPane);

        add(controlPanel, BorderLayout.EAST);
    }

    private void startDiceRoll() {
        if (isAnimating) return;
        isAnimating = true;
        rollButton.setEnabled(false);

        final int[] ticks = {0};
        Timer rollTimer = new Timer(60, null);
        rollTimer.addActionListener(e -> {
            int tempRoll = random.nextInt(6) + 1;
            dicePanel.setValue(tempRoll);
            ticks[0]++;

            if (ticks[0] >= 12) {
                rollTimer.stop();
                int finalRoll = random.nextInt(6) + 1;
                dicePanel.setValue(finalRoll);
                log("\n[" + GameData.PLAYER_NAMES[currentPlayer] + "] dobott: " + finalRoll);
                movePlayerDirect(finalRoll);
            }
        });
        rollTimer.start();
    }

    private void movePlayerDirect(int steps) {
        final int targetPos = Math.min(tiles.size() - 1, Math.max(0, playerPositions[currentPlayer] + steps));

        Timer moveTimer = new Timer(300, null);
        moveTimer.addActionListener(e -> {
            if (playerPositions[currentPlayer] < targetPos) {
                playerPositions[currentPlayer]++;
                boardPanel.repaint();
            } else if (playerPositions[currentPlayer] > targetPos) {
                playerPositions[currentPlayer]--;
                boardPanel.repaint();
            } else {
                moveTimer.stop();
                boardPanel.repaint();
                // Kis késleltetés, hogy a játékos lássa a bábu megérkezését, mielőtt felugrik az ablak
                SwingUtilities.invokeLater(this::handleTileLanding);
            }
        });
        moveTimer.start();
    }

    private void handleTileLanding() {
        try {
            int pos = playerPositions[currentPlayer];
            Tile currentTile = tiles.get(pos);
            log("-> Mező (#" + (pos + 1) + "): " + currentTile.getTitle() + " – " + currentTile.getDescription());

            if (currentTile.getType() == TileType.FINISH) {
                showCustomDialog("GYŐZELEM!",
                        GameData.PLAYER_NAMES[currentPlayer] + " elsőként ért el az 1918-as fegyverszünetig és megnyerte a játékot!",
                        GameData.ACCENT_GOLD);
                resetGame();
                return;
            }

            if (currentTile.getType() == TileType.EVENT_FORWARD) {
                showCustomDialog("TÖRTÉNELMI ESEMÉNY – HADISIKER",
                        currentTile.getTitle() + "\n" + currentTile.getDescription() + "\n\nHadisiker! Bónusz: +1 mező előre.",
                        new Color(16, 185, 129));
                applyBonusDirect(1);
                return;
            } else if (currentTile.getType() == TileType.EVENT_BACK) {
                showCustomDialog("TÖRTÉNELMI ESEMÉNY – VISSZAVONULÁS",
                        currentTile.getTitle() + "\n" + currentTile.getDescription() + "\n\nVisszavonulás! Büntetés: -1 mező hátra.",
                        new Color(239, 68, 68));
                applyBonusDirect(-1);
                return;
            } else if (currentTile.getType() == TileType.QUIZ && currentTile.getQuestion() != null) {
                Question q = currentTile.getQuestion();
                boolean isCorrect = showQuizDialog(currentTile.getTitle(), currentTile.getDescription(), q);

                if (isCorrect) {
                    log("   [HELYES VÁLASZ!] Bónusz: +1 lépés előre!");
                    showCustomDialog("KIVÁLÓ VÁLASZ!", "Helyes válasz! +1 bónusz lépést kapsz előre.", new Color(16, 185, 129));
                    applyBonusDirect(1);
                    return;
                } else {
                    log("   [HIBÁS VÁLASZ!] Büntetés: -1 lépés hátra.");
                    showCustomDialog("HIBÁS VÁLASZ!", "Sajnos nem talált!\n\nBüntetés: Lépj vissza 1 mezőt!", new Color(239, 68, 68));
                    applyBonusDirect(-1);
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log("Hiba történt az esemény feldolgozásakor: " + ex.getMessage());
        }

        nextTurn();
    }

    private void applyBonusDirect(int delta) {
        playerPositions[currentPlayer] = Math.min(tiles.size() - 1, Math.max(0, playerPositions[currentPlayer] + delta));
        boardPanel.repaint();
        // Az új mezőn már NEM indítunk újabb eseményt vagy kérdést, így garantáltan nincs végtelen hurok!
        nextTurn();
    }

    private void nextTurn() {
        currentPlayer = (currentPlayer + 1) % 4;
        turnLabel.setText("Következik: " + GameData.PLAYER_NAMES[currentPlayer]);
        turnLabel.setForeground(GameData.PLAYER_COLORS[currentPlayer]);
        isAnimating = false;
        rollButton.setEnabled(true);
    }

    private void resetGame() {
        for (int i = 0; i < 4; i++) playerPositions[i] = 0;
        currentPlayer = 0;
        isAnimating = false;
        rollButton.setEnabled(true);
        turnLabel.setText("Következik: " + GameData.PLAYER_NAMES[0]);
        turnLabel.setForeground(GameData.PLAYER_COLORS[0]);
        logArea.setText("Új játék indult!\n");
        boardPanel.repaint();
    }

    private void log(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // --- EGYSÉGES SÖTÉT DIZÁJNÚ PÁRBESZÉD ABLAKOK ---

    private void showCustomDialog(String title, String message, Color headerColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setSize(450, 260);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(GameData.PANEL_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(headerColor, 2),
                new EmptyBorder(22, 24, 22, 24)
        ));

        JLabel titleLbl = new JLabel(title, JLabel.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLbl.setForeground(headerColor);
        panel.add(titleLbl, BorderLayout.NORTH);

        JTextArea msgArea = new JTextArea(message);
        msgArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msgArea.setForeground(GameData.TEXT_LIGHT);
        msgArea.setBackground(GameData.PANEL_DARK);
        msgArea.setEditable(false);
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        panel.add(msgArea, BorderLayout.CENTER);

        JButton okButton = new JButton("RENDBEN");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        okButton.setBackground(headerColor);
        okButton.setForeground(GameData.BG_DARK);
        okButton.setFocusPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(140, 36));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(okButton);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 4 különálló alakzatba írt válaszlehetőség, megkeverve, előzetes kijelölés nélkül!
    private boolean showQuizDialog(String title, String description, Question q) {
        String[] rawOptions = q.getOptions();
        String correctText = rawOptions[q.getCorrectIndex()];

        // Válaszok összekeverése, hogy a helyes válasz pozíciója ne legyen kiszámítható
        List<String> shuffledOptions = new ArrayList<>();
        for (String opt : rawOptions) {
            shuffledOptions.add(opt);
        }
        Collections.shuffle(shuffledOptions, random);

        final int[] selectedIndex = {-1};
        final boolean[] answeredCorrectly = {false};

        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setSize(560, 470);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(GameData.PANEL_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GameData.ACCENT_GOLD, 2),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JLabel titleLbl = new JLabel("TÖRTÉNELMI KVÍZ • " + title.toUpperCase(), JLabel.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLbl.setForeground(GameData.ACCENT_GOLD);
        panel.add(titleLbl, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);

        JTextArea descArea = new JTextArea(description + "\n\n" + q.getPrompt());
        descArea.setFont(new Font("SansSerif", Font.BOLD, 14));
        descArea.setForeground(GameData.TEXT_LIGHT);
        descArea.setBackground(GameData.PANEL_DARK);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setMaximumSize(new Dimension(510, 90));
        centerContainer.add(descArea);
        centerContainer.add(Box.createVerticalStrut(14));

        // 4 lekerekített alakzat kártya (A, B, C, D) - EGYIK SINCS KIJELÖLVE INDULÁSKOR!
        String[] letters = {"A", "B", "C", "D"};
        AnswerShapeCard[] cards = new AnswerShapeCard[shuffledOptions.size()];

        JButton submitButton = new JButton("VÁLASSZ EGY VÁLASZT!");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitButton.setBackground(new Color(71, 85, 105));
        submitButton.setForeground(GameData.TEXT_LIGHT);
        submitButton.setFocusPainted(false);
        submitButton.setEnabled(false);
        submitButton.setPreferredSize(new Dimension(220, 38));

        for (int i = 0; i < shuffledOptions.size(); i++) {
            final int idx = i;
            cards[i] = new AnswerShapeCard(letters[i], shuffledOptions.get(i), () -> {
                selectedIndex[0] = idx;
                for (int j = 0; j < cards.length; j++) {
                    cards[j].setSelected(j == idx);
                }
                submitButton.setEnabled(true);
                submitButton.setBackground(GameData.ACCENT_GOLD);
                submitButton.setForeground(GameData.BG_DARK);
                submitButton.setText("VÁLASZ BEKÜLDÉSE");
                submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            });
            centerContainer.add(cards[i]);
            centerContainer.add(Box.createVerticalStrut(8));
        }

        panel.add(centerContainer, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            if (selectedIndex[0] >= 0) {
                String chosenText = shuffledOptions.get(selectedIndex[0]);
                answeredCorrectly[0] = chosenText.equals(correctText);
                dialog.dispose();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(submitButton);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
        return answeredCorrectly[0];
    }
}
