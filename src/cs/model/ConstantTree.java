package cs.model;

import java.util.ArrayList;
import java.util.Stack;

public class ConstantTree {
    public String conjunctor;
    public ArrayList<Constant> kids = new ArrayList<Constant>();
    public ConstantTree subTree;


    public ConstantTree(String conjunctor, Stack<Constant> nvStack, ConstantTree subTree) {
        this.conjunctor = conjunctor;
        while (!nvStack.empty()) {
            kids.add(nvStack.pop());
        }
        this.subTree = subTree;
    }

    public String toString() {
        String ret = conjunctor + "\n";
        for (Constant nv : kids) {
            ret += "\tKIDS" + nv + "\n";
        }
        if (subTree != null) {
            ret += "\tSUBTREE\n";
            ret += subTree;
        }
        return ret;
    }
}
