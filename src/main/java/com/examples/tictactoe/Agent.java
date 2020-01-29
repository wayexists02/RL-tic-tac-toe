package com.examples.tictactoe;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Agent {

    private final List<String> history;

    private final Environment env;
    private final String name;
    
    private double epsilon;

    public Agent(final String name) {
        this.history = new LinkedList<>();

        this.env = Environment.getInstance();
        this.name = name;

        this.epsilon = 0.1;
    }

    public String getName() {
        return name;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setTrainable(boolean trainable) {
        if (trainable)
            epsilon = 0.1;
        else
            epsilon = 0.0;
    }

    public void reset() {
        history.clear();
    }

    public int getGreedyAction() {
        State current_state = env.getCurrentState();
        List<Integer> possible_actions = current_state.getActions();

        double maximum_value = -1000000.0;
        int maximum_action = -1;

        for (int i = 0; i < possible_actions.size(); i += 1) {
            int possible_action = possible_actions.get(i);
            String possible_state_string = getPossibleStateString(possible_action);
            State possible_state = env.getState(possible_state_string);
            double value_of_possible_state = possible_state.getValue();
            
            // System.out.println("Possible state: " + possible_state_string);
            // System.out.println("Possible action: " + possible_action);
            // System.out.println("Value: " + value_of_possible_state);

            if (maximum_value < value_of_possible_state) {
                maximum_value = value_of_possible_state;
                maximum_action = possible_action;
            }
        }

        // System.out.println("Maximum Value: " + maximum_value);
        // System.out.println("Maximum Action: " + maximum_action);

        return maximum_action;
    }

    public int epsilonGreedy() {
        Random rand = new Random(System.currentTimeMillis());

        List<Integer> possible_actions = env.getCurrentState().getActions();
        int greedy_action = getGreedyAction();
        int greedy_action_index = possible_actions.indexOf(greedy_action);

        // System.out.println("Greedy action: " + greedy_action);
        // System.out.println("Greedy action index: " + greedy_action_index);
        
        int action;

        if (rand.nextDouble() < this.epsilon && possible_actions.size() > 1) {
            // exploration
            int random_index = rand.nextInt(possible_actions.size() - 1);

            if (random_index >= greedy_action_index)
                random_index += 1;

            action = possible_actions.get(random_index);
        }
        else {
            // exploitation
            action = greedy_action;
        }

        StringBuilder nextStateStrBuilder = new StringBuilder(env.getCurrentStateString());
        nextStateStrBuilder.replace(action, action+1, String.valueOf(env.turn()));
        this.history.add(0, nextStateStrBuilder.toString());

        // take action
        return action;
    }
    
    public List<String> getHistory() {
        return history;
    }

    private String getPossibleStateString(int action) {
        String current_state_string = env.getCurrentStateString();
        int turn = env.turn();

        StringBuilder strBuilder = new StringBuilder(current_state_string);
        strBuilder.replace(action, action + 1, String.valueOf(turn));

        return strBuilder.toString();
    }
}
