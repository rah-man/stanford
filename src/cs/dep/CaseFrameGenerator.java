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

        caseFrame.actFrame = getActionFrame(head, headChildren);
        caseFrame.conFrame = getConditionFrame(head, headChildren);
    }

    public CaseFrame getCaseFrame() {
        return caseFrame;
    }

    private ActionFrame getActionFrame(TreeNode head, ArrayList<TreeNode> headChildren) {
        ActionFrame actFrame = new ActionFrame();

        // assuming the root always has nsub and dobj
        // PROCESS THE ACTION OBJECT FROM dobj
        // this only works for one action, so far
        /*for (TreeNode node : headChildren.get(DOBJ).children) {
            if (!node.reln.equals("det")) {
                object.add(node);
            }
        }*/

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


        /*
        // assuming the root always has nsub and dobj
        // PROCESS THE CONDITION FROM nsub
        for (TreeNode node : headChildren.get(NSUB).children) {
            if (!(node.reln.equals("case") || node.reln.equals("dep") || node.reln.equals("cc"))) {
                subject.add(node);
            }
        }

        String pattern = "NNS|NN";
        ArrayList<TreeNode> conditions = new ArrayList<TreeNode>();
        // assuming there are at least two children
        // DO I REALLY NEED THE CONDITIONS LIST?
        conditions.add(subject.get(0));
        condFrame.addTree(subject.get(0));
        for (TreeNode node : subject.get(0).children) {
            if (node.tag.matches(pattern)) {
                conditions.add(node);
                condFrame.addTree(node);
            }
        }

        // nmod is an important marker
        boolean modExist = false;
        pattern = "(\\w*)mod";
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        for (TreeNode node : subject.get(1).children) {
            if (node.reln.matches(pattern)) {
                modExist = true;
                queue.add(node);
            }
        }

        // get until no more children (?)
        Set<TreeNode> modTree = new TreeSet<TreeNode>();
        modTree.add(subject.get(1));
        if (modExist) {
            while (!queue.isEmpty()) {
                TreeNode node = queue.removeFirst();
                modTree.add(node);

                queue.addAll(node.children);
            }
        }*/

        condFrame.addTree(new ArrayList<TreeNode>(aSet));
        condFrame.normaliseConditions();
        return condFrame;
    }

    public static void main(String[] args) {
        DependencyParse dp = new DependencyParse();
        List<DependencyTree> parseTreeList = dp.parseSentence();
        CaseFrameGenerator cfGenerator = new CaseFrameGenerator(parseTreeList);

        CaseFrame caseFrame = cfGenerator.getCaseFrame();
        System.out.println("=====CONDITION FRAME=====");
        System.out.println(caseFrame.conFrame);
        System.out.println("=====ACTION FRAME=====");
        System.out.println(caseFrame.actFrame);
    }
}

/**
 * Small container for CaseFrame object. Nothing much.
 */
class CaseFrame {
    protected ConditionFrame conFrame;
    protected ActionFrame actFrame;
}