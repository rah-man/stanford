package cs.model;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import cs.util.ConstantEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
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
        this.ctx = ctx;
        this.modelAsSentence = modelAsSentence;
        sentenceExpression = new ArrayList<Expr>();

        buildFromSentenceModel();
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
        list.add(createExpression(c));
    }

    private Expr createExpression(Constant c) {
        Expr exp = null;

        if (c.getType().equals("Bool")) {
            exp = ctx.mkBoolConst(c.name);
        } else if (c.getType().equals("Int")) {
            exp = ctx.mkIntConst(c.name);
        }

        if (declarations != null) {
            declarations.add(exp);
        }
        BoolExpr assignExp = getExpressionAssignmentFromConstant(c, exp);

        return assignExp;
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
            boolean booleanValue = false;
            if (isBooleanValue(c)) {
                booleanValue = getBooleanValue(c);
            }

            assignExpr = (exp instanceof BoolExpr) ?
                    (ctx.mkEq(exp, ctx.mkBool(booleanValue))) : (ctx.mkEq(exp, ctx.mkInt((Integer) c.value)));
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

    private boolean isBooleanValue(Constant c) {
        boolean aBoolean = false;
        if (c.value instanceof Boolean) {
            aBoolean = true;
        } else if (c.value instanceof Integer) {
            aBoolean = false;
        } else if (!StringUtils.isNumeric(((String) c.value).toLowerCase())) {
            aBoolean = true;
        }
        return aBoolean;
    }

    private boolean getBooleanValue(Constant c) {
        if (c.value instanceof Boolean) {
            return (boolean) c.value;
        } else {
            return Boolean.parseBoolean((String) c.value);
        }
    }

    private void buildFromSentenceModel() {
        Scanner sc = new Scanner(modelAsSentence);
        Stack<String> parenthesis = null;
        Stack<String> operator = null;
        Stack<Constant> constantStack = null;
        Stack<ConstantTree> constantTree = null;

        while (sc.hasNextLine()) {
            boolean afterLeftPar = false;
            parenthesis = new Stack<String>();
            operator = new Stack<String>();
            constantStack = new Stack<Constant>();
            constantTree = new Stack<ConstantTree>();

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
                    Constant nv = makeConstant(nameValue);
                    constantStack.push(nv);
                    i = entityIndex;
                    continue;
                }

                if (letter == ')') {
                    parenthesis.pop();
                    String ops = operator.pop();

                    if (!ops.equals("and") && !ops.equals("or")) {
                        Constant entity = constantStack.pop();
                        entity.mod = ops;
                        constantStack.push(entity);
                        i++;
                    } else {
                        ConstantTree sub = (constantTree.empty()) ? null : constantTree.pop();
                        ConstantTree tree = new ConstantTree(ops, constantStack, sub);
                        constantTree.push(tree);
                        i++;
                    }

                    if (parenthesis.empty()) {
                        break;
                    }
                    continue;
                }
            }

            if (!constantStack.isEmpty()) {
//                System.out.println("NameValueStack");
//                constantStack.forEach(System.out::println);
                for (Constant c : constantStack) {
                    sentenceExpression.add(createExpression(c));
                }
            }

            if (!constantTree.isEmpty()) {
//                System.out.println("TreeStack");
//                constantTree.forEach(System.out::println);
                buildModelFromTree(constantTree);
            }
        }
    }

    private Constant makeConstant(String[] nameValue){
        Constant cons;
        String name = nameValue[0].trim();
        String value = nameValue[1].trim();

        if(!StringUtils.isNumeric(((String) value).toLowerCase())){
            cons = new Constant(name, Boolean.parseBoolean(value));
        }else{
            cons = new Constant(name, Integer.parseInt(value));
        }

        return cons;
    }

    private void buildModelFromTree(Stack<ConstantTree> constantTree){
        ConstantTree root = constantTree.peek();
        LinkedList<ConstantTree> treeList = new LinkedList<ConstantTree>();
        treeList.add(root);
        while(root.subTree != null){
            treeList.addFirst(root.subTree);
            root = root.subTree;
        }

        ArrayList<Expr> kidsExpression = new ArrayList<Expr>();

        for(ConstantTree cTree : treeList){
            String connector = cTree.connector;
            ArrayList<Constant> kids = cTree.kids;

            for (Constant c : kids) {
                Expr e = createExpression(c);
                kidsExpression.add(e);
//                System.out.println("EXPRESSION= " + e);
            }

            BoolExpr combinedSentence = connector.equals("and")
                    ? ctx.mkAnd(kidsExpression.stream().toArray(BoolExpr[]::new))
                    : ctx.mkOr(kidsExpression.stream().toArray(BoolExpr[]::new));
            kidsExpression.clear();
            kidsExpression.add(combinedSentence);
//            System.out.println("COMBINED SENTENCE= " + combinedSentence);
        }

        sentenceExpression.add(kidsExpression.get(0));
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
