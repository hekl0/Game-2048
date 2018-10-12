package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class Main extends JFrame implements KeyListener, WindowListener {
    final int DESIRE_WIDTH = 520;
    final int DESIRE_HEIGHT = 440;
    final String BACKGROUND_COLOR = "#BBADA0";
    final String ICON_SOURCE = "src/resources/icon.png";

    Game game;
    Menu menu;

    public Main() {
        new Date();
        getContentPane().setBackground(Color.decode(BACKGROUND_COLOR));
        setLayout(new FlowLayout());

        game = new Game(this);
        menu = new Menu();
        add(menu);
        add(game);

        pack(); //size to fit its components
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //function exit x btn
        setLocationRelativeTo(null); //place the window in center
        setVisible(true);
        setResizable(false);
        setTitle("  Project1 - 2048 Game");
        setIconImage(new ImageIcon(ICON_SOURCE).getImage());

        this.addKeyListener(this);
        addWindowListener(this);
    }

    public boolean createDialog(String message) {
        message = "Highest Value: " + Game.highestValue + "\nNumber of Move: " + Game.numMove + "\n" + message;
        int n = JOptionPane.showOptionDialog(this,
                message,
                "Dialog",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(ICON_SOURCE),
                new Object[]{"Yes", "No"},
                null);

        return (n == 0);
    }

    public static void main(String[] args) {
	     new Main();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Game.isAnimationRunning)
            return;

        switch (e.getKeyCode()) {
            case (KeyEvent.VK_UP):
            case (KeyEvent.VK_W):
                game.moveTable(Game.Direction.UP);
                break;
            case (KeyEvent.VK_DOWN):
            case (KeyEvent.VK_S):
                game.moveTable(Game.Direction.DOWN);
                break;
            case (KeyEvent.VK_LEFT):
            case (KeyEvent.VK_A):
                game.moveTable(Game.Direction.LEFT);
                break;
            case (KeyEvent.VK_RIGHT):
            case (KeyEvent.VK_D):
                game.moveTable(Game.Direction.RIGHT);
                break;
            case (KeyEvent.VK_R):
                if (createDialog("Are you sure to restart?"))
                    game.newGame();
                break;
            case (KeyEvent.VK_Q):
                if (createDialog("Are you sure to quit?"))
                    System.exit(0);
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (createDialog("Are you sure to quit?"))
            System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
