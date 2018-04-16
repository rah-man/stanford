package cs.dep;

import java.io.Serializable;
import java.util.ArrayList;

public class TreeNode implements Comparable<TreeNode>, Serializable {
    protected String value;
    protected String tag;
    protected int index;
    protected TreeNode parent;
    protected ArrayList<TreeNode> children;
    protected String reln;
    protected String relnLong;

    public TreeNode(String value, String tag, int index) {
        this.value = value;
        this.tag = tag;
        this.index = index;
        children = new ArrayList<TreeNode>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(index + "/" + value + "/" + tag + "/" + reln);
        sb.append("\n");
        sb.append("\tPARENT: " + ((parent == null) ? (" -- ") : (parent.index + "/" + parent.value + "/" + parent.tag + "/" + parent.reln)));
        sb.append("\n");
        sb.append("\tChildren:\n");
        for (TreeNode n : children) {
            sb.append("\t\t" + n.index + "/" + n.value + "/" + n.tag + "/" + n.reln);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void printNode() {
        System.out.println(toString());
    }

    @Override
    public int compareTo(TreeNode o) {
        return index - o.index;
    }
}
