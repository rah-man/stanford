package cs.dep;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.StringReader;
import java.util.*;

public class DependencyParse {
    protected Redwood.RedwoodChannels log = Redwood.channels(new Object[]{edu.stanford.nlp.parser.nndep.demo.DependencyParserDemo.class});
    protected MaxentTagger tagger = null;
    protected edu.stanford.nlp.parser.nndep.DependencyParser parser = null;
    protected String modelPath, taggerPath, text;
    protected Collection<TypedDependency> dependencies;

    public DependencyParse() {
        this("edu/stanford/nlp/models/parser/nndep/english_UD.gz",
                "edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger",
                "Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and diabetes and an ACR of 3 mg/mmol or more (ACR category A2 or A3).");
    }

    public DependencyParse(String text) {
        this("edu/stanford/nlp/models/parser/nndep/english_UD.gz",
                "edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger",
                text);
    }

    public DependencyParse(String modelPath, String taggerPath, String text) {
        this.modelPath = modelPath;
        this.taggerPath = taggerPath;
        this.text = text;

        tagger = new MaxentTagger(taggerPath);
        parser = edu.stanford.nlp.parser.nndep.DependencyParser.loadFromModelFile(modelPath);
    }

    public List<DependencyTree> parseSentence() {
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
        Iterator it = tokenizer.iterator();
        List<DependencyTree> treeList = new ArrayList<DependencyTree>();

        while (it.hasNext()) {
            List<HasWord> sentence = (List) it.next();
            List<TaggedWord> tagged = tagger.tagSentence(sentence);
            GrammaticalStructure gs = parser.predict(tagged);

            dependencies = gs.typedDependencies();
            Collection<TypedDependency> typedDependencies = gs.allTypedDependencies();
            LinkedHashMap<Integer, TreeNode> nodeMap = new LinkedHashMap<Integer, TreeNode>();
            nodeMap.put(0, new TreeNode("ROOT", null, 0));
            for (TypedDependency t : typedDependencies) {
//                IndexedWord gov = t.gov();
                IndexedWord dep = t.dep();
                GrammaticalRelation reln = t.reln();
//                System.out.println(gov.value() + "\\" + gov.tag() + "\\" + gov.index());
//                System.out.println(reln + " : " + gov + " --> " + dep);
//                System.out.println(t);
                TreeNode node = new TreeNode(dep.value(), dep.tag(), dep.index());
                node.reln = reln.getShortName();
                node.relnLong = reln.getLongName();
                nodeMap.put(dep.index(), node);
            }

            for (TypedDependency t : typedDependencies) {
                IndexedWord gov = t.gov();
                IndexedWord dep = t.dep();
                TreeNode node = nodeMap.get(dep.index());
                TreeNode parent = (gov.value().equals("ROOT")) ? nodeMap.get(0) : nodeMap.get(gov.index());
                node.parent = parent;
                parent.children.add(node);
            }
            treeList.add(new DependencyTree(nodeMap));
        }

        return treeList;
    }

    public void printDependencyStructure(TreeNode root) {
        LinkedList<TreeNode> tree = new LinkedList<TreeNode>();
        tree.add(root);
        while (!tree.isEmpty()) {
            TreeNode node = tree.removeFirst();
            node.printNode();
            tree.addAll(node.children);
        }
    }

    public void printTypedDependency() {
        for (TypedDependency t : dependencies) {
            System.out.println(t);
        }
    }

    public String getText() {
        return text;
    }

    public static void main(String[] args) {
        DependencyParse dp = new DependencyParse();
        List<DependencyTree> parseList = dp.parseSentence();
        for (DependencyTree tree : parseList) {
            dp.printDependencyStructure(tree.getRoot());
        }

        dp.printTypedDependency();
    }
}