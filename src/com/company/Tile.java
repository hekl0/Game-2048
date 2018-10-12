package com.company;

import java.awt.*;

public class Tile {
    static final String[] COLORCODE = new String[]{"#CDC1B4", "#EEE4DA", "#EDE0C8", "#F2B179", "#F59563", "#F67C5F",
            "#F65E3B", "#EDCF72", "#EDCC61", "#EDC850", "#EDC53f", "#EDC22E"};
    static final String WORD_COLOR = "#3c3a32";
    static final int WORD_SIZE = 25;
    static final int TILE_SIZE = 90;
    static final int TILE_GAP = 8;

    public int value;
    public int x, y; // the row and column on board 4 x 4
    public int x2D, y2D; // the position on graphic
    public int targetX, targetY; // this tile is moving to row and column on board 4x4 (for animation)

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.x2D = get2DValue(x);
        this.y2D = get2DValue(y);
    }

    public static int get2DValue(int x) {
        return (x + 1) * TILE_GAP + x * TILE_SIZE;
    }

    public static void drawTile(Graphics g, int value, int x2D, int y2D) {
        //draw background color of the tile
        g.setColor(Color.decode(getColorCodeFromValue(value)));
        g.fillRoundRect(x2D, y2D, TILE_SIZE, TILE_SIZE, 15, 15);

        //draw value of the tile
        if (value == 0) return;
        g.setColor(Color.decode(WORD_COLOR));
        g.setFont(new Font(null, Font.BOLD, WORD_SIZE));
        int widthValue = g.getFontMetrics().stringWidth(Integer.toString(value));
        g.drawString(Integer.toString(value), x2D + TILE_SIZE/2 - widthValue/2, y2D + TILE_SIZE/2 + 10);
    }

    public static String getColorCodeFromValue(int value) {
        if (value == 0)
            return COLORCODE[0];

        int count = 1;
        while (value != 2) {
            value /= 2;
            count++;
        }

        return COLORCODE[count];
    }
}
