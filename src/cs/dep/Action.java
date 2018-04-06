package cs.dep;

import java.util.Objects;

public class Action {
    // public because getters are too much
    public String action;
    public String value;

    public Action() {
    }

    /**
     * For GUI Builder
     *
     * @param actionObject - an array of String and String
     */
    public Action(Object[] actionObject) {
        this((String) actionObject[0], (String) actionObject[1]);
    }

    public Action(String action, String value) {
        this.action = action.toLowerCase();
        this.value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return action + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action1 = (Action) o;
        return Objects.equals(action, action1.action) &&
                Objects.equals(value, action1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, value);
    }
}