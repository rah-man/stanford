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

public class CaseFramePanel extends JPanel {
    protected JFrame mainFrame = new JFrame("Case Frame");
    protected String text;
    protected JEditorPane editorPane;
    protected JTable conditionTable;
    protected JTable actionTable;
    protected CaseFrameGenerator cfGenerator;
    protected JScrollPane editorPaneScroll, conditionScroll, actionScroll;
    protected JButton newConditionButton, removeConditionButton, newActionButton, removeActionButton, generateModelButton;
    protected JSeparator editorConditionSeparator, conditionActionSeparator;

    public CaseFramePanel() {
        super(new GridLayout(3, 0));

        String text = "Control blood pressure to targets of 120-139/<90 mmHg in people without diabetes with ACR < 70 mg/mmol.";
        DependencyParse dp = new DependencyParse(text);
        List<DependencyTree> parseTreeList = dp.parseSentence();
        cfGenerator = new CaseFrameGenerator(parseTreeList);
        this.text = dp.getText();

        initBuild();
    }

    private void initBuild() {
        editorPaneScroll = new JScrollPane();
        editorPane = new JEditorPane();
        conditionScroll = new JScrollPane();
        newConditionButton = new JButton("New Condition");
        removeConditionButton = new JButton("Remove Conditions");
        actionScroll = new JScrollPane();
        actionTable = new JTable();
        editorConditionSeparator = new JSeparator();
        newActionButton = new JButton("New Action");
        removeActionButton = new JButton("Remove Actions");
        conditionActionSeparator = new JSeparator();
        generateModelButton = new JButton("Generate Model");

        editorPane.setText(text);
        editorPane.setEditable(false);
        editorPaneScroll.setViewportView(editorPane);

        conditionTable = new JTable(new ConditionTableModel(cfGenerator.getConditionList()));
        conditionTable.getTableHeader().setReorderingAllowed(false);
        conditionTable.setFillsViewportHeight(true);
        conditionTable.setRowSelectionAllowed(true);
        conditionTable.setColumnSelectionAllowed(false);
        conditionScroll.setViewportView(conditionTable);
        newConditionButton.addActionListener(new AddConditionButtonListener(conditionTable));
        removeConditionButton.addActionListener(new RemoveButtonListener(conditionTable));

        actionTable = new JTable(new ActionTableModel(cfGenerator.getActionList()));
        actionTable.setFillsViewportHeight(true);
        actionTable.setRowSelectionAllowed(true);
        actionTable.setColumnSelectionAllowed(false);
        actionTable.getColumnModel().getColumn(4).setMaxWidth(150);
        actionTable.getTableHeader().setReorderingAllowed(false);
        actionScroll.setViewportView(actionTable);
        newActionButton.addActionListener(new AddActionButtonListener(actionTable));
        removeActionButton.addActionListener(new RemoveButtonListener(actionTable));

        generateModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("First, make sure every sentences have been verified. HOW?");
                System.out.println("Next, the main issue: transform every sentences to Z3 syntax");
                System.out.println("Finally, check the satisfiability (OF/AGAINST WHAT?)");
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(editorPaneScroll, GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
                        .addComponent(conditionScroll)
                        .addComponent(actionScroll)
                        .addComponent(editorConditionSeparator)
                        .addComponent(conditionActionSeparator)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(newConditionButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeConditionButton))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(newActionButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeActionButton))
                                        .addComponent(generateModelButton, GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(editorPaneScroll, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(conditionScroll, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(removeConditionButton)
                                        .addComponent(newConditionButton))
                                .addGap(18, 18, 18)
                                .addComponent(editorConditionSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(actionScroll, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(removeActionButton)
                                        .addComponent(newActionButton))
                                .addGap(18, 18, 18)
                                .addComponent(conditionActionSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(generateModelButton)
                                .addGap(0, 16, Short.MAX_VALUE))
        );
    }

    public void createAndShowGUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CaseFramePanel newContentPane = new CaseFramePanel();
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
                new CaseFramePanel().createAndShowGUI();
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
            JButton submitButton = new JButton("Submit");
            JButton cancelButton = new JButton("Cancel");
            JLabel conditionLabel = new JLabel("Condition:");
            JLabel modLabel = new JLabel("Mod:");
            JLabel valueLabel = new JLabel("Value:");
            JTextField conditionField = new JTextField();
            JTextField modField = new JTextField();
            JTextField valueField = new JTextField();
            JFrame newConditionFrame = new JFrame("New Condition");
            JPanel newConditionPanel = new JPanel();

            GroupLayout layout = new GroupLayout(newConditionPanel);
            newConditionPanel.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(valueLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(modLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(conditionLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(conditionField)
                                            .addComponent(modField)
                                            .addComponent(valueField)))
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(264, Short.MAX_VALUE)
                                    .addComponent(submitButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cancelButton))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(conditionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(conditionField, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(modLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(modField, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(valueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(valueField, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(submitButton)
                                            .addComponent(cancelButton)))
            );

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
            JPanel newActionPanel = new JPanel();

            GroupLayout layout = new GroupLayout(newActionPanel);
            newActionPanel.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(conditionLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                            .addComponent(valueLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(agentLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(actionLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(actionField)
                                            .addComponent(agentField)
                                            .addComponent(valueField)
                                            .addComponent(conditionField)))
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(0, 264, Short.MAX_VALUE)
                                    .addComponent(submitButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cancelButton))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(actionLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(actionField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(agentLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(agentField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(valueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(valueField, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(conditionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(conditionField, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(cancelButton)
                                            .addComponent(submitButton)))
            );

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

            newActionFrame.add(newActionPanel);
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