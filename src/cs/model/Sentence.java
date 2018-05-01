package cs.model;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import cs.util.ConstantEnum;

import java.util.ArrayList;

public class Sentence {
    protected ArrayList<Constant> conditions, actions;
    protected ArrayList<Expr> declarations, condAssignments, actAssignments, sentenceExpression;
    protected boolean orCond, orAct;
    protected Context ctx;

    public Sentence(Context ctx, ArrayList<Constant> conditions, ArrayList<Constant> actions, boolean orCond, boolean orAct) {
        this.ctx = ctx;
        this.conditions = conditions;
        this.actions = actions;
        this.orCond = orCond;
        this.orAct = orAct;

        declarations = new ArrayList<Expr>();
        condAssignments = new ArrayList<Expr>();
        actAssignments = new ArrayList<Expr>();
        sentenceExpression = new ArrayList<Expr>();

        getSentenceExpression();
    }

    public void getSentenceExpression() {
        for (Constant c : conditions) {
            setDeclarationsAndAssignments(c, condAssignments);
        }

        for (Constant c : actions) {
            setDeclarationsAndAssignments(c, actAssignments);
        }

        setBiimplicationFromAssignments();

//        System.out.println("\nDECLARATION (OF WAR)");
//        actAssignments.forEach(System.out::println);
//        condAssignments.forEach(System.out::println);

        System.out.println("\nSENTENCE");
        for (Expr e : sentenceExpression) {
            System.out.println(e);
        }
    }

    private void setDeclarationsAndAssignments(Constant c, ArrayList<Expr> list) {
        Expr exp = null;

        if (c.getType().equals("Bool")) {
            exp = ctx.mkBoolConst(c.name);
        } else if (c.getType().equals("Int")) {
            exp = ctx.mkIntConst(c.name);
        }

        BoolExpr assignExp = getExpressionAssignmentFromConstant(c, exp);
        declarations.add(exp);
        list.add(assignExp);
    }

    private void setBiimplicationFromAssignments() {
        BoolExpr combinedCondition = (orCond) ? ctx.mkOr(condAssignments.stream().toArray(BoolExpr[]::new))
                : ctx.mkAnd(condAssignments.stream().toArray(BoolExpr[]::new));

        boolean bpAction = isBPAction();

        if (bpAction) {
            setBPBiimplication(combinedCondition);
        } else if (orAct) {
            setOrActionBiimplication(combinedCondition);
        } else {
            setNormalBiimplication(combinedCondition);
        }
    }

    private void setOrActionBiimplication(BoolExpr combinedCondition) {
        // combine all actions into one or expression
        BoolExpr orActionExpression = ctx.mkOr(actAssignments.stream().toArray(BoolExpr[]::new));
        BoolExpr fin = ctx.mkIff(combinedCondition, orActionExpression);
        sentenceExpression.add(fin);
    }

    private void setBPBiimplication(BoolExpr combinedCondition) {
        // combine all actions into one and expression
        BoolExpr andActionExpression = ctx.mkAnd(actAssignments.stream().toArray(BoolExpr[]::new));
        BoolExpr fin = ctx.mkIff(combinedCondition, andActionExpression);
        sentenceExpression.add(fin);
    }

    private void setNormalBiimplication(BoolExpr combinedCondition) {
        for (Expr e : actAssignments) {
            BoolExpr fin = ctx.mkIff(combinedCondition, (BoolExpr) e);
            sentenceExpression.add(fin);
        }
    }

    private boolean isBPAction() {
        boolean bpAction = false;

        for (Constant c : actions) {
            if (c.name.startsWith("blood-pressure")) {
                bpAction = true;
                break;
            }
        }

        return bpAction;
    }

    private BoolExpr getExpressionAssignmentFromConstant(Constant c, Expr exp) {
        BoolExpr assignExpr = null;

        if (c.getMod().equals(ConstantEnum.EQ)) {
            assignExpr = (exp instanceof BoolExpr) ?
                    (ctx.mkEq(exp, ctx.mkBool((Boolean) c.value))) : (ctx.mkEq(exp, ctx.mkInt((Integer) c.value)));
        } else if (c.getMod().equals(ConstantEnum.GT)) {
            assignExpr = ctx.mkGt((IntExpr) exp, ctx.mkInt((Integer) c.value));
        } else if (c.getMod().equals(ConstantEnum.GTE)) {
            assignExpr = ctx.mkGe((IntExpr) exp, ctx.mkInt((Integer) c.value));
        } else if (c.getMod().equals(ConstantEnum.LT)) {
            assignExpr = ctx.mkLt((IntExpr) exp, ctx.mkInt((Integer) c.value));
        } else if (c.getMod().equals(ConstantEnum.LTE)) {
            assignExpr = ctx.mkLe((IntExpr) exp, ctx.mkInt((Integer) c.value));
        }

        return assignExpr;
    }
}
