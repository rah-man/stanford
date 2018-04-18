package cs.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Constant<E> {
    protected String name;
    protected String mod;
    protected E value;

    public Constant(String name, String mod, E value) {
        this.name = name.toLowerCase();
        this.mod = mod;
        this.value = value;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(declareConstant());
        builder.append("\n");
        builder.append(assertConstant());
        return builder.toString();
    }

    public String declareConstant() {
        return "(declare-const " + name + " " + getType() + ")";
    }

    public String assertConstant() {
        return "(assert " + assignConstant() + ")";
    }

    public String assignConstant() {
        return "(" + mod + " " + name + " " + value + ")";
    }

    private String getType() {
        String type = "Int";
        if (value instanceof Boolean) {
            type = "Bool";
        } else if (value instanceof Integer) {
            type = "Int";
        }

        return type;
    }
}
