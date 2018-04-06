package cs.dep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionFrame {
    private ArrayList<ArrayList<TreeNode>> conditionTree = new ArrayList<ArrayList<TreeNode>>();
    protected ArrayList<Condition> condList;
    private final String ORMORE = "or more";
    private final String OF = "of";
    private final String ORMORE_SUBS = "<=";
    private final int CONDITION = 0;
    private final int MOD = 1;
    private final int VALUE = 2;

    public ConditionFrame() {
        condList = new ArrayList<Condition>();
    }

    public void addTree(TreeNode node) {
        ArrayList<TreeNode> tree = new ArrayList<TreeNode>();
        tree.add(node);
        addTree(tree);
    }

    public void addTree(ArrayList<TreeNode> tree) {
        conditionTree.add(tree);
    }

    public ArrayList<Condition> normaliseConditions() {
        for (ArrayList<TreeNode> treeList : conditionTree) {
            if (treeList.size() == 1) {
                Condition cond = new Condition(treeList.get(CONDITION).value, "=", "true");
                condList.add(cond);
            } else {
                ArrayList<String> l = new ArrayList<String>();
                for (TreeNode node : treeList) {
                    l.add(node.value);
                }

                String flatCondition = flattenStringList(l);
                if (flatCondition.contains(ORMORE)) {
                    flatCondition = flatCondition.replaceAll(ORMORE, "");
                    if (flatCondition.contains(OF)) {
                        flatCondition = flatCondition.replaceAll(OF, ORMORE_SUBS);
                    }
                }

                String[] split = flatCondition.split(" ");
                Condition cond = new Condition(split[CONDITION], split[MOD],
                        flattenStringList(Arrays.asList(Arrays.copyOfRange(split, VALUE, split.length))));
                condList.add(cond);
            }
        }

        return condList;
    }

    private String flattenStringList(List<String> list) {
        String text = "";
        for (String s : list) {
            text += s + " ";
        }
        return text.trim();
    }

    public String toString() {
        String output = "";
        for (Condition c : condList) {
            output += c.toString() + "\n";
        }
        return output;
    }
}

