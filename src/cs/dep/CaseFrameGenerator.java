package cs.dep;

import java.util.*;

public class CaseFrameGenerator {
    private static final int ROOT = 0;
    private static final int NSUB = 0;
    private static final int DOBJ = 0;

    protected CaseFrame caseFrame;
    protected List<DependencyTree> parseTreeList;

    public CaseFrameGenerator() {
        this(null);
    }

    public CaseFrameGenerator(List<DependencyTree> parseTreeList) {
        caseFrame = new CaseFrame();
        this.parseTreeList = parseTreeList;
        buildCaseFrame();
    }

    private void buildCaseFrame() {
        ConditionFrame condFrame = new ConditionFrame();
        ActionFrame actFrame = new ActionFrame();

        DependencyTree tree = parseTreeList.get(0);
        TreeNode root = tree.getRoot();             // ROOT
        TreeNode head = root.children.get(ROOT);    // OFFER

        try {
            // should they be separated or combined??
            caseFrame.actionFrame = getActionFrame(head);
            caseFrame.conditionFrame = getConditionFrame(head);
        } catch (NullPointerException e) {
            //AND DO NOTHING?
        }
    }

    private ArrayList<TreeNode> getHeadChildren(TreeNode head) {
        ArrayList<TreeNode> headChildren = new ArrayList<TreeNode>();
        for (TreeNode node : head.children) {
            if (!node.reln.equals("punct")) {
                headChildren.add(node);
            }
        }
        return headChildren;
    }

    public ArrayList<Condition> getConditionList() {
        return (caseFrame.conditionFrame != null) ? caseFrame.conditionFrame.condList : null;
    }

    public ArrayList<Action> getActionList() {
        return (caseFrame.actionFrame != null) ? caseFrame.actionFrame.actionList : null;
    }

    private ActionFrame getActionFrame(TreeNode head) {
        ActionFrame actFrame = null;
        ArrayList<TreeNode> headChildren = getHeadChildren(head);
        if (headChildren.get(0).reln.equals("dobj")) {
            actFrame = getActionFrameFromRule1(head);
        } else {
            actFrame = getActionFrameFromRule2(head);
        }

        return actFrame;
    }

    private ActionFrame getActionFrameFromRule1(TreeNode head) {
        ActionFrame actionFrame = new ActionFrame();
        ArrayList<TreeNode> headChildren = getHeadChildren(head);
        Set<TreeNode> actTree = new TreeSet<TreeNode>();
        actTree.add(head);
        actTree.add(headChildren.get(DOBJ));
        for (TreeNode node : headChildren.get(DOBJ).children) {
            if (!(node.reln.equals("det") || node.reln.equals("punct") || node.reln.equals("nmod")
                    || node.reln.equals("cc") || node.reln.equals("conj"))) {
                actTree.add(node);
            }
        }

        actionFrame.addTree(new ArrayList<TreeNode>(actTree));
        actionFrame.normaliseAction();
        return actionFrame;
    }

    private ActionFrame getActionFrameFromRule2(TreeNode head) {
        ActionFrame actionFrame = new ActionFrame();
        Set<TreeNode> actTree = new TreeSet<TreeNode>();
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(head);
        while (!queue.isEmpty()) {
            TreeNode node = queue.removeFirst();
            TreeNode parent = node.parent;
            if (!node.value.equals(".") || (node.reln.equals("nmod") && parent != null && !parent.reln.equals("dep"))) {
                actTree.add(node);
            }

            for (TreeNode c : node.children) {
                if (!(node.reln.equals("dep") && c.reln.equals("nmod"))) {
                    queue.add(c);
                }
            }
        }

        actionFrame.addTree(new ArrayList<TreeNode>(actTree));
        actionFrame.normaliseAction();
        return actionFrame;
    }

    private ConditionFrame getConditionFrame(TreeNode head) {
        ConditionFrame conditionFrame = null;
        try {
            conditionFrame = getConditionFromRule1(head);
        } catch (NullPointerException e) {
            conditionFrame = getConditionFromRule2(head);
        }
        return conditionFrame;
    }

