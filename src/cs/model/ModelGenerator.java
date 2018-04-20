package cs.model;

import cs.dep.*;
import cs.dep.Action;
import cs.gui.*;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModelGenerator {
    protected static final int CONDITION = 0;
    protected static final int MOD = 1;
    protected static final int CONDITION_VALUE = 2;
    protected static final int OR_CONJUCTION = 3;
    protected static final int ACTION = 0;
    protected static final int AGENT = 1;
    protected static final int ACTION_VALUE = 2;
    protected static final int ACTION_CONDITION = 3;
    protected static final int ACTION_OR_CONDITION = 4;


    protected CaseFramePanel[] caseFramePanels;
    protected ArrayList<ArrayList<Condition>> conditions;
    protected ArrayList<ArrayList<Action>> actions;
    protected Set<String> declarationSet, assertionSet;

    public ModelGenerator() {
        this(null);
    }

    public ModelGenerator(CaseFramePanel[] caseFramePanels) {
        this.caseFramePanels = caseFramePanels;
        conditions = new ArrayList<ArrayList<Condition>>();
        actions = new ArrayList<ArrayList<Action>>();
        declarationSet = new LinkedHashSet<String>();
        assertionSet = new LinkedHashSet<String>();

        getFrames();
    }

    private void getFrames() {
        for (CaseFramePanel caseFramePanel : caseFramePanels) {
            getActionFrames(caseFramePanel);
            getConditionFrame(caseFramePanel);
        }
        getConditionAndActionConstants();
        declarationSet.forEach(System.out::println);
        System.out.println();
        assertionSet.forEach(System.out::println);
    }

    private void getConditionAndActionConstants() {
        for (int i = 0; i < conditions.size(); i++) {
            ArrayList<Condition> c = conditions.get(i);
            boolean orConj = c.stream()
                    .filter(s -> s.or == true)
                    .map(Condition::getOR)
                    .findAny()
                    .orElse(false);

            Constant[] cons = new Constant[c.size()];
            int consIndex = 0;
            for (Condition cond : c) {
                Constant constant = ConstantFactory.generateConstant(cond);
                cons[consIndex++] = constant;

                declarationSet.add(constant.declareConstant());
                if (!orConj) {
                    assertionSet.add(constant.assertConstant());
                }
            }

            if (orConj) {
                assertionSet.add(ConstantFactory.assertCombineConstant("or", cons));
            }

            ArrayList<Action> a = actions.get(i);
            ArrayList<Constant> constList = new ArrayList<Constant>();
            orConj = a.stream()
                    .filter(s -> s.or == true)
                    .map(Action::getOR)
                    .findAny()
                    .orElse(false);

            for (Action act : a) {
                Constant[] constants = ConstantFactory.generateConstant(act);
                constList.addAll(Arrays.asList(ConstantFactory.generateConstant(act)));
                for (Constant actionCons : constants) {
                    declarationSet.add(actionCons.declareConstant());

                    if (!orConj) {
                        assertionSet.add(actionCons.assertConstant());
                    }
                }
            }

            if (orConj) {
                assertionSet.add(ConstantFactory.assertCombineConstant("or", constList.stream().toArray(Constant[]::new)));
            }
        }
    }

    private void getActionFrames(CaseFramePanel caseFramePanel) {
        JTable actionTable = caseFramePanel.getActionTable();
        int actionRowCount = actionTable.getRowCount();
        TableModel actionModel = actionTable.getModel();
        ArrayList<Action> actionList = new ArrayList<Action>();

        for (int i = 0; i < actionRowCount; i++) {
            Action action = new Action((String) actionModel.getValueAt(i, ACTION),
                    (String) actionModel.getValueAt(i, AGENT),
                    (String) actionModel.getValueAt(i, ACTION_VALUE),
                    (String) actionModel.getValueAt(i, ACTION_CONDITION),
                    (boolean) actionModel.getValueAt(i, ACTION_OR_CONDITION));
            actionList.add(action);
        }

        actions.add(actionList);
    }

    private void getConditionFrame(CaseFramePanel caseFramePanel) {
        JTable conditionTable = caseFramePanel.getConditionTable();
        int conditionRowCount = conditionTable.getRowCount();
        TableModel conditionModel = conditionTable.getModel();
        ArrayList<Condition> conditionList = new ArrayList<Condition>();

        for (int i = 0; i < conditionRowCount; i++) {
            Condition condition = new Condition((String) conditionModel.getValueAt(i, CONDITION),
                    (String) conditionModel.getValueAt(i, MOD),
                    (String) conditionModel.getValueAt(i, CONDITION_VALUE),
                    (boolean) conditionModel.getValueAt(i, OR_CONJUCTION));
            conditionList.add(condition);
        }

        conditions.add(conditionList);
    }
}
