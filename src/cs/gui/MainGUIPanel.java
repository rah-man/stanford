package cs.gui;

import cs.util.Sentences;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUIPanel extends JPanel {
    private JButton clearSentencesButton;
    private JScrollPane jScrollPane1;
    private JButton saveSentencesButton;
    private JEditorPane sentencesEditorPane;
    private final int CLEAR_BUTTON_GAP = 5;

    public MainGUIPanel() {
        initComponents();
    }

    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        sentencesEditorPane = new JEditorPane();
        saveSentencesButton = new JButton();
        clearSentencesButton = new JButton();

        sentencesEditorPane.setText(Sentences.sentences());
        jScrollPane1.setViewportView(sentencesEditorPane);

        saveSentencesButton.setText("Add Sentences");
        saveSentencesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveSentencesButtonActionPerformed(evt, sentencesEditorPane);
            }
        });

        clearSentencesButton.setText("Clear");
        clearSentencesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearSentencesButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveSentencesButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clearSentencesButton)
                                .addGap(CLEAR_BUTTON_GAP, CLEAR_BUTTON_GAP, CLEAR_BUTTON_GAP))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveSentencesButton)
                                        .addComponent(clearSentencesButton))
                                .addContainerGap())
        );
    }

    private void clearSentencesButtonActionPerformed(ActionEvent evt) {
        sentencesEditorPane.setText("");
    }

    private void saveSentencesButtonActionPerformed(ActionEvent evt, JEditorPane sentencesEditorPane) {
        System.out.println(sentencesEditorPane.getText());
    }

    public void createAndShowGUI() {
        JFrame mainFrame = new JFrame("Add Sentences");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainGUIPanel newContentPane = new MainGUIPanel();
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
                new MainGUIPanel().createAndShowGUI();
            }
        });
    }
}
