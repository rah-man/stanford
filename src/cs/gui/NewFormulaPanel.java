package cs.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewFormulaPanel extends JPanel {
    protected JButton cancelButton, saveButton;
    protected JEditorPane editorPane;
    protected JScrollPane editorScrollPane;
    protected JFrame mainFrame;
    protected ModelFrame parent;

    public NewFormulaPanel(JFrame parent) {
        this.parent = (ModelFrame) parent;
        initComponents();
    }

    public void initComponents() {
        editorScrollPane = new JScrollPane();
        editorPane = new JEditorPane();
        saveButton = new JButton();
        cancelButton = new JButton();
        mainFrame = new JFrame("Enter new Z3 formula");

        editorScrollPane.setViewportView(editorPane);
        saveButton.setText("Save");
        cancelButton.setText("Cancel");

        saveButton.addActionListener(e -> saveFormula(e));
        cancelButton.addActionListener(e -> closeAndShowParent());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(editorScrollPane, GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton)
                                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(editorScrollPane, GroupLayout.PREFERRED_SIZE, 372, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveButton)
                                        .addComponent(cancelButton))
                                .addGap(0, 16, Short.MAX_VALUE))
        );
    }

    public void createAndShowGUI() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setOpaque(true);
        mainFrame.setContentPane(this);

        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(parent);
    }

    private void saveFormula(ActionEvent e) {
        StringBuilder sb = new StringBuilder(parent.model.declarationsToString());
        sb.append("\n" + editorPane.getText());
        parent.editorPane.setText(sb.toString());

        closeAndShowParent();
    }

    private void closeAndShowParent() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
        parent.setVisible(true);
    }
}
