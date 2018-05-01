package cs.gui;

import cs.model.ModelGenerator;
import cs.model.Z3Model;

import javax.swing.*;

public class ModelFrame extends JFrame {
    protected javax.swing.JButton checkModelButton;
    protected javax.swing.JButton addSentenceButton;
    protected javax.swing.JEditorPane editorPane;
    protected javax.swing.JScrollPane editorScrollPane;

    protected Z3Model model;

    public ModelFrame(ModelGenerator modelGenerator) {
        model = modelGenerator.getZ3Model();
        initComponents();
    }

    private void initComponents() {
        editorScrollPane = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();
        checkModelButton = new javax.swing.JButton();
        addSentenceButton = new javax.swing.JButton();

        editorPane.setText(model.declarationsToString());
        editorPane.setEditable(false);
        editorScrollPane.setViewportView(editorPane);
        checkModelButton.setText("Check Model");
        addSentenceButton.setText("Add Sentence");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(editorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(addSentenceButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkModelButton)
                                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(editorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(checkModelButton)
                                        .addComponent(addSentenceButton))
                                .addGap(0, 21, Short.MAX_VALUE))
        );

        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
