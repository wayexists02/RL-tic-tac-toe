package com.examples.tictactoe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Board extends JPanel {

    private JFrame mainFrame;

    private int action;
    private int turn;

    private boolean training;
    private String currentStateString;

    public Board() {
        this.setSize(400, 400);
        
        mainFrame = new JFrame();
        mainFrame.setTitle("Tic Tac Toe");

        mainFrame.setSize(400, 400);
        mainFrame.setResizable(false);
        mainFrame.add(this);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        applyMouseAdapter();

        action = -1;
    }

    public boolean isTrainingMode() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Do you want to train agent?",
            "Train AI",
            JOptionPane.YES_NO_OPTION
        );

        return option == JOptionPane.YES_OPTION;
    }

    public void showDialog(String title, String msg) {
        JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.OK_CANCEL_OPTION);
    }

    public boolean confirmExit() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    public void resetAction() {
        this.action = -1;
    }

    public int getAction() {
        return action;
    }

    public void close() {
        mainFrame.dispose();
    }

    public void update(String currentStateString, int turn) {
        this.currentStateString = currentStateString;
        this.turn = turn;

        if (mainFrame.isVisible())
            repaint();
        else
            mainFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, 400, 400);

        drawGraphics(g2d, this.currentStateString);
    }

    private void drawGraphics(Graphics2D g2d, String state_string) {
        for (int i = 0; i < state_string.length(); i += 1) {
            char state_char = state_string.charAt(i);
            int center_x = 100 + 100*(i % 3);
            int center_y = 100 + 100*(i / 3);

            g2d.setPaint(Color.BLACK);
            g2d.drawRect(center_x - 45, center_y - 45, 90, 90);

            switch (state_char) {
                case '1':
                    g2d.setPaint(Color.BLUE);
                    g2d.fillOval(center_x - 40, center_y - 40, 80, 80);
                    break;
                case '2':
                    g2d.setPaint(Color.RED);
                    g2d.fillOval(center_x - 40, center_y - 40, 80, 80);
                    break;
                default:
                    break;
            }
        }
    }

    private void applyMouseAdapter() {
        if (training) return;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (Board.this.turn != 1) return;

                int x = ev.getX();
                int y = ev.getY();

                int x_index;
                int y_index;

                if (x > 55 && x < 145)
                    x_index = 0;
                else if (x > 155 && x < 245)
                    x_index = 1;
                else if (x > 255 && x < 345)
                    x_index = 2;
                else
                    x_index = -1;

                if  (y > 55 && y < 145)
                    y_index = 0;
                else if (y > 155 && y < 245)
                    y_index = 1;
                else if (y > 255 && y < 345)
                    y_index = 2;
                else
                    y_index = -1;

                if (x_index == -1 || y_index == -1)
                    return;

                Board.this.action = x_index + y_index * 3;
            }
        });
    }
}
