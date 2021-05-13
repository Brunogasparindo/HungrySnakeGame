package academy.learnprogramming;

import java.awt.*;

public class Apple extends GameObject {
    public boolean isAlive = true;

    public Apple(int x, int y) {
        super(x, y);
    }

    public void drawApple(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(x * GamePanel.UNIT_SIZE, y * GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE);
    }
}
