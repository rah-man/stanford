package cs.dep;

import java.util.ArrayList;

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
            String agent = "";
            // how to identify these two values?
            String value = "";
            String condition = "";
            for (int i = 1; i < treeList.size(); i++) {
                agent += treeList.get(i).value + " ";
            }

            Action act = new Action(action, agent.trim(), value.trim(), condition.trim());
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


