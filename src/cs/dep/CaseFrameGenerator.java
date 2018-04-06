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

        ArrayList<TreeNode> headChildren = new ArrayList<TreeNode>();
        for (TreeNode node : head.children) {
            if (!node.reln.equals("punct")) {
                headChildren.add(node);
            }
        }

        caseFrame.actionFrame = getActionFrame(head, headChildren);
        caseFrame.conditionFrame = getConditionFrame(head, headChildren);
    }

    public ArrayList<Condition> getConditionList() {
        return caseFrame.conditionFrame.condList;
    }

    public ArrayList<Action> getActionList() {
        return caseFrame.actionFrame.actionList;
    }

    private ActionFrame getActionFrame(TreeNode head, ArrayList<TreeNode> headChildren) {
        ActionFrame actFrame = new ActionFrame();

        Set<TreeNode> actTree = new TreeSet<TreeNode>();
        actTree.add(head);
        actTree.add(headChildren.get(DOBJ));
        for (TreeNode node : headChildren.get(DOBJ).children) {
            if (!(node.reln.equals("det") || node.reln.equals("punct") || node.reln.equals("nmod")
                    || node.reln.equals("cc") || node.reln.equals("conj"))) {
                actTree.add(node);
            }
        }

        actFrame.addTree(new ArrayList<TreeNode>(actTree));
        actFrame.normaliseAction();
        return actFrame;
    }

    private ConditionFrame getConditionFrame(TreeNode head, ArrayList<TreeNode> headChildren) {
        // NEW RULE
        ConditionFrame condFrame = new ConditionFrame();
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

