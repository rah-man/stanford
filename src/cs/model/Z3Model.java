package cs.model;

import com.microsoft.z3.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Z3Model {
    class TestFailedException extends Exception {
        public TestFailedException() {
            super("Check FAILED");
        }
    }

    protected Context ctx;
    protected HashMap<String, String> cfg;
    protected ArrayList<Sentence> sentences;

    public Z3Model() {
        cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);

        sentences = new ArrayList<Sentence>();
    }

    public void buildSentence(ArrayList<Constant> conditions, ArrayList<Constant> actions, boolean orCond, boolean orAct) {
        Sentence sentence = new Sentence(ctx, conditions, actions, orCond, orAct);
        sentences.add(sentence);
    }

    public BoolExpr andAllSentences() {
        ArrayList<Expr> allSent = new ArrayList<Expr>();
        for (Sentence sent : sentences) {
            allSent.addAll(sent.sentenceExpression);
        }

        BoolExpr andAll = ctx.mkAnd(allSent.stream().toArray(BoolExpr[]::new));

        return andAll;
    }

    public void checkModel() {
        try {
            Model model = check(ctx,
                    andAllSentences(),
                    Status.SATISFIABLE);

        /*System.out.println("\nFunction declarations");
        for (FuncDecl func : model.getDecls()) {
            System.out.println(func);
        }*/

        /*System.out.println("\nFunction declarations");
        for (FuncDecl func : model.getFuncDecls()) {
            System.out.println(func);
        }*/

            System.out.println("\nFunction declarations and their values");
            for (FuncDecl func : model.getDecls()) {
                System.out.println(func);
                System.out.println("\t" + model.eval(model.getConstInterp(func), true));
            }
        } catch (Z3Model.TestFailedException e) {
            e.printStackTrace();
        }
    }

    private Model check(Context ctx, BoolExpr f, Status sat) throws Z3Model.TestFailedException {
        Solver s = ctx.mkSolver();
        s.add(f);
        if (s.check() != sat)
            throw new Z3Model.TestFailedException();
        if (sat == Status.SATISFIABLE)
            return s.getModel();
        else
            return null;
    }
}
