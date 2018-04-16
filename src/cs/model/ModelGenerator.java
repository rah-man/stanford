package cs.model;

import cs.dep.*;
import cs.gui.*;

import javax.swing.*;

public class ModelGenerator {
    CaseFramePanel[] caseFramePanels;

    public ModelGenerator() {
        this(null);
    }

    public ModelGenerator(CaseFramePanel[] caseFramePanels) {
        this.caseFramePanels = caseFramePanels;

        getFrames();
    }

    private void getFrames() {
        for (CaseFramePanel caseFrame : caseFramePanels) {
            System.out.println("=================================");
            JTable conditionTable = caseFrame.getConditionTable();
            JTable actionTable = caseFrame.getActionTable();
            String[] conditionColumnNames = ((ConditionTableModel) conditionTable.getModel()).columnNames;
            String[] actionColumnNames = ((ActionTableModel) actionTable.getModel()).columnNames;

            int conditionColumnCount = conditionTable.getColumnCount() - 1;
            int conditionRowCount = conditionTable.getRowCount();
            for (int i = 0; i < conditionRowCount; i++) {
                for (int j = 0; j < conditionColumnCount; j++) {
                    System.out.println(conditionColumnNames[j] + ": " + conditionTable.getModel().getValueAt(i, j));
                }
                System.out.println();
            }
        }

    }
}
