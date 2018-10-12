package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel implements ActionListener {
    final int WIDTH = 100;
    final int HEIGHT = 400;
    final String BACKGROUND_COLOR = "#BBADA0";
    final int UPDATE_DELAY = 5;
    final String FONT_NAME = "Lucida Console";
    static final String WORD_COLOR = "#3c3a32";
    static final int WORD_SIZE = 16;

    public Menu() {
        setPreferredSize(new Dimension(100, 400));
        setBackground(Color.decode(BACKGROUND_COLOR));

        Timer timer = new Timer(UPDATE_DELAY, this);
        timer.start();
    }

    void drawText(Graphics g, String text, int y) {
        int widthText;
        widthText = g.getFontMetrics().stringWidth(text);
        g.drawString(text, WIDTH / 2 - widthText / 2, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.decode(WORD_COLOR));
        g.setFont(new Font(FONT_NAME, Font.BOLD, WORD_SIZE));

        drawText(g, "Highest", 100);
        drawText(g, "Value:", 125);
        drawText(g, Integer.toString(Game.highestValue), 150);

        drawText(g, "Moves:", 200);
        drawText(g, Integer.toString(Game.numMove), 225);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
