package com.examples.tictactoe;

import java.util.List;
import java.util.Random;


public class Agent {

    private final Environment env;
    private final String name;
    
    private double epsilon;
    private double stepSize;
    private double gamma;

    private String prevStateString;
    private int prevAction;

    public Agent(final String name, final Environment env, double epsilon, double stepSize, double gamma) {
        this.name = name;
        this.env = env;
        this.epsilon = epsilon;
        this.stepSize = stepSize;
        this.gamma = gamma;
    }

    public void setEpsilon(double eps) {
        this.epsilon = eps;
    }

    public String getName() {
        return name;
    }

    public int firstStep() {
        prevStateString = env.getCurrentStateString();
        prevAction = getEGreedyAction();

        return prevAction;
    }

    public int step(double reward) {
        State prevState = env.getState(prevStateString);

        double currActionValue = getCurrentStateExpectedValue();
        double prevActionValue = prevState.getActionValue(prevAction);
        double newPrevActionValue = prevActionValue + stepSize*(reward + gamma*currActionValue - prevActionValue);

        prevState.setValue(prevAction, newPrevActionValue);

        updatePolicy();

        int action = getEGreedyAction();
        
        prevStateString = env.getCurrentStateString();
        prevAction = action;

        return action;
    }

    public void finalStep(double reward) {
        State prevState = env.getState(prevStateString);

        double prevActionValue = prevState.getActionValue(prevAction);
        double newPrevActionValue = prevActionValue + stepSize*(reward - prevActionValue);

        prevState.setValue(prevAction, newPrevActionValue);

        updatePolicy();
    }

    private void updatePolicy() {
        State prevState = env.getState(prevStateString);
        int greedyAction = getGreedyAction(prevState);
        
        for (int action: prevState.getActions()) {
            if (action == greedyAction)
                prevState.setPolicy(greedyAction, 1.0 - 0.1 + 0.1 / prevState.getActions().size());
            else
                prevState.setPolicy(action, 0.1 / prevState.getActions().size());
        }

        if (!prevState.validatePolicy())
            throw new RuntimeException("Incorrect policy!");
    }

    private double getCurrentStateExpectedValue() {
        State currentState = env.getCurrentState();

        double expectation = 0.0;

        for (int action: currentState.getActions()) {
            double actionVal = currentState.getActionValue(action);
            double pol = currentState.getPolicyByAction(action);

            expectation += actionVal*pol;
        }

        return expectation;
    }

    private int getGreedyAction(State state) {
        double maxActionVal = -10000000.0;
        int maxAction = -1;
        
        for (int action: state.getActions()) {
            double actionVal = state.getActionValue(action);

            if (actionVal > maxActionVal) {
                maxActionVal = actionVal;
                maxAction = action;
            }
        }

        return maxAction;
    }

    private int getEGreedyAction() {
        Random rand = new Random(System.currentTimeMillis());
        List<Integer> possibleActions = env.getCurrentState().getActions();

        int action;

        if (rand.nextDouble() < this.epsilon && possibleActions.size() > 1) {
            // exploration
            int randIndex = rand.nextInt(possibleActions.size());
            action = possibleActions.get(randIndex);
        }
        else {
            // exploitation
            action = getGreedyAction(env.getCurrentState());
        }

        return action;
    }
}
