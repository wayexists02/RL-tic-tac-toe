package com.examples.tictactoe;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SerializableEnvironment implements Externalizable{

    private static final long serialVersionUID = 5366782098938387626L;

    public Map<String, List<Integer>> actions;
    public Map<String, List<Double>> actionValues;
    public Map<String, List<Double>> policy;

    public SerializableEnvironment() {
        actions = new HashMap<>();
        actionValues = new HashMap<>();
        policy = new HashMap<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        int size = actionValues.size();
        out.writeInt(size);

        for (String key : actionValues.keySet()) {
            out.writeUTF(key);

            int len = actions.get(key).size();
            out.writeInt(len);

            for (int action: actions.get(key)) out.writeInt(action);
            for (double actionVal: actionValues.get(key)) out.writeDouble(actionVal);
            for (double pol: policy.get(key)) out.writeDouble(pol);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();

        for (int i = 0; i < size; i += 1) {
            String key = in.readUTF();
            int len = in.readInt();

            actions.put(key, new ArrayList<>(len));
            actionValues.put(key, new ArrayList<>(len));
            policy.put(key, new ArrayList<>(len));

            for (int j = 0; j < len; j += 1) actions.get(key).add(in.readInt());
            for (int j = 0; j < len; j += 1) actionValues.get(key).add(in.readDouble());
            for (int j = 0; j < len; j += 1) policy.get(key).add(in.readDouble());
        }
    }
}