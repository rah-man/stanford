package cs.dep;

import java.util.Objects;

public class Condition<E> {
    // public because getters are too much
    public String condition;
    public String mod;
    public String value;
    public boolean or;

    public Condition() {
    }

    /**
     * For GUI Builder
     *
     * @param conditionObject - an array of String, String and Boolean
     */
    public Condition(Object[] conditionObject) {
        this((String) conditionObject[0], (String) conditionObject[1], (String) conditionObject[2], (boolean) conditionObject[3]);
    }

    public Condition(String condition, String mod, String value, boolean or) {
        this.condition = condition;
        this.mod = mod;
        this.value = value;
        this.or = or;
    }

    @Override
    public String toString() {
        return condition + " " + mod + " " + value + ", conj=" + or;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition1 = (Condition) o;
        return Objects.equals(condition, condition1.condition) &&
                Objects.equals(mod, condition1.mod) &&
                Objects.equals(value, condition1.value) &&
                Objects.equals(or, condition1.or);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, mod, value, or);
    }

    public boolean getOR() {
        return or;
    }
}
