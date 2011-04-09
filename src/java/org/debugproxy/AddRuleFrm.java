package org.debugproxy;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * @author Matthew Bogner
 */
public class AddRuleFrm extends JDialog {
    private RewriteRuleTableModel _rewriteRuleTableModel;
    private String selectedFileName = null;

    public AddRuleFrm(Frame owner, RewriteRuleTableModel rewriteRuleTableModel) {
        super(owner);
        initComponents();
        _rewriteRuleTableModel = rewriteRuleTableModel;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        //In order for the Save button to be enabled, the URL and filename fields must have some valid content.
        //Just proceed with saving.

        _rewriteRuleTableModel.addRule(new RewriteRule(tfUrl.getText(), selectedFileName));
        ((DebugProxyMainFrm)this.getParent()).reloadProxyServerRules();
        this.setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    private void setSaveButtonState(){
        if (tfUrl.getText() != null && selectedFileName != null && !"".equals(tfUrl.getText().trim()) && !"".equals(selectedFileName.trim())) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }

    private void tfUrlCaretUpdate(CaretEvent e) {
        setSaveButtonState();
    }

    private void fileChooserPropertyChange(PropertyChangeEvent e) {
        // when dialog closes, update property editor value
        File file = fileChooser.getSelectedFile();
        selectedFileName = (file != null && file.isFile()) ? file.getAbsolutePath() : null;

        setSaveButtonState();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.bazaarvoice.debugproxy.resources");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        lblUrl = new JLabel();
        lblFile = new JLabel();
        fileChooser = new JFileChooser();
        tfUrl = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle(bundle.getString("this.title"));
        setMinimumSize(new Dimension(300, 150));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- lblUrl ----
                lblUrl.setText(bundle.getString("lblUrl.text"));
                lblUrl.setFont(new Font("Dialog", Font.PLAIN, 10));

                //---- lblFile ----
                lblFile.setText(bundle.getString("lblFile.text"));
                lblFile.setFont(new Font("Dialog", Font.PLAIN, 10));

                //---- fileChooser ----
                fileChooser.setFont(new Font("Dialog", Font.PLAIN, 10));
                fileChooser.setControlButtonsAreShown(false);
                fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        fileChooserPropertyChange(e);
                    }
                });

                //---- tfUrl ----
                tfUrl.addCaretListener(new CaretListener() {
                    @Override
                    public void caretUpdate(CaretEvent e) {
                        tfUrlCaretUpdate(e);
                    }
                });

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(lblUrl)
                                .addComponent(lblFile))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(fileChooser, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                                .addComponent(tfUrl, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblUrl)
                                .addComponent(tfUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblFile)
                                .addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText(bundle.getString("okButton.text"));
                okButton.setEnabled(false);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("cancelButton.text"));
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel lblUrl;
    private JLabel lblFile;
    private JFileChooser fileChooser;
    private JTextField tfUrl;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
