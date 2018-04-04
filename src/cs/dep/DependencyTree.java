package cs.dep;

import java.util.LinkedHashMap;

public class DependencyTree {
    private LinkedHashMap<Integer, TreeNode> tree;

    public DependencyTree() {
        this(null);
    }

    public DependencyTree(LinkedHashMap<Integer, TreeNode> tree) {
        this.tree = tree;
    }

    public TreeNode getRoot() {
        return (tree.containsKey(0)) ? tree.get(0) : null;
    }

    public LinkedHashMap<Integer, TreeNode> getTree() {
        return tree;
    }
}
