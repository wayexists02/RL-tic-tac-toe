package com.examples.tictactoe;

import javax.swing.JOptionPane;

/**
 * Tic tac toe reinforcement learning example
 *
 */
public class App 
{
    private static final double DISCOUNT_RATE = 0.5;

    public static void main( String[] args )
    {
        // Create environment
        Environment env = Environment.getInstance();
        Board board = new Board();
        board.show();

        // create agent
        Agent agent = new Agent("Agent");

        System.out.println("Start");

        env.load();

        // main loop
        while (true) {

            // game loop
            while (!env.isGameOver()) {
                // System.out.println("Current state: " + env.getCurrentStateString());

                // turn & action information
                int turn = env.turn();
                int action = -1;
                
                if (turn == 1) {
                    // User or agent 1 turn
                    System.out.println("Turn 1");

                    while (true) {
                        action = board.getAction();
                        if (action != -1 && env.isValidAction(action))
                            break;

                        Thread.yield();
                    }
                }
                else if (turn == 2) {
                    // Agent 2 turn
                    System.out.println("Turn 2");

                    action = agent.epsilonGreedy();
                }
                else {
                    // invalid turn
                    System.err.println("Invalid turn number: " + String.valueOf(turn));
                }

                // System.out.println("Action: " + String.valueOf(action));

                // update board
                env.updateState(action);

                board.repaint();
                board.resetAction();
                env.nextTurn();
            }

            double reward = 0;
            int winner = env.getWinner();

            if (winner == 1) {
                reward = -1.0;
            }
            else if (winner == 2) {
                reward = 1.0;
            }

            // update value function of agent 1
            double reward_sample = reward;
            for (String stateString : agent.getHistory()) {
                State state = env.getState(stateString);
                state.updateValue(reward_sample);

                reward_sample = 0.0 + DISCOUNT_RATE * reward_sample;
            }

            System.out.println("Winner: " + winner);

            if (winner == -1)
                JOptionPane.showConfirmDialog(board, "Draw");
            else
                JOptionPane.showConfirmDialog(board, "Player " + winner + " win!");

            // whether the player want to continue playing game or not
            int option = JOptionPane.showOptionDialog(
                board, 
                "Do you want to continue playing game?", 
                "End of Game", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                new String[] {"Yes", "No"}, 
                null
            );

            // if no answer or 'no', then exit
            if (option == JOptionPane.CLOSED_OPTION || option == 1)
                break;

            // before playing game, reset game environment
            agent.reset();
            env.save();
            env.reset();

            board.repaint();
        }

        System.out.println("End");
        System.exit(0);
    }
}
