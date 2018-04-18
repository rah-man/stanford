package cs.model;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Constant<E> {
    protected String name;
    protected String mod;
    protected E value;

    public Constant(String name, String mod, E value) {
        this.name = cleanName(name);
        this.mod = mod;
        this.value = value;
    }

    private String cleanName(String name) {
        name = name.toLowerCase();
        String[] splitName = name.split(" ");
        StringJoiner joiner = new StringJoiner("-");
        for (String word : splitName) {
            joiner.add(word);
        }
        return Arrays.stream(splitName).map(s -> s).collect(Collectors.joining("-"));
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
