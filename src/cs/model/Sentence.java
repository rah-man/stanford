package cs.model;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import cs.util.ConstantEnum;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Sentence {
    protected ArrayList<Constant> conditions, actions;
    protected ArrayList<Expr> declarations, condAssignments, actAssignments, sentenceExpression;
    protected String modelAsSentence;
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

    public Sentence(Context ctx, String modelAsSentence) {
        this.modelAsSentence = modelAsSentence;
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

//
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

    private Stack<NameValueTree> buildFromSentenceModel() {
        Scanner sc = new Scanner(modelAsSentence);
        Stack<String> parenthesis = null;
        Stack<String> operator = null;
        Stack<NameValue> nameValueStack = null;
        Stack<NameValueTree> treeStack = null;

        while (sc.hasNextLine()) {
            boolean afterLeftPar = false;
            parenthesis = new Stack<String>();
            operator = new Stack<String>();
            nameValueStack = new Stack<NameValue>();
            treeStack = new Stack<NameValueTree>();

            String line = sc.nextLine().trim();

            for (int i = 0; i < line.length(); ) {
                char letter = line.charAt(i);

                if (letter == '(') {
                    parenthesis.push(Character.toString(letter));
                    i++;
                    afterLeftPar = true;
                    continue;
                }

                if (letter == ' ') {
                    i++;
                    continue;
                }

                if (letter != ' ' && afterLeftPar) {
                    int opsIndex = operatorIndex(i, line);
                    operator.push(line.substring(i, opsIndex));
                    i = opsIndex;
                    afterLeftPar = false;
                    continue;
                }

                if (letter != ' ' && letter != ')' && !afterLeftPar) {
                    int entityIndex = entityIndex(i, line);
                    String[] nameValue = line.substring(i, entityIndex).split(" ");
                    NameValue nv = new NameValue(nameValue[0].trim(), nameValue[1].trim());
                    nameValueStack.push(nv);
                    i = entityIndex;
                    continue;
                }

                if (letter == ')') {
                    parenthesis.pop();
                    String ops = operator.pop();

                    if (!ops.equals("and") && !ops.equals("or")) {
                        NameValue entity = nameValueStack.pop();
                        entity.mod = ops;
                        nameValueStack.push(entity);
                        i++;
                    } else {
                        NameValueTree sub = (treeStack.empty()) ? null : treeStack.pop();
                        NameValueTree tree = new NameValueTree(ops, nameValueStack, sub);
                        treeStack.push(tree);
                        i++;
                    }

                    if (parenthesis.empty()) {
                        // make context here, because if there's a newline, the current information will be lost
                        break;
                    }
                    continue;
                }
            }
        }

        System.out.println("\nTREE:");
        treeStack.forEach(System.out::println);

        return treeStack;
    }

    private int operatorIndex(int i, String line) {
        for (int j = i; ; j++) {
            char c = line.charAt(j);
            if (c == ' ' || c == '(') {
                return j;
            }
        }
    }

    private int entityIndex(int i, String line) {
        for (int j = i; ; j++) {
            char c = line.charAt(j);
            if (c == ')') {
                return j;
            }
        }
    }
}
