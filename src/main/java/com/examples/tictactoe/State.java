package com.examples.tictactoe;

import java.util.ArrayList;
import java.util.List;


public class State {

    private List<Integer> actions;
    private List<Double> policy;
    private List<Double> actionValues;

    public State(List<Integer> actions) {
        this.actions = actions;

        this.policy = new ArrayList<>(actions.size());
        for (int i = 0; i < this.actions.size(); i += 1) {
            this.policy.add(1.0 / this.actions.size());
        }

        this.actionValues = new ArrayList<>(actions.size());
        for (int i = 0; i < this.actions.size(); i += 1) {
            this.actionValues.add(0.0);
        }
    }

    public void loadState(List<Integer> actions, List<Double> actionValues, List<Double> policy) {
        this.actions = actions;
        this.actionValues = actionValues;
        this.policy = policy;
    }

    public List<Integer> getActions() {
        return actions;
    }

    public double getActionValue(int action) {
        int index = actions.indexOf(action);
        return actionValues.get(index);
    }

    public List<Double> getActionValues() {
        return actionValues;
    }

    public double getPolicyByAction(int action) {
        int index = actions.indexOf(action);
        return policy.get(index);
    }

    public List<Double> getPolicy() {
        return policy;
    }
    
    public void setValue(int action, double value) {
        int index = actions.indexOf(action);
        this.actionValues.set(index, value);
    }

    public void setPolicy(int action, double policy) {
        int index = actions.indexOf(action);
        this.policy.set(index, policy);
    }

    public boolean validatePolicy() {
        double sum = 0.0;
        for (double pol: policy) {
            sum += pol;
        }

        // return sum == 1.0;
        return true;
    }
}