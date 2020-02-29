package com.examples.tictactoe;

import java.util.LinkedList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Environment {

    public static final String SAVE_PATH = "./state_value.bin";

    private Map<String, State> states;
    private String currentState;
    private int winner;
    private boolean gameOver;

    private int turn;

    public Environment() {
        int num_states = 3^9;

        states = new HashMap<>(num_states);

        permuteStates(9).stream().forEach((String key) -> {
            List<Integer> actions = new LinkedList<>();
            for (int i = 0; i < key.length(); i += 1)
                if (key.charAt(i) == '0')
                    actions.add(i);

            states.put(key, new State(actions));
        });

        System.out.println("The number of states: " + String.valueOf(states.size()));

        reset();
    }

    public boolean isGameOver() {

        for (int i = 0; i < 3; i += 1) {
            if (currentState.charAt(i) == currentState.charAt(i + 3) && currentState.charAt(i + 3) == currentState.charAt(i + 6)) {
                if (currentState.charAt(i) - 48 != 0) {
                    winner = currentState.charAt(i) - 48;
                    gameOver = true;
                }
            }
            else if (currentState.charAt(i*3) == currentState.charAt(i*3 + 1) && currentState.charAt(i*3 + 1) == currentState.charAt(i*3 + 2)) {
                if (currentState.charAt(i*3) - 48 != 0) {
                    winner = currentState.charAt(i*3) - 48; 
                    gameOver = true;
                }
            }
        }

        if (currentState.charAt(0) == currentState.charAt(4) && currentState.charAt(4) == currentState.charAt(8)) {
            if (currentState.charAt(0) - 48 != 0) {
                winner = currentState.charAt(0) - 48;
                gameOver = true;
            }
        }

        if (currentState.charAt(2) == currentState.charAt(4) && currentState.charAt(4) == currentState.charAt(6)) {
            if (currentState.charAt(2) - 48 != 0) {
                winner = currentState.charAt(2) - 48;
                gameOver = true;
            }
        }

        if (!gameOver && !currentState.contains("0")) {
            winner = -1;
            gameOver = true;
        }

        return gameOver;
    }

    public State getCurrentState() {
        return states.get(currentState);
    }

    public String getCurrentStateString() {
        return currentState;
    }

    public void reset() {
        currentState = "000000000";
        winner = -1;
        gameOver = false;

        turn = 1;
    }

    public int getTurn() {
        return turn;
    }

    public int getWinner() {
        return winner;
    }

    public State getState(String stateString) {
        return states.get(stateString);
    }

    public void updateState(int action) {
        StringBuilder strBuilder = new StringBuilder(currentState);
        strBuilder.replace(action, action + 1, String.valueOf(turn));
        currentState = strBuilder.toString();
    }

    public boolean isValidAction(int action) {
        return currentState.charAt(action) == '0';
    }

    public void nextTurn() {
        if (turn == 1)
            turn = 2;
        else if (turn == 2)
            turn = 1;
    }

    public void save() {
        SerializableEnvironment serializableEnv = new SerializableEnvironment();
        
        for (String key : states.keySet()) {
            State state = states.get(key);

            List<Integer> actions = state.getActions();
            List<Double> actionValues = state.getActionValues();
            List<Double> policy = state.getPolicy();

            serializableEnv.actions.put(key, actions);
            serializableEnv.actionValues.put(key, actionValues);
            serializableEnv.policy.put(key, policy);
        }

        File file = new File(SAVE_PATH);

        try (ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(file))) {
            if (!Files.exists(file.toPath()))
                Files.createFile(file.toPath());
                
            objout.writeObject(serializableEnv);
        } catch (IOException exc) {

        }
    }

    public void load() {
        File file = new File(SAVE_PATH);

        if (!Files.exists(file.toPath()))
            return;

        try (ObjectInputStream objin = new ObjectInputStream(new FileInputStream(file))) {
            SerializableEnvironment serializableEnv = (SerializableEnvironment) objin.readObject();
            
            for (String stateString: serializableEnv.actions.keySet()) {
                State state = getState(stateString);

                List<Integer> actions = serializableEnv.actions.get(stateString);
                List<Double> actionValues = serializableEnv.actionValues.get(stateString);
                List<Double> policy = serializableEnv.policy.get(stateString);

                state.loadState(actions, actionValues, policy);
            }

        } catch (IOException | ClassNotFoundException exc) {

        }
    }

    private List<String> permuteStates(int n) {
        List<String> keys = new LinkedList<>();
        List<String> prefix_keys;
         
        if (n == 1)
            prefix_keys = new LinkedList<>();
        else
            prefix_keys = permuteStates(n - 1);

        if (prefix_keys.size() > 0) {
            prefix_keys.stream().forEach((String elem) -> {
                String elem1 = elem + "0";
                String elem2 = elem + "1";
                String elem3 = elem + "2";

                keys.add(elem1);
                keys.add(elem2);
                keys.add(elem3);
            });
        }
        else {
            keys.add("0");
            keys.add("1");
            keys.add("2");
        }

        return keys;
    }
}