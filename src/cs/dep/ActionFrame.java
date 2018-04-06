package cs.dep;

import java.util.ArrayList;
import java.util.Objects;

public class ActionFrame {
    private ArrayList<ArrayList<TreeNode>> actionTree = new ArrayList<ArrayList<TreeNode>>();
    protected ArrayList<Action> actionList;
    private final int ACTION = 0;

    public ActionFrame() {
        actionList = new ArrayList<Action>();
    }

    public void addTree(ArrayList<TreeNode> tree) {
        actionTree.add(tree);
    }

    public ArrayList<Action> normaliseAction() {
        for (ArrayList<TreeNode> treeList : actionTree) {
            String action = treeList.get(ACTION).value;
            String value = "";
            for (int i = 1; i < treeList.size(); i++) {
                value += treeList.get(i).value + " ";
            }

            Action act = new Action(action, value.trim());
            actionList.add(act);
        }

        return actionList;
    }

    public String toString() {
        String output = "";
        for (Action a : actionList) {
            output += a.toString() + "\n";
        }
        return output;
    }
}

class Action {
    protected String action;
    protected String value;

    public Action() {
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
