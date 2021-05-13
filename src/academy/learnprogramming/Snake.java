package academy.learnprogramming;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private final List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x+1, y));
        snakeParts.add(new GameObject(x+2, y));
    }

    public List<GameObject> getSnakeParts() {
        return snakeParts;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void drawSnake(Graphics g) {
        boolean head = true;
        for (GameObject snakePart : snakeParts) {
            Color color = head ? Color.green : new Color(45, 180, 0);
            g.setColor(color);
            g.fillRect(snakePart.x * GamePanel.UNIT_SIZE, snakePart.y * GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE, GamePanel.UNIT_SIZE);
            head = false;
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        // Check if is still on the field
        if (newHead.x <0 || newHead.x >= GamePanel.X_UNITS || newHead.y < 0 || newHead.y >= GamePanel.Y_UNITS) {
            isAlive = false;
            return;
        }

        // Check if collision
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }

        // add the new head at the first position of the snakeParts
        snakeParts.add(0, newHead);

        // Check is apple was eaten and remove the tail if not
        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    public GameObject createNewHead() {
        GameObject oldHead = snakeParts.get(0);
        if (direction == Direction.LEFT) {
            return new GameObject(oldHead.x - 1, oldHead.y);
        }
        if (direction == Direction.RIGHT) {
            return new GameObject(oldHead.x + 1, oldHead.y);
        }
        if (direction == Direction.UP) {
            return new GameObject(oldHead.x, oldHead.y - 1);
        }
        if (direction == Direction.DOWN) {
            return new GameObject(oldHead.x, oldHead.y + 1);
        }
        // not possible to come to this part. But just in case...
        return null;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject object) {
        for (GameObject snakePart : snakeParts) {
            if (object.x == snakePart.x && object.y == snakePart.y) {
                return true;
            }
        }
        return false;
    }
}
