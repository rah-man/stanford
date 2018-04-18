package cs.model;

import cs.dep.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstantFactory {
    public static Constant generateConstant(Condition condition) {
        String value = condition.value;
        if (value.equals("true") || value.equals("false")) {
            return new Constant(condition.condition, condition.mod, Boolean.parseBoolean(value));
        } else if (Character.isDigit(value.charAt(0))) {
            return new Constant(condition.condition, condition.mod, Integer.parseInt(value.split(" ")[0]));
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
