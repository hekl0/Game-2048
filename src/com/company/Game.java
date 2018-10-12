package com.company;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements ActionListener {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    final int WIDTH = 400;
    final int HEIGHT = 400;
    final String BACKGROUND_COLOR = "#BBADA0";
    final int UPDATE_DELAY = 7;
    final int MOVING_DISTANCE = 14;

    public static boolean isAnimationRunning = false;
    public static int numMove = 0;
    public static int highestValue = 0;

    Tile[][] tiles = new Tile[4][4];
    int[][] numOfTilesMovingTo = new int[4][4]; //number of tiles is moving to the row i and column j
    List<Tile> movingTiles = new ArrayList<>(); //list of tiles which are moving for animation
    int[][] valueOfTable; //get value from table tiles

    Main main;

    public Game(Main main) {
        this.main = main;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        newGame();

        repaint();

        Timer timer = new Timer(UPDATE_DELAY, this);
        timer.start();
    }

    void newGame() {
        numMove = 0;
        highestValue = 2;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                tiles[i][j] = null;
        addNewTile();
        addNewTile();
    }

    void endMovingAnimation() {
        isAnimationRunning = false;
        addNewTile();
        repaint();

        //check if game over
        if (!isAbleToMoveLeftRight(false) && !isAbleToMoveLeftRight(true)
                && !isAbleToMoveUpDown(false) && !isAbleToMoveUpDown(true)) {
            System.out.println("Game Over!");
            if (main.createDialog("Game Over! Restart?"))
                this.newGame();
            else
                System.exit(0);
        }
    }

    void addNewTile() {
        List<Pair<Integer, Integer>> emptyTileList = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (tiles[i][j] == null)
                    emptyTileList.add(new Pair<>(i, j));

        int index = (int) (Math.random() * emptyTileList.size());
        int x = emptyTileList.get(index).getKey();
        int y = emptyTileList.get(index).getValue();
        Tile newTile = new Tile(((int) (Math.random() * 10)) <= 7 ? 2 : 4, x, y);

        tiles[x][y] = newTile;
        highestValue = Math.max(highestValue, newTile.value);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode(BACKGROUND_COLOR));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                Tile.drawTile(g, 0, Tile.get2DValue(i), Tile.get2DValue(j));
                if (tiles[i][j] != null)
                    Tile.drawTile(g, tiles[i][j].value, tiles[i][j].x2D, tiles[i][j].y2D);
            }

        for (Tile tile : movingTiles)
            Tile.drawTile(g, tile.value, tile.x2D, tile.y2D);
    }

    //get value from table tiles for the array valueOfTable
    int[][] getCopyValue() {
        int[][] copyValue = new int[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (tiles[i][j] == null)
                    copyValue[i][j] = 0;
                else
                    copyValue[i][j] = tiles[i][j].value;
        return copyValue;
    }

    public void moveTable(Direction type) {
        switch (type) {
            case UP:
                if (!isAbleToMoveUpDown(true)) return;
                break;
            case DOWN:
                if (!isAbleToMoveUpDown(false)) return;
                break;
            case LEFT:
                if (!isAbleToMoveLeftRight(true)) return;
                break;
            case RIGHT:
                if (!isAbleToMoveLeftRight(false)) return;
                break;
        }

        setMovingAnimation();
        numMove++;
    }

    public boolean isAbleToMoveUpDown(boolean isUp) {
        int a = isUp ? 0 : 3;
        int b = isUp ? 4 : -1;
        int d = isUp ? 1 : -1;

        valueOfTable = getCopyValue();
        for (int i = 0; i < 4; i++) {
            int lim = a;
            for (int j = a; j != b; j += d)
                if (valueOfTable[i][j] != 0) {
                    tiles[i][j].targetX = tiles[i][j].x;
                    tiles[i][j].targetY = tiles[i][j].y;

                    if (lim == j) continue;
                    if (valueOfTable[i][lim] == 0) {
                        valueOfTable[i][lim] = valueOfTable[i][j];
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = i;
                        tiles[i][j].targetY = lim;
                        continue;
                    }
                    if (valueOfTable[i][lim] == valueOfTable[i][j]) {
                        valueOfTable[i][lim] *= 2;
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = i;
                        tiles[i][j].targetY = lim;
                        lim += d;
                        continue;
                    }
                    if (valueOfTable[i][lim] != valueOfTable[i][j]) {
                        lim += d;
                        if (lim == j) continue;
                        valueOfTable[i][lim] = valueOfTable[i][j];
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = i;
                        tiles[i][j].targetY = lim;
                    }
                }
        }

        return isDifferent(valueOfTable);
    }

    public boolean isAbleToMoveLeftRight(boolean isLeft) {
        int a = isLeft ? 0 : 3;
        int b = isLeft ? 4 : -1;
        int d = isLeft ? 1 : -1;

        valueOfTable = getCopyValue();
        for (int j = 0; j < 4; j++) {
            int lim = a;
            for (int i = a; i != b; i += d)
                if (valueOfTable[i][j] != 0) {
                    tiles[i][j].targetX = tiles[i][j].x;
                    tiles[i][j].targetY = tiles[i][j].y;

                    if (lim == i) continue;
                    if (valueOfTable[lim][j] == 0) {
                        valueOfTable[lim][j] = valueOfTable[i][j];
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = lim;
                        tiles[i][j].targetY = j;
                        continue;
                    }
                    if (valueOfTable[lim][j] == valueOfTable[i][j]) {
                        valueOfTable[lim][j] *= 2;
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = lim;
                        tiles[i][j].targetY = j;
                        lim += d;
                        continue;
                    }
                    if (valueOfTable[lim][j] != valueOfTable[i][j]) {
                        lim += d;
                        if (lim == i) continue;
                        valueOfTable[lim][j] = valueOfTable[i][j];
                        valueOfTable[i][j] = 0;
                        tiles[i][j].targetX = lim;
                        tiles[i][j].targetY = j;
                    }
                }
        }

        return isDifferent(valueOfTable);
    }

    boolean isDifferent(int[][] value) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (tiles[i][j] == null && value[i][j] != 0)
                    return true;
                if (tiles[i][j] != null)
                    if (tiles[i][j].value != value[i][j])
                        return true;
            }
        return false;
    }

    private void setMovingAnimation() {
        isAnimationRunning = true;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                numOfTilesMovingTo[i][j] = 0;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (tiles[i][j] != null) {
                    movingTiles.add(tiles[i][j]);
                    if (tiles[i][j].x != tiles[i][j].targetX || tiles[i][j].y != tiles[i][j].targetY)
                        numOfTilesMovingTo[tiles[i][j].targetX][tiles[i][j].targetY] += 1;
                    tiles[i][j] = null;
                }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (movingTiles.size() == 0 && isAnimationRunning)
            endMovingAnimation();

        List<Tile> temp = new ArrayList<>();
        for (Tile tile : movingTiles)
            if (tile.x2D == Tile.get2DValue(tile.targetX) && tile.y2D == Tile.get2DValue(tile.targetY)
                    && numOfTilesMovingTo[tile.targetX][tile.targetY] == 0) {
                highestValue = Math.max(highestValue, valueOfTable[tile.targetX][tile.targetY]);
                tiles[tile.targetX][tile.targetY] = new Tile(valueOfTable[tile.targetX][tile.targetY], tile.targetX, tile.targetY);
            } else
                temp.add(tile);
        movingTiles = temp;

        for (Tile tile : movingTiles)
            if (tile.x2D != Tile.get2DValue(tile.targetX) || tile.y2D != Tile.get2DValue(tile.targetY)) {
                if (tile.x2D < Tile.get2DValue(tile.targetX)) tile.x2D += MOVING_DISTANCE;
                if (tile.x2D > Tile.get2DValue(tile.targetX)) tile.x2D -= MOVING_DISTANCE;
                if (tile.y2D < Tile.get2DValue(tile.targetY)) tile.y2D += MOVING_DISTANCE;
                if (tile.y2D > Tile.get2DValue(tile.targetY)) tile.y2D -= MOVING_DISTANCE;

                if (tile.x2D == Tile.get2DValue(tile.targetX) &&
                        tile.y2D == Tile.get2DValue(tile.targetY))
                    numOfTilesMovingTo[tile.targetX][tile.targetY] -= 1;
            }

        repaint();
    }
}
