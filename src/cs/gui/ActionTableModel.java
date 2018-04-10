package cs.gui;

import cs.dep.Action;

import java.util.ArrayList;

public class ActionTableModel extends GUITableModel {
    public ActionTableModel(ArrayList<Action> actionList) {
        this.columnNames = new String[]{"Action", "Agent", "Value", "Condition", "Check"};
        this.dataList = (actionList != null) ? actionList : new ArrayList<Action>();
        fillTable(actionList);
    }

    private void fillTable(ArrayList<Action> actionList) {
        if (actionList != null) {
            for (Action action : actionList) {
                data.add(new Object[]{action.action, action.agent, action.value, action.condition, f});
            }
        }
    }

    @Override
    public void removeRows(ArrayList<Integer> rowList) {
        ArrayList<Object[]> deletedObject = new ArrayList<Object[]>();
        for (Integer row : rowList) {
            deletedObject.add(data.get(row));
        }

        for (Object[] deleted : deletedObject) {
            Action action = new Action(deleted);
            System.out.println("Trying to remove: " + action);
            int removeIndex = dataList.indexOf(action);
            dataList.remove(removeIndex);
            data.remove(removeIndex);
            fireTableDataChanged();
        }
    }

    @Override
    public void addRow(Object obj) {
        Action action = (Action) obj;
        dataList.add(action);
        data.add(new Object[]{action.action, action.agent, action.value, action.condition, f});
        fireTableDataChanged();
    }
}
