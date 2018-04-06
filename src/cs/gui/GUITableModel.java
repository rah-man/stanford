package cs.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public abstract class GUITableModel extends AbstractTableModel {
    protected String[] columnNames;
    protected ArrayList<Object[]> data = new ArrayList<Object[]>();
    protected Boolean f = false;
    protected ArrayList dataList = new ArrayList();

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
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }

    /**
     * Make it editable only if it's the last column
     *
     * @param row
     * @param col
     * @return true if it's the last column
     */
    public boolean isCellEditable(int row, int col) {
        return (col == columnNames.length - 1) ? true : false;
    }

    public abstract void removeRows(ArrayList<Integer> rowList);
    public abstract void addRow(Object obj);
}