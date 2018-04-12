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
            String action = "";
            String agent = "";
            String value = "";
            String condition = "";

            if (treeList.get(0).reln.equals("compound") && treeList.get(1).reln.equals("compound")) {
                //do 4&5 here
                action = treeList.get(ACTION).value;
                boolean stop = false;
                for (int i = ACTION + 1; ; i++) {
                    TreeNode node = treeList.get(i);
                    if (!treeList.get(i).reln.equals("case")) {
                        agent += node.value + " ";
                    } else {
                        break;
                    }
                }

                // find last case index
                int caseIndex = 0;
                for (int i = 0; i < treeList.size(); i++) {
                    if (treeList.get(i).reln.equals("case")) {
                        caseIndex = i;
                    }
                }

                for (int i = caseIndex + 1; i < treeList.size(); i++) {
                    value += treeList.get(i).value + " ";
                }
            } else {
                action = treeList.get(ACTION).value;
                for (int i = 1; i < treeList.size(); i++) {
                    agent += treeList.get(i).value + " ";
                }
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


