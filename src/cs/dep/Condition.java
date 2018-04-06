package cs.dep;

import java.util.Objects;

public class Condition {
    // public because getters are too much
    public String condition;
    public String mod;
    public String value;

    public Condition() {
    }

    /**
     * For GUI Builder
     *
     * @param conditionObject - an array of String, String and Boolean
     */
    public Condition(Object[] conditionObject) {
        this((String) conditionObject[0], (String) conditionObject[1], (String) conditionObject[2]);
    }

    public Condition(String condition, String mod, String value) {
        this.condition = condition;
        this.mod = mod;
        this.value = value;
    }

    @Override
    public String toString() {
        return condition + " " + mod + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition1 = (Condition) o;
        return Objects.equals(condition, condition1.condition) &&
                Objects.equals(mod, condition1.mod) &&
                Objects.equals(value, condition1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, mod, value);
    }
}
