package cs.dep;

import java.util.Objects;

public class Action {
    // public because getters are too much
    public String action;
    public String agent;
    public String value;

    public Action() {
    }

    /**
     * For GUI Builder
     *
     * @param actionObject - an array of String and String
     */
    public Action(Object[] actionObject) {
        this((String) actionObject[0], (String) actionObject[1], (String) actionObject[2]);
    }

    public Action(String action, String agent, String value) {
        this.action = action.toLowerCase();
        this.agent = agent.toLowerCase();
        this.value = value;
    }

    @Override
    public String toString() {
        return action + " " + agent + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action1 = (Action) o;
        return Objects.equals(action, action1.action) &&
                Objects.equals(agent, action1.agent) &&
                Objects.equals(value, action1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, agent, value);
    }
}