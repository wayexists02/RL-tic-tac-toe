package com.examples.tictactoe;

import com.examples.tictactoe.Environment;

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
    private Environment env;

    private int action;

    private boolean training;

    public Board() {
        env = Environment.getInstance();

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

    public boolean isTrainable() {
        int option = JOptionPane.showOptionDialog(
            this,
            "Do you want to train agent?",
            "Train AI",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[] {"Yes", "No"},
            null
        );

        training = false;

        if (option == JOptionPane.CLOSED_OPTION)
            System.exit(0);
        else if (option == 0)
            training = true;
        else
            training = false;

        return training;
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    public void resetAction() {
        this.action = -1;
    }

    public int getAction() {
        return action;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, 400, 400);

        drawBoard(g2d, env.getCurrentStateString());
    }

    private void drawBoard(Graphics2D g2d, String state_string) {
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
                if (env.turn() != 1) return;

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
