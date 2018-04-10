package cs.gui;

import cs.dep.CaseFrameGenerator;
import cs.dep.Condition;
import cs.dep.Action;
import cs.dep.DependencyParse;
import cs.dep.DependencyTree;

import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CaseFrameGUI extends JPanel {
    protected JFrame mainFrame = new JFrame("Case Frame");
    protected String text;
    protected JEditorPane editorPane;
    protected JTable conditionTable;
    protected JTable actionTable;
    protected CaseFrameGenerator cfGenerator;

    public CaseFrameGUI() {
        super(new GridLayout(3, 0));

        String text = "Control blood pressure to targets of 120-139/<90 mmHg in people without diabetes with ACR < 70 mg/mmol.";
        DependencyParse dp = new DependencyParse(text);
        List<DependencyTree> parseTreeList = dp.parseSentence();
        cfGenerator = new CaseFrameGenerator(parseTreeList);

        this.text = dp.getText();

        initBuild();
    }

    private void initBuild() {
        buildEditorPane();
        buildConditionTable();
        buildActionTable();
    }

    private void buildEditorPane() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setText(text);

        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));

        add(editorScrollPane);
    }

    private void buildConditionTable() {
        JPanel conditionTablePanel = new JPanel(new GridLayout(2, 0));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Condition");
        JButton removeButton = new JButton("Remove Condition");

        conditionTable = new JTable(new ConditionTableModel(cfGenerator.getConditionList()));
        conditionTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        conditionTable.setFillsViewportHeight(true);
        conditionTable.setRowSelectionAllowed(true);
        conditionTable.setColumnSelectionAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(conditionTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addButton.addActionListener(new AddConditionButtonListener(conditionTable));
        removeButton.addActionListener(new RemoveButtonListener(conditionTable));
        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);

        conditionTablePanel.add(tableScrollPane);
        conditionTablePanel.add(bottomPanel);
        add(conditionTablePanel);
    }

    private void buildActionTable() {
        JPanel actionTablePanel = new JPanel(new GridLayout(2, 0));
        JPanel bottomPanel = new JPanel(new GridLayout(2, 0));
        JPanel actionRelatedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel modelRelatedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("New Action");
        JButton removeButton = new JButton("Remove Action");
        JButton generateModelButton = new JButton("Generate Model");

        actionTable = new JTable(new ActionTableModel(cfGenerator.getActionList()));
        actionTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        actionTable.setFillsViewportHeight(true);
        actionTable.setRowSelectionAllowed(true);
        actionTable.setColumnSelectionAllowed(false);
        actionTable.getColumnModel().getColumn(0).setMaxWidth(150);
        actionTable.getColumnModel().getColumn(4).setMaxWidth(50);

        JScrollPane tableScrollPane = new JScrollPane(actionTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addButton.addActionListener(new AddActionButtonListener(actionTable));
        removeButton.addActionListener(new RemoveButtonListener(actionTable));
        actionRelatedPanel.add(addButton);
        actionRelatedPanel.add(removeButton);
        bottomPanel.add(actionRelatedPanel);


        generateModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("First, make sure every sentences have been verified. HOW?");
                System.out.println("Next, the main issue: transform every sentences to Z3 syntax");
                System.out.println("Finally, check the satisfiability (OF/AGAINST WHAT?)");
            }
        });
        modelRelatedPanel.add(generateModelButton);
        bottomPanel.add(modelRelatedPanel);

        actionTablePanel.add(tableScrollPane);
        actionTablePanel.add(bottomPanel);
        add(actionTablePanel);
    }


    public void createAndShowGUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CaseFrameGUI newContentPane = new CaseFrameGUI();
        newContentPane.setOpaque(true);
        mainFrame.setContentPane(newContentPane);

        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CaseFrameGUI().createAndShowGUI();
            }
        });
    }

    class AddConditionButtonListener implements ActionListener {
        JTable table;
        GUITableModel tableModel;

        private AddConditionButtonListener(JTable table) {
            this.table = table;
            tableModel = (GUITableModel) table.getModel();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buildNewConditionFrame();
        }

        private void buildNewConditionFrame() {
            JFrame newConditionFrame = new JFrame("New Condition");
            JLabel conditionLabel = new JLabel("Condition:");
            JLabel modLabel = new JLabel("Mod:");
            JLabel valueLabel = new JLabel("Value:");
            JTextField conditionField = new JTextField();
            JTextField modField = new JTextField();
            JTextField valueField = new JTextField();
            JButton submitButton = new JButton("Submit");
            JButton cancelButton = new JButton("Cancel");

            JPanel newConditionPanel = new JPanel(new GridLayout(4, 2));
            newConditionPanel.add(conditionLabel);
            newConditionPanel.add(conditionField);
            newConditionPanel.add(modLabel);
            newConditionPanel.add(modField);
            newConditionPanel.add(valueLabel);
            newConditionPanel.add(valueField);
            newConditionPanel.add(submitButton);
            newConditionPanel.add(cancelButton);

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Condition condition = new Condition(conditionField.getText(),
                            modField.getText(), valueField.getText());
                    tableModel.addRow(condition);
                    newConditionFrame.dispose();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newConditionFrame.dispose();
                }
            });

            newConditionFrame.add(newConditionPanel);
            newConditionFrame.pack();
            newConditionFrame.setResizable(false);
            newConditionFrame.setLocationRelativeTo(null);
            newConditionFrame.setVisible(true);
        }
    }

    class AddActionButtonListener implements ActionListener {
        JTable table;
        GUITableModel tableModel;

        private AddActionButtonListener(JTable table) {
            this.table = table;
            tableModel = (GUITableModel) table.getModel();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buildNewActionFrame();
        }

        private void buildNewActionFrame() {
            JFrame newActionFrame = new JFrame("New Action");
            JLabel actionLabel = new JLabel("Action:");
            JLabel agentLabel = new JLabel("Agent");
            JLabel valueLabel = new JLabel("Value:");
            JLabel conditionLabel = new JLabel("Condition:");
            JTextField actionField = new JTextField();
            JTextField agentField = new JTextField();
            JTextField valueField = new JTextField();
            JTextField conditionField = new JTextField();
            JButton submitButton = new JButton("Submit");
            JButton cancelButton = new JButton("Cancel");

            JPanel newConditionPanel = new JPanel(new GridLayout(5, 2));
            newConditionPanel.add(actionLabel);
            newConditionPanel.add(actionField);
            newConditionPanel.add(agentLabel);
            newConditionPanel.add(agentField);
            newConditionPanel.add(valueLabel);
            newConditionPanel.add(valueField);
            newConditionPanel.add(conditionLabel);
            newConditionPanel.add(conditionField);
            newConditionPanel.add(submitButton);
            newConditionPanel.add(cancelButton);

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Action action = new Action(actionField.getText().toLowerCase(), agentField.getText(), valueField.getText(), conditionField.getText());
                    tableModel.addRow(action);
                    newActionFrame.dispose();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newActionFrame.dispose();
                }
            });

            newActionFrame.add(newConditionPanel);
            newActionFrame.pack();
            newActionFrame.setResizable(false);
            newActionFrame.setLocationRelativeTo(null);
            newActionFrame.setVisible(true);
        }
    }

    class RemoveButtonListener implements ActionListener {
        JTable table;
        GUITableModel tableModel;

        private RemoveButtonListener(JTable table) {
            this.table = table;
            tableModel = (GUITableModel) table.getModel();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int numRows = table.getRowCount();
            int numCols = table.getColumnCount();
            ArrayList<Integer> rowList = new ArrayList<Integer>();

            for (int i = 0; i < numRows; i++) {
                if (tableModel.getValueAt(i, numCols - 1) == Boolean.TRUE) {
                    rowList.add(i);
                }
            }
            tableModel.removeRows(rowList);
        }
    }

}