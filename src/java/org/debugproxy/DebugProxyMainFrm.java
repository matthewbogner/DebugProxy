package org.debugproxy;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Matthew Bogner
 */
public class DebugProxyMainFrm extends JFrame {
    private ResourceBundle bundle = ResourceBundle.getBundle("resources");
    private DebugProxy proxyServer;

    public DebugProxyMainFrm() {
        initComponents();

        tblRewriteRules.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (tblRewriteRules.getSelectedRow() > -1) {
                        //Row must be selected.  Enable the delete button
                        btnDeleteRule.setEnabled(true);
                    } else {
                        //Nothing selected.  Disable the delete button
                        btnDeleteRule.setEnabled(false);
                    }
                }
            }
        });

        proxyServer = new DebugProxy(((RewriteRuleTableModel)tblRewriteRules.getModel()).getAllRules(), Integer.parseInt(tfPort.getText()));
    }

    private void toggleBtnStartStopActionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {

            //Start the proxy server
            proxyServer.start(Integer.parseInt(tfPort.getText()));

            //Disable the port text field
            tfPort.setEnabled(false);

            //Change the status label
            lblServerStatus.setText("Server started");
            lblServerStatus.setForeground(Color.green);

            //Change the text of the button
            toggleBtnStartStop.setText(bundle.getString("toggleBtnStartStop.Stop.text"));

        } else {

            //Stop the proxy server
            proxyServer.stop();

            //Enable the port text field
            tfPort.setEnabled(true);

            //Change the status label
            lblServerStatus.setText("Server stopped");
            lblServerStatus.setForeground(Color.red);

            //Change the text of the button
            toggleBtnStartStop.setText(bundle.getString("toggleBtnStartStop.text"));

        }
    }

    private void btnAddRuleActionPerformed(ActionEvent e) {
        JDialog addRuleDlg = new AddRuleFrm(this, (RewriteRuleTableModel)tblRewriteRules.getModel());
        addRuleDlg.setVisible(true);
    }

    private void btnDeleteRuleActionPerformed(ActionEvent e) {
        if (tblRewriteRules.getSelectedRow() > -1) {
            int rowModelIdx = tblRewriteRules.convertRowIndexToModel(tblRewriteRules.getSelectedRow());
            RewriteRuleTableModel model = (RewriteRuleTableModel)tblRewriteRules.getModel();
            model.removeRule(rowModelIdx);
            reloadProxyServerRules();
        }
    }

    public void reloadProxyServerRules() {
        proxyServer.reloadRules(((RewriteRuleTableModel)tblRewriteRules.getModel()).getAllRules());
    }


    public static void main(String[] args) {
        final DebugProxyMainFrm f = new DebugProxyMainFrm();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });
    }

    private void debugProxyWindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("resources");
        lblTitle = new JLabel();
        lblCopyrightAttribution = new JLabel();
        scrollPaneRewriteRules = new JScrollPane();
        tblRewriteRules = new JTable(new RewriteRuleTableModel());
        toggleBtnStartStop = new JToggleButton();
        lblPort = new JLabel();
        tfPort = new JTextField();
        lblRewriteRules = new JLabel();
        btnDeleteRule = new JButton();
        btnAddRule = new JButton();
        lblServerStatus = new JLabel();

        //======== this ========
        setTitle(bundle.getString("this.title"));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                debugProxyWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();

        //---- lblTitle ----
        lblTitle.setText(bundle.getString("lblTitle.text"));
        lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        //---- lblCopyrightAttribution ----
        lblCopyrightAttribution.setText(bundle.getString("lblCopyrightAttribution.text"));
        lblCopyrightAttribution.setFont(new Font("Dialog", Font.PLAIN, 10));

        //======== scrollPaneRewriteRules ========
        {

            //---- tblRewriteRules ----
            tblRewriteRules.setAutoCreateRowSorter(true);
            tblRewriteRules.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
            tblRewriteRules.setFont(new Font("Dialog", Font.PLAIN, 10));
            tblRewriteRules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPaneRewriteRules.setViewportView(tblRewriteRules);
        }

        //---- toggleBtnStartStop ----
        toggleBtnStartStop.setText(bundle.getString("toggleBtnStartStop.text"));
        toggleBtnStartStop.setFont(new Font("Dialog", Font.PLAIN, 10));
        toggleBtnStartStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleBtnStartStopActionPerformed(e);
            }
        });

        //---- lblPort ----
        lblPort.setText(bundle.getString("lblPort.text"));
        lblPort.setFont(new Font("Dialog", Font.PLAIN, 10));

        //---- tfPort ----
        tfPort.setText(bundle.getString("tfPort.text"));

        //---- lblRewriteRules ----
        lblRewriteRules.setText(bundle.getString("lblRewriteRules.text"));
        lblRewriteRules.setFont(new Font("Dialog", Font.BOLD, 10));

        //---- btnDeleteRule ----
        btnDeleteRule.setText(bundle.getString("btnDeleteRule.text"));
        btnDeleteRule.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnDeleteRule.setEnabled(false);
        btnDeleteRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeleteRuleActionPerformed(e);
            }
        });

        //---- btnAddRule ----
        btnAddRule.setText(bundle.getString("btnAddRule.text"));
        btnAddRule.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnAddRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAddRuleActionPerformed(e);
            }
        });

        //---- lblServerStatus ----
        lblServerStatus.setText(bundle.getString("lblServerStatus.text"));
        lblServerStatus.setForeground(Color.red);
        lblServerStatus.setFont(new Font("Dialog", Font.PLAIN, 10));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(scrollPaneRewriteRules, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                        .addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                        .addComponent(lblCopyrightAttribution)
                        .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                            .addComponent(toggleBtnStartStop)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblPort)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfPort, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblServerStatus))
                        .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                            .addComponent(lblRewriteRules)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 385, Short.MAX_VALUE)
                            .addComponent(btnAddRule)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnDeleteRule)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(toggleBtnStartStop)
                        .addComponent(lblPort)
                        .addComponent(tfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblServerStatus))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblRewriteRules)
                        .addComponent(btnAddRule)
                        .addComponent(btnDeleteRule))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPaneRewriteRules, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(lblCopyrightAttribution)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel lblTitle;
    private JLabel lblCopyrightAttribution;
    private JScrollPane scrollPaneRewriteRules;
    private JTable tblRewriteRules;
    private JToggleButton toggleBtnStartStop;
    private JLabel lblPort;
    private JTextField tfPort;
    private JLabel lblRewriteRules;
    private JButton btnDeleteRule;
    private JButton btnAddRule;
    private JLabel lblServerStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
