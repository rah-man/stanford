package cs.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CaseFrameTabbedPanel extends JPanel {
    JTabbedPane caseTabbedPane;

    public CaseFrameTabbedPanel(CaseFramePanel[] caseFrameList) {
        initComponents(caseFrameList);
    }

    private void initComponents(CaseFramePanel[] caseFrameList) {
        caseTabbedPane = new JTabbedPane();
        for (int i = 0; i < caseFrameList.length; i++) {
            caseTabbedPane.addTab("Sentence " + (i + 1), caseFrameList[i]);
        }

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseTabbedPane, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseTabbedPane, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE))
        );
    }

    public static void main(String[] args) {
        String text = "Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and diabetes and an ACR of 3 mg/mmol or more (ACR category A2 or A3). " +
                "Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and hypertension and an ACR of 30 mg/mmol or more (ACR category A3). " +
                "Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and an ACR of 70 mg/mmol or more (irrespective of hypertension or cardiovascular disease). " +
                "Control blood pressure to targets of 120-139/<90 mmHg in people without diabetes with ACR < 70 mg/mmol. " +
                "Control blood pressure to targets of 120-129/<80 mmHg in people with diabetes or with ACR >= 70 mg/mmol.";
        Scanner scanner = new Scanner(text);
        scanner.useDelimiter("\\.");

        ArrayList<String> textList = new ArrayList<String>();
        while (scanner.hasNext()) {
            textList.add(scanner.next().trim() + ".");
        }

        CaseFramePanel[] caseFrameList = new CaseFramePanel[textList.size()];
        boolean isFinal = false;
        for (int i = 0; i < caseFrameList.length; i++) {
            if (i == caseFrameList.length - 1) {
                isFinal = true;
            }
            caseFrameList[i] = new CaseFramePanel(textList.get(i), isFinal);
        }

        JFrame mainFrame = new JFrame("Case Frame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CaseFrameTabbedPanel newContentPane = new CaseFrameTabbedPanel(caseFrameList);
        newContentPane.setOpaque(true);
        mainFrame.setContentPane(newContentPane);

        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
    }
}
