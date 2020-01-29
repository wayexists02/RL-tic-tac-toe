package com.examples.tictactoe;

import java.io.Serializable;
import java.util.List;


public class State implements Serializable {

    private List<Integer> actions;
    private double value;
    private int n;

    public State(List<Integer> actions) {
        this.actions = actions;
        this.value = 0.0;
        this.n = 0;
    }

    public List<Integer> getActions() {
        return actions;
    }

    public double getValue() {
        return value;
    }

    public int getCount() {
        return n;
    }

    public void loadState(int count, double value) {
        this.n = count;
        this.value = value;
    }

    public void updateValue(double sample) {
        this.n += 1;
        double step_size = 1.0 / n;
        this.value = this.value + step_size * (sample - this.value);
    }
}