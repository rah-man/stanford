package cs.model;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.sun.org.apache.xpath.internal.operations.Bool;
import cs.util.ConstantEnum;

import java.util.ArrayList;
import java.util.HashMap;

public class Sentence {
    protected ArrayList<Constant> conditions;
    protected ArrayList<Constant> actions;
    protected Context ctx;
    protected HashMap<String, String> cfg;
    ArrayList<Expr> declarations;
    ArrayList<Expr> condAssignments;
    ArrayList<Expr> actAssignments;
    ArrayList<Expr> sentExpression;

    public Sentence(ArrayList<Constant> conditions, ArrayList<Constant> actions) {
        this.conditions = conditions;
        this.actions = actions;

        cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);

        declarations = new ArrayList<Expr>();
        condAssignments = new ArrayList<Expr>();
        actAssignments = new ArrayList<Expr>();
        sentExpression = new ArrayList<Expr>();

        getSentenceExpression();
    }

    public void getSentenceExpression() {
        /*
        BoolExpr ckd = ctx.mkBoolConst("ckd");
        IntExpr acr = ctx.mkIntConst("acr");
        BoolExpr lowCostRenin = ctx.mkBoolConst("low-cost-renin-angiotensin-aldosterone-system-antagonist");
        BoolExpr hypertension = ctx.mkBoolConst("hypertension");

        BoolExpr ckdTrue = ctx.mkEq(ckd, ctx.mkBool(true));
        BoolExpr acr30 = ctx.mkGe(acr, ctx.mkInt(30));
        BoolExpr hypertensionTrue = ctx.mkEq(hypertension, ctx.mkBool(true));
        BoolExpr lowCostReninTrue = ctx.mkEq(lowCostRenin, ctx.mkBool(true));

        BoolExpr andEverything = ctx.mkAnd(ckdTrue, ctx.mkAnd(acr30, ctx.mkAnd(hypertensionTrue, lowCostReninTrue)));

        sent1 = ctx.mkEq(ctx.mkAnd(ckdTrue, diabetesTrue, acrGE3), lowCostReninTrue);
         */

        for (Constant c : conditions) {
            setDeclarationsAndAssignments(c, condAssignments);
        }

        for (Constant c : actions) {
            setDeclarationsAndAssignments(c, actAssignments);
        }

        setBiimplicationFromAssignments();

        for (Expr e : sentExpression) {
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
        BoolExpr andExpression = ctx.mkAnd(condAssignments.stream().toArray(BoolExpr[]::new));

        for (Expr e : actAssignments) {
            BoolExpr fin = ctx.mkIff(andExpression, (BoolExpr) e);
            sentExpression.add(fin);
        }
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
