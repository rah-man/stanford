package cs.gui;

import javax.swing.*;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import cs.dep.*;

public class CaseFrame extends JPanel {
    protected boolean DEBUG = false;
    protected JEditorPane editorPane;
    protected JTable conditionTable;

    public CaseFrame() {
        super(new GridLayout(3, 0));
        buildEditorPane();
        buildConditionTable();
        buildActionTable();
    }

    private void buildEditorPane() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setText("Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and diabetes and an ACR of 3 mg/mmol or more (ACR category A2 or A3).");

        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));

        add(editorScrollPane);
    }

    private void buildConditionTable() {
        JPanel conditionTablePanel = new JPanel(new GridLayout(2, 0));
        conditionTable = new JTable(new ConditionTableModel());
        conditionTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        conditionTable.setFillsViewportHeight(true);
        conditionTable.getColumnModel().getSelectionModel().
                addListSelectionListener(new CaseFrame.ColumnListener());
        conditionTable.setRowSelectionAllowed(true);
        conditionTable.setColumnSelectionAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(conditionTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new RemoveButtonListener(conditionTable));

        conditionTablePanel.add(tableScrollPane);
        conditionTablePanel.add(removeButton);
        add(conditionTablePanel);
    }

    private void buildActionTable() {
        String[] columnNames = {"Action",
                "Value"};

        Object[][] data = {
                {"offer", "low-cost renin-angiotensin-aldosterone system antagonist"}
        };

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setMaxWidth(150);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(tableScrollPane);
    }

    class RemoveButtonListener implements ActionListener{
        JTable table;
        TableModel model;
        ConditionTableModel cTableModel;
        private RemoveButtonListener(JTable table){
            this.table = table;
            model = this.table.getModel();
            cTableModel = (ConditionTableModel) model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int numRows = table.getRowCount();
            int numCols = table.getColumnCount();
            ArrayList<Integer> rowList = new ArrayList<Integer>();

            for (int i = 0; i < numRows; i++) {
                if(model.getValueAt(i, numCols-1) == Boolean.TRUE){
                    rowList.add(i);
                }
            }
            cTableModel.removeRows(rowList);
        }
    }

    class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) event.getSource();
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        System.out.println("Selected index: " + i);
                    }
                }
            }
        }
    }

    private void outputSelection() {
        System.out.print((String.format("Lead: %d, %d. ",
                conditionTable.getSelectionModel().getLeadSelectionIndex(),
                conditionTable.getColumnModel().getSelectionModel().
                        getLeadSelectionIndex())));
        for (int c : conditionTable.getSelectedRows()) {
            System.out.print(String.format(" %d", c));
        }
        for (int c : conditionTable.getSelectedColumns()) {
            System.out.print(String.format(" %d", c));
        }
        System.out.println(".");
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("How now brown cow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CaseFrame newContentPane = new CaseFrame();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class ConditionTableModel extends AbstractTableModel {
    protected ArrayList<Condition> conditionList = new ArrayList<Condition>();
    protected String[] columnNames = {"Condition", "Mod", "Value", "Check"};
    protected ArrayList<Object[]> data = new ArrayList<Object[]>();

    protected static final int CONDITION = 0;
    protected static final int MOD = 1;
    protected static final int VALUE = 2;

    public ConditionTableModel(){
        Object[][] obj = {
                {"CKD", "=", "true", new Boolean(false)},
                {"diabetes", "=", "true", new Boolean(false)},
                {"ACR", "<=", "70 mg/mmol", new Boolean(false)}
        };

        for(Object[] o : obj){
            Condition condition = new Condition(o);
            conditionList.add(condition);
            data.add(o);
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }

    public void removeRows(ArrayList<Integer> rowList){
        ArrayList<Object[]> deletedObject = new ArrayList<Object[]>();
        for(Integer row : rowList){
            deletedObject.add(data.get(row));
        }

        for(Object[] deleted : deletedObject){
            Condition condition = new Condition(deleted);
            System.out.println("Trying to remove: " + condition);
            int conditionIndex = conditionList.indexOf(condition);
            conditionList.remove(conditionIndex);
            data.remove(conditionIndex);
            fireTableDataChanged();
        }
    }

}