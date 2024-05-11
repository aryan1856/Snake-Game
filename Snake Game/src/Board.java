import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;

    private final int ALL_DOTS = 1600;
    private final int DOT = 10;
    private final int RANDOM_POSITION = 39;

    private int apple_x;
    private int apple_y;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean isInGame = true;

    private int dots=3;
    private Timer timer;
    private int score = 0;
    private JLabel scoreLabel;

    Board() {
        addKeyListener(new TAdapter());

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(400, 400));
        setFocusable(true);
        loadImages();
        initGame();
        createScoreLabel();
    }

    private void createScoreLabel() {
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);
    }
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
        head = i3.getImage();
    }

    public void initGame() {
        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT;
        }

        locateApple();

        timer = new Timer(140, this);
        timer.start();
    }

    public void locateApple() {
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT;

        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g) {
        if (isInGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0 ; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrices = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (400 - metrices.stringWidth(msg)) / 2, 400/2);
    }

    public void move() {
        for (int i = dots ; i > 0 ; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) {
            x[0] = x[0] - DOT;
        }
        if (right) {
            x[0] = x[0] + DOT;
        }
        if (up) {
            y[0] = y[0] - DOT;
        }
        if (down) {
            y[0] = y[0] + DOT;
        }
    }

    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++; // Increment score when apple is eaten
            scoreLabel.setText("Score: " + score); // Update score label
            locateApple();
        }
    }

    public void checkGameOver() {
        for(int i = dots; i > 0; i--) {
            if (( i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                isInGame = false;
            }
        }

        if (y[0] >= 400) {
            isInGame = false;
        }
        if (x[0] >= 400) {
            isInGame = false;
        }
        if (y[0] < 0) {
            isInGame = false;
        }
        if (x[0] < 0) {
            isInGame = false;
        }

        if (!isInGame) {
            timer.stop();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (isInGame) {
            checkApple();
            checkGameOver();
            move();
        }

        repaint();
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && (!right)) {
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_RIGHT && (!left)) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && (!down)) {
                up = true;
                left = false;
                right = false;
            }

            if (key == KeyEvent.VK_DOWN && (!up)) {
                down = true;
                left = false;
                right = false;
            }
        }
    }

}