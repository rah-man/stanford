package cs.gui;

import cs.util.Sentences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class CaseFrameTabbedPanel extends JFrame {
    JTabbedPane caseTabbedPane;
    JButton generateModelButton;
    JPanel guiPanel;
    CaseFramePanel[] caseFrameList;

    public CaseFrameTabbedPanel(String text) {
        Scanner scanner = new Scanner(Sentences.sentences());
        scanner.useDelimiter("\\.");
        ArrayList<String> textList = new ArrayList<String>();
        while (scanner.hasNext()) {
            textList.add(scanner.next().trim() + ".");
        }

        caseFrameList = new CaseFramePanel[textList.size()];
        boolean isFinal = false;
        for (int i = 0; i < caseFrameList.length; i++) {
            caseFrameList[i] = new CaseFramePanel(textList.get(i), isFinal, i + 1);
        }

        initComponents(caseFrameList);
    }

    public CaseFrameTabbedPanel(CaseFramePanel[] caseFrameList) {
        initComponents(caseFrameList);
    }

    private void initComponents(CaseFramePanel[] caseFrameList) {
        guiPanel = new JPanel();
        caseTabbedPane = new JTabbedPane();
        generateModelButton = new JButton("Generate Model");
        generateModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                generateModelButtonActionPerformed(evt);
            }
        });

        for (int i = 0; i < caseFrameList.length; i++) {
            caseTabbedPane.addTab("Sentence " + (i + 1), caseFrameList[i]);
        }

        GroupLayout layout = new GroupLayout(guiPanel);
        guiPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(caseTabbedPane, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(generateModelButton)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseTabbedPane, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(generateModelButton)
                                .addGap(0, 12, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(guiPanel);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void generateModelButtonActionPerformed(ActionEvent evt) {
        int response = JOptionPane.showConfirmDialog(null, "All sentences have been checked?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        System.out.println("Answer: " + response);
        if (response == JOptionPane.YES_OPTION) {
            //TO DO: change the implementation to model generation
            System.out.println("Get the information from each case frame.");
            System.out.println("And generate the model. (Should have been done since ages).");
            System.out.println("And close this pretty frame.");
            setVisible(false);
            dispose();
        }
    }

    private void close() {
        WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
    }

    public static void main(String[] args) {
//        JFrame mainFrame = new JFrame("Case Frame");
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        CaseFrameTabbedPanel newContentPane = new CaseFrameTabbedPanel(Sentences.sentences());
//        newContentPane.setOpaque(true);
//        mainFrame.setContentPane(newContentPane);
//
//        mainFrame.pack();
//        mainFrame.setVisible(true);
//        mainFrame.setResizable(false);
//        mainFrame.setLocationRelativeTo(null);
        new CaseFrameTabbedPanel(Sentences.sentences());
    }
}
