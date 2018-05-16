package cs.model;

import java.util.ArrayList;
import java.util.Stack;

public class ConstantTree {
    public String connector;
    public ArrayList<Constant> kids = new ArrayList<Constant>();
    public ConstantTree subTree;


    public ConstantTree(String conjunctor, Stack<Constant> constants, ConstantTree subTree) {
        this.connector = conjunctor;
        while (!constants.empty()) {
            kids.add(constants.pop());
        }
        this.subTree = subTree;
    }

    public String toString() {
        String ret = connector + "\n";
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
