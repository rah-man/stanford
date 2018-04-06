package cs.gui;

import cs.dep.Condition;

import java.util.ArrayList;

public class ConditionTableModel extends GUITableModel {
    public ConditionTableModel(ArrayList<Condition> conditionList) {
        this.columnNames = new String[]{"Condition", "Mod", "Value", "Check"};
        this.dataList = conditionList;

        for (Condition condition : conditionList) {
            data.add(new Object[]{condition.condition, condition.mod, condition.value, f});
        }
    }

    @Override
    public void removeRows(ArrayList<Integer> rowList) {
        ArrayList<Object[]> deletedObject = new ArrayList<Object[]>();
        for (Integer row : rowList) {
            deletedObject.add(data.get(row));
        }

        for (Object[] deleted : deletedObject) {
            Condition condition = new Condition(deleted);
            System.out.println("Trying to remove: " + condition);
            int removeIndex = dataList.indexOf(condition);
            dataList.remove(removeIndex);
            data.remove(removeIndex);
            fireTableDataChanged();
        }
    }

    @Override
    public void addRow(Object obj) {
        Condition condition = (Condition) obj;
        dataList.add(condition);
        data.add(new Object[]{condition.condition, condition.mod, condition.value, f});
        fireTableDataChanged();
    }
}
