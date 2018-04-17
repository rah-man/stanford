package cs.dep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionFrame {
    protected ArrayList<ArrayList<TreeNode>> conditionTree = new ArrayList<ArrayList<TreeNode>>();
    protected ArrayList<Condition> condList;
    private final String ORMORE = "or more";
    private final String OF = "of";
    private final String ORMORE_SUBS = ">=";
    private final String WITH = "with";
    private final String WITHOUT = "without";
    private final int CONDITION = 0;
    private final int MOD = 1;
    private final int VALUE = 2;
    private final String[] mathSymbol = {"<", "=", ">", "!"};
    private final boolean OR = false;

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
                Condition cond = new Condition(treeList.get(CONDITION).value, "=", "true", OR);
                condList.add(cond);
            } else if (treeList.size() == 2) {
                if (treeList.get(0).value.equals(WITHOUT)) {
                    Condition cond = new Condition(treeList.get(1).value, "=", "false", OR);
                    condList.add(cond);
                } else if (treeList.get(0).value.equals(WITH)) {
                    Condition cond = new Condition(treeList.get(1).value, "=", "true", OR);
                    condList.add(cond);
                }
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

                if (flatCondition.contains(WITH)) {
                    flatCondition = flatCondition.replaceAll(WITH, "");
                }

                String[] split = flatCondition.trim().split(" ");
                Condition cond = new Condition(split[CONDITION], split[MOD],
                        flattenStringList(Arrays.asList(Arrays.copyOfRange(split, VALUE, split.length))), OR);
                condList.add(cond);
            }
        }

        return condList;
    }

    private String flattenStringList(List<String> list) {
        String text = "";
        boolean prevIsMathSymbol = false;
        for (String s : list) {
            if (containsMathSymbol(s)) {
                if (prevIsMathSymbol) {
                    text = text.trim() + s + " ";
                    prevIsMathSymbol = false;
                } else {
                    text += s + " ";
                    prevIsMathSymbol = true;
                }
            } else {
                text += s + " ";
            }
        }
        return text.trim();
    }

    private boolean containsMathSymbol(String inputString) {
        return Arrays.stream(mathSymbol).parallel().anyMatch(inputString::contains);
    }

    public String toString() {
        String output = "";
        for (Condition c : condList) {
            output += c.toString() + "\n";
        }
        return output;
    }

    public ArrayList<Condition> getCondList() {
        return condList;
    }
}

