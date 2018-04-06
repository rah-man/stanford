package cs.gui;

import cs.dep.CaseFrameGenerator;
import cs.dep.DependencyParse;
import cs.dep.DependencyTree;

import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableModel;

public class CaseFrameGUI extends JPanel {
    protected String text;
    protected JEditorPane editorPane;
    protected JTable conditionTable;
    protected JTable actionTable;
    protected CaseFrameGenerator cfGenerator;

    public CaseFrameGUI() {
        super(new GridLayout(3, 0));

        DependencyParse dp = new DependencyParse();
        List<DependencyTree> parseTreeList = dp.parseSentence();
        cfGenerator = new CaseFrameGenerator(parseTreeList);

        text = dp.getText();

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

        conditionTable = new JTable(new ConditionTableModel(cfGenerator.getConditionList()));
        conditionTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        conditionTable.setFillsViewportHeight(true);
        conditionTable.setRowSelectionAllowed(true);
        conditionTable.setColumnSelectionAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(conditionTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JButton removeButton = new JButton("Remove Condition");
        removeButton.addActionListener(new RemoveButtonListener(conditionTable));
        bottomPanel.add(removeButton);

        conditionTablePanel.add(tableScrollPane);
        conditionTablePanel.add(bottomPanel);
        add(conditionTablePanel);
    }

    private void buildActionTable() {
        JPanel actionTablePanel = new JPanel(new GridLayout(2, 0));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        actionTable = new JTable(new ActionTableModel(cfGenerator.getActionList()));
        actionTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        actionTable.setFillsViewportHeight(true);
        actionTable.setRowSelectionAllowed(true);
        actionTable.setColumnSelectionAllowed(false);
        actionTable.getColumnModel().getColumn(0).setMaxWidth(150);
        actionTable.getColumnModel().getColumn(2).setMaxWidth(50);

        JScrollPane tableScrollPane = new JScrollPane(actionTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JButton removeButton = new JButton("Remove Action");
        removeButton.addActionListener(new RemoveButtonListener(actionTable));
        bottomPanel.add(removeButton);

        actionTablePanel.add(tableScrollPane);
        actionTablePanel.add(bottomPanel);
        add(actionTablePanel);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Case Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CaseFrameGUI newContentPane = new CaseFrameGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    class RemoveButtonListener implements ActionListener {
        JTable table;
        TableModel model;
        GUITableModel cTableModel;

        private RemoveButtonListener(JTable table) {
            this.table = table;
            model = this.table.getModel();
            cTableModel = (GUITableModel) model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int numRows = table.getRowCount();
            int numCols = table.getColumnCount();
            ArrayList<Integer> rowList = new ArrayList<Integer>();

            for (int i = 0; i < numRows; i++) {
                if (model.getValueAt(i, numCols - 1) == Boolean.TRUE) {
                    rowList.add(i);
                }
            }
            cTableModel.removeRows(rowList);
        }
    }

}