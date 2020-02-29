package com.examples.tictactoe;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * Tic tac toe reinforcement learning example
 *
 */
public class App 
{
    private static final double STEP_SIZE = 0.3;
    private static final double DISCOUNT_RATE = 0.3;

    private Environment env;
    private Agent[] agents;
    private Board board;

    private boolean trainingMode;
    private int trainingCounter;

    public App() {
        env = new Environment();
        agents = new Agent[2];

        agents[0] = new Agent("Agent 1", env, 0.5, STEP_SIZE, DISCOUNT_RATE);
        agents[1] = new Agent("Agent 2", env, 0.0, STEP_SIZE, DISCOUNT_RATE);
        board = new Board();

        trainingMode = false;
        trainingCounter = 0;
    }

    public void initialize() {
        board.update(env.getCurrentStateString(), env.getTurn());
    }

    public void launch() {
        System.out.println("Start!");
        env.load();

        while (true) {
            if (!trainingMode && board.isTrainingMode()) {
                trainingMode = true;
                trainingCounter = 100000;
                agents[1].setEpsilon(0.5);
            }
            else {
                agents[1].setEpsilon(0.0);
            }

            initialize();

            firstStep();
            mainStep();
            finalStep();

            env.reset();

            if (trainingMode && trainingCounter > 0) {
                trainingCounter -= 1;
                
                if (trainingCounter == 0)
                    trainingMode = false;

                continue;
            }

            if (board.confirmExit()) break;
        }
        
        env.save();
        board.close();
        System.out.println("end");
    }

    private void firstStep() {
        int action = -1;

        if (trainingMode) {
            action = agents[0].firstStep();
            nextTurn(action);
        }
        else {
            System.out.println("\nTurn 1");
            action = userInput();
            System.out.println("Action: " + String.valueOf(action));
            nextTurn(action);
        }
            
        if (!trainingMode) System.out.println("\nTurn 2");
        action = agents[1].firstStep();
        if (!trainingMode) System.out.println("Action: " + String.valueOf(action));
        nextTurn(action);
    }

    private void mainStep() {
        while (!env.isGameOver()) {
            int turn = env.getTurn();
            int action = -1;

            if (turn == 1 && !trainingMode) {
                System.out.println("Turn 1");

                action = userInput();
            }
            else if (turn == 1) {
                action = agents[0].step(0.0);
            }
            else {
                if (!trainingMode) System.out.println("Turn 2");
                action = agents[1].step(0.0);
            }
            
            if (!trainingMode) System.out.println("Action: " + String.valueOf(action));
            nextTurn(action);
        }
    }

    private void finalStep() {
        int winner = env.getWinner();
        int reward1 = 0;
        int reward2 = 0;

        if (winner == 1) {
            reward1 = 1;
            reward2 = -1;
        }
        else if (winner == 2) {
            reward1 = -1;
            reward2 = 1;
        }

        if (trainingMode) agents[0].finalStep(reward1);
        agents[1].finalStep(reward2);
    }

    private void nextTurn(int action) {
        env.updateState(action);
        env.nextTurn();

        // update board
        board.resetAction();
        board.update(env.getCurrentStateString(), env.getTurn());
    }

    private int userInput() {
        int action = -1;

        while (true) {
            action = board.getAction();
            if (action != -1 && env.isValidAction(action))
                return action;

            Thread.yield();
        }
    }

    public static void main( String[] args )
    {
        App app = new App();
        app.launch();
    }
}
