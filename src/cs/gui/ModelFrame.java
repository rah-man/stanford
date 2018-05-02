package cs.gui;

import cs.model.ModelGenerator;
import cs.model.Z3Model;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ModelFrame extends JFrame {
    protected JButton checkModelButton;
    protected JButton addSentenceButton;
    protected JEditorPane editorPane;
    protected JScrollPane editorScrollPane;

    protected Z3Model model;

    public ModelFrame(ModelGenerator modelGenerator) {
        model = modelGenerator.getZ3Model();
        initComponents();
    }

    private void initComponents() {
        editorScrollPane = new JScrollPane();
        editorPane = new JEditorPane();
        checkModelButton = new JButton();
        addSentenceButton = new JButton();

        editorPane.setText(model.declarationsToString());
        editorPane.setEditable(false);
        editorScrollPane.setViewportView(editorPane);
        checkModelButton.setText("Check Model");
        addSentenceButton.setText("Add Formula");
        addSentenceButton.addActionListener(e -> addSentenceButtonActionPerformed(e));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(editorScrollPane, GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(addSentenceButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkModelButton)
                                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(editorScrollPane, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(checkModelButton)
                                        .addComponent(addSentenceButton))
                                .addGap(0, 21, Short.MAX_VALUE))
        );

        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addSentenceButtonActionPerformed(ActionEvent e) {
        NewFormulaPanel newFormulaPanel = new NewFormulaPanel(this);
        newFormulaPanel.createAndShowGUI();
//        setVisible(false);
    }
}
