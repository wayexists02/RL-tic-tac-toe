package com.examples.tictactoe;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;


public class SerializableEnvironment implements Externalizable{

    private static final long serialVersionUID = 5366782098938387626L;

    public Map<String, Double> state_value;
    public Map<String, Integer> counts;

    public SerializableEnvironment() {
        state_value = new HashMap<>();
        counts = new HashMap<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        int size = state_value.size();
        out.writeInt(size);

        for (String key : state_value.keySet()) {
            double value = state_value.get(key);
            int count = counts.get(key);

            out.writeUTF(key);
            out.writeDouble(value);
            out.writeInt(count);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();

        for (int i = 0; i < size; i += 1) {
            String key = in.readUTF();
            double value = in.readDouble();
            int count = in.readInt();

            state_value.put(key, value);
            counts.put(key, count);
        }
    }
}