    private ConditionFrame getConditionFromRule1(TreeNode head) {
        // NEW RULE
        ConditionFrame condFrame = new ConditionFrame();
        ArrayList<TreeNode> headChildren = getHeadChildren(head);
        // find nmod and conj from headChildren.get(DOBJ).children >__<
        TreeNode nmod = null;
        TreeNode conj = null;
        boolean conjWasNull = true;
        for (TreeNode node : headChildren.get(DOBJ).children) {
            if (node.reln.equals("nmod")) {
                nmod = node;
            }
            if (node.reln.equals("conj")) {
                conj = node;
                conjWasNull = false;
            }
        }

        LinkedList<TreeNode> tempQueue = new LinkedList<TreeNode>();

        if (conj == null) {
            tempQueue.add(nmod);
            findConj:
            while (!tempQueue.isEmpty()) {
                TreeNode t = tempQueue.removeFirst();
                for (TreeNode c : t.children) {
                    if (c.reln.equals("conj")) {
                        conj = c;
                        break findConj;
                    }
                }
                tempQueue.addAll(t.children);
            }
        }

        // recursively traverse on nmod until no more conj found
        tempQueue = new LinkedList<TreeNode>();
        tempQueue.addAll(nmod.children);
        while (!tempQueue.isEmpty()) {
            TreeNode t = tempQueue.removeFirst();
            if (t.reln.equals("nmod") || (t.reln.equals("conj") && !conjWasNull)) {
                condFrame.addTree(t);
            }

            // TO DO: ADD ONLY THE CHILDREN THAT FOLLOW THE IF CRITERIA
            for (TreeNode c : t.children) {
                if (t.reln.equals("nmod") || (t.reln.equals("conj") && !conjWasNull)) {
                    tempQueue.add(c);
                }
            }
        }

        // recursively traverse on conj(?)
        Set<TreeNode> aSet = new TreeSet<TreeNode>();
        tempQueue.add(conj);
        while (!tempQueue.isEmpty()) {
            TreeNode t = tempQueue.removeFirst();
            if (!(t.reln.equals("det") || t.reln.equals("dep") || t.reln.equals("appos"))) {
                aSet.add(t);
            }

            // TO DO: ADD ONLY THE CHILDREN THAT FOLLOW THE IF CRITERIA
            for (TreeNode c : t.children) {
                if (!(c.reln.equals("det") || c.reln.equals("dep") || c.reln.equals("appos"))) {
                    tempQueue.add(c);
                }
            }
        }

        condFrame.addTree(new ArrayList<TreeNode>(aSet));
        condFrame.normaliseConditions();
        return condFrame;
    }

    private ConditionFrame getConditionFromRule2(TreeNode head) {
        ConditionFrame conditionFrame = new ConditionFrame();

        TreeNode depNode = findRelnInChildren(head, "dep");
        TreeNode conditionHead = findConditionHead(depNode);

        Set<TreeNode> conditionSet = new TreeSet<TreeNode>();
        conditionSet.add(conditionHead);
        conditionSet.add(findRelnInChildren(conditionHead, "case"));
        conditionFrame.addTree(new ArrayList<TreeNode>(conditionSet));

        conditionSet = new TreeSet<TreeNode>();
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        TreeNode subTree = findRelnInChildren(conditionHead, "nmod");
        if (subTree == null) {
            subTree = findRelnInChildren(conditionHead, "conj");
        }
        queue.add(subTree);
        while (!queue.isEmpty()) {
            TreeNode node = queue.remove();
            conditionSet.add(node);
            queue.addAll(node.children);
        }
        conditionFrame.addTree(new ArrayList<TreeNode>(conditionSet));
        conditionFrame.normaliseConditions();

        return conditionFrame;
    }

    private TreeNode findConditionHead(TreeNode head) {
        TreeNode node = findRelnInChildren(head, "nmod");
        node = findRelnInChildren(node, "nmod");
        if (node == null) {
            node = findRelnInChildren(head, "nmod", true);
        }
        return node;
    }

    private TreeNode findRelnInChildren(TreeNode head, String relnName) {
        return findRelnInChildren(head, relnName, false);
    }

    private TreeNode findRelnInChildren(TreeNode head, String relnName, boolean canContinue) {
        TreeNode node = null;

        for (TreeNode c : head.children) {
            if (c.reln.equals(relnName)) {
                node = c;
                if (!canContinue) {
                    break;
                }
            }
        }

        return node;
    }

    public CaseFrame getCaseFrame() {
        return caseFrame;
    }

    public static void main(String[] args) {
        DependencyParse dp = new DependencyParse();
        List<DependencyTree> parseTreeList = dp.parseSentence();
        CaseFrameGenerator cfGenerator = new CaseFrameGenerator(parseTreeList);

        CaseFrame caseFrame = cfGenerator.getCaseFrame();
        System.out.println("=====CONDITION FRAME=====");
        System.out.println(caseFrame.conditionFrame);
        System.out.println("=====ACTION FRAME=====");
        System.out.println(caseFrame.actionFrame);
    }
}

