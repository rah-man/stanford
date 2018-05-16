package cs.gui;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import cs.model.ModelGenerator;
import cs.model.Z3Model;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Scanner;

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
        checkModelButton.addActionListener(e -> checkModel(e));
        addSentenceButton.addActionListener(e -> openNewFormulaPanel(e));

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

    private void openNewFormulaPanel(ActionEvent e) {
        NewFormulaPanel newFormulaPanel = new NewFormulaPanel(this);
        newFormulaPanel.createAndShowGUI();
//        setVisible(false);
    }

    private void checkModel(ActionEvent e) {
        String originalText = model.declarationsToString().trim();
        String newText = editorPane.getText().trim().replaceAll("\\r", "");

        String diff = StringUtils.difference(originalText, newText).trim();
        Scanner scanner = new Scanner(diff);
//        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
//            System.out.println(++i + "\t" + line);
            model.buildSentence(line);
        }

        model.checkModel();
    }

    public static void main(String[] args) {

    }
}
