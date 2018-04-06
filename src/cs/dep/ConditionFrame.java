package cs.dep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

class Condition {
    protected String condition;
    protected String mod;
    protected String value;

    public Condition() {
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
