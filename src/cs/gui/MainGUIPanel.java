package cs.gui;

import cs.util.Sentences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainGUIPanel extends JFrame {
    private JButton clearSentencesButton;
    private JScrollPane jScrollPane1;
    private JButton saveSentencesButton;
    private JEditorPane sentencesEditorPane;
    private JPanel guiPanel;

    public MainGUIPanel() {
        setTitle("Add Sentences");
        initComponents();
    }

    private void initComponents() {
        guiPanel = new JPanel();
        jScrollPane1 = new JScrollPane();
        sentencesEditorPane = new JEditorPane();
        saveSentencesButton = new JButton();
        clearSentencesButton = new JButton();

        sentencesEditorPane.setText(Sentences.sentences());
        jScrollPane1.setViewportView(sentencesEditorPane);

        saveSentencesButton.setText("Save Sentences");
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

        GroupLayout layout = new GroupLayout(guiPanel);
        guiPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveSentencesButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clearSentencesButton)
                                .addGap(5, 5, 5))
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

        setContentPane(guiPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void clearSentencesButtonActionPerformed(ActionEvent evt) {
        sentencesEditorPane.setText("");
    }

    private void saveSentencesButtonActionPerformed(ActionEvent evt, JEditorPane sentencesEditorPane) {
        System.out.println(sentencesEditorPane.getText());
        setVisible(false);
        dispose();
        new CaseFrameTabbedPanel(sentencesEditorPane.getText().trim());
    }

    private void close() {
        WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
    }

    public void createAndShowGUI() {
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        MainGUIPanel newContentPane = new MainGUIPanel();
//        newContentPane.setOpaque(true);
//        mainFrame.setContentPane(newContentPane);
//
//        mainFrame.pack();
//        mainFrame.setVisible(true);
//        mainFrame.setResizable(false);
//        mainFrame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUIPanel().setVisible(true);
            }
        });
    }
}
