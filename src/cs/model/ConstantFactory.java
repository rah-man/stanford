package cs.model;

import cs.dep.Action;
import cs.dep.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstantFactory {
    protected static final int LOW_SYSTOLE = 0;
    protected static final int HIG_SYSTOLE = 1;
    protected static final int DIASTOLE_MOD = 0;
    protected static final int DIASTOLE_VALUE = 1;

    public static Constant generateConstant(Condition condition) {
        String value = condition.value;
        if (value.equals("true") || value.equals("false")) {
            return new Constant(condition.condition, condition.mod, Boolean.parseBoolean(value));
        } else if (Character.isDigit(value.charAt(0))) {
            return new Constant(condition.condition, condition.mod, Integer.parseInt(value.split(" ")[0]));
        }
        return null;
    }

    public static Constant[] generateConstant(Action action) {
        String agent = action.agent;
        String value = action.value;
        if (value.equals("") || value.length() == 0) {
            return new Constant[]{new Constant(action.agent, "=", true)};
        } else if (agent.equals("blood pressure")) {
            String[] valueSplit = value.split("/");
            String[] systole = valueSplit[0].split("-");
            String[] diastole = valueSplit[1].trim().split(" ");

            int low = Integer.parseInt(systole[LOW_SYSTOLE].trim());
            int high = Integer.parseInt(systole[HIG_SYSTOLE].trim());

            return new Constant[]{
                    new Constant("blood-pressure-systole-low", ">=", low),
                    new Constant("blood-pressure-systole-high", "<=", high),
                    new Constant("blood-pressure-diastole", diastole[DIASTOLE_MOD], Integer.parseInt(diastole[DIASTOLE_VALUE]))
            };
        }
        return null;
    }

    private static String combineConstantAssertion(String conj, Constant... cons) {
        List<Constant> constants = Arrays.asList(cons);
        String assertions = constants.stream().map(c -> c.assignConstant()).collect(Collectors.joining(" "));
        return "(" + conj + " " + assertions + ")";
    }

    public static String assertCombineConstant(String conj, Constant... cons) {
        return "(assert " + combineConstantAssertion(conj, cons) + ")";
    }

    public static void main(String[] args) {
        String booleanString = "3 mg/mmol";
        System.out.println(Integer.parseInt(booleanString.split(" ")[0]));
    }
}
