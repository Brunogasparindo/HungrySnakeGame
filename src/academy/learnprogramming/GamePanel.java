package academy.learnprogramming;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int X_UNITS = SCREEN_WIDTH / UNIT_SIZE;
    static final int Y_UNITS = SCREEN_HEIGHT / UNIT_SIZE;

    private Snake snake;
    public Apple apple;

    private boolean running;
    private static final int GOAL = 100;
    private int score;
    private int turnDelay;
    private static final int DELAY_REDUCTION_PACE = 10;

    Timer timer;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
//        System.out.println("starting game...");
        turnDelay = 300;
        setTurnTimer(turnDelay);
        snake = new Snake(SCREEN_WIDTH/UNIT_SIZE/2 - 1, SCREEN_HEIGHT/UNIT_SIZE/2 - 1); // place the snake in the middle of the screen
        createNewApple();
        score = 0;

        running = true;
    }

    // necessary to initialize the Graphics g
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawScene(g);
    }

    private void drawScene(Graphics g) {
        // not necessary. just used as help tool to see the game matrix

/*
        for (int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
*/

        snake.drawSnake(g);
        apple.drawApple(g);

        // show SCORE on the screen
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score))/2, g.getFont().getSize());

        // show message to the user case the game is over
        if (!running) {
            if (score >= GOAL) {
                g.setColor(Color.BLUE);
                g.setFont( new Font("Ink Free",Font.BOLD, 75));
                FontMetrics metrics2 = getFontMetrics(g.getFont());
                g.drawString("YOU WIN", (SCREEN_WIDTH - metrics2.stringWidth("You Win"))/2, SCREEN_HEIGHT/2);
            } else {
                g.setColor(Color.BLUE);
                g.setFont( new Font("Ink Free",Font.BOLD, 75));
                FontMetrics metrics2 = getFontMetrics(g.getFont());
                g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
            }

        }
    }

    private void createNewApple() {
        Apple newApple;
        do {
            int x = new Random().nextInt(X_UNITS);
            int y = new Random().nextInt(Y_UNITS);
            newApple = new Apple(x, y);
        } while (snake.checkCollision(newApple));
        apple = newApple;
    }

    // keep the snake in movement...
    private void setTurnTimer(int delay) {
        timer = new Timer(delay, this);
        timer.start();
    }

    // This method has to be there, otherwise the app doesn't run....
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            snake.move(apple);
        }

        if (!snake.isAlive) {
            gameOver();
        }
        if (score >= GOAL) {
            win();
        }

        if (!apple.isAlive) {
            createNewApple();
            score += 5;
            turnDelay -= DELAY_REDUCTION_PACE;
            timer.setDelay(turnDelay);
        }

        repaint(); // don't know where it comes from, but works to draw the graphic again...
    }

    public void win() {
        running = false;
        timer.stop();
    }

    public void gameOver() {
        running = false;
        timer.stop();
    }


    // inner class to handle KEY PRESS
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !running) {
                startGame();
            }

            if ((snake.getDirection() == Direction.LEFT || snake.getDirection() == Direction.RIGHT)
                && snake.getSnakeParts().get(0).x == snake.getSnakeParts().get(1).x) {
                return;
            }
            if ((snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN)
                && snake.getSnakeParts().get(0).y == snake.getSnakeParts().get(1).y) {
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snake.getDirection() != Direction.RIGHT) {
                        snake.setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.getDirection() != Direction.LEFT) {
                        snake.setDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.getDirection() != Direction.DOWN) {
                        snake.setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.getDirection() != Direction.UP) {
                        snake.setDirection(Direction.DOWN);
                    }
                    break;
            }
        }
    }
}
