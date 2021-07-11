/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author Sanele
 */
public class dialogCovers extends JDialog {

    /**
     * Creates new form dialogCovers
     * @param frmParent
     */
    public dialogCovers(frmMain frmParent) {
        super(frmParent, "Funeral Covers", ModalityType.APPLICATION_MODAL);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        txtCoverAmount.requestFocusInWindow();
    }
    
    private int intCoverID;

    private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    private final clsValidation validation = new clsValidation();
    
    private String mVerifyInput() {
        if(txtCoverAmount.getText().equals("")) {
            return "Enter cover amount.";
        } else if(txtCoverCategory.getText().equals("")) {
            return "Enter cover category";
        } else if(txtPremium.getText().equals("")) {
            return "Enter Premium amount";
        }
        return "";
    }
        
    private void mCreateCover() {
        if(mVerifyInput().equals("")) {
            if(validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()).equals("")
                        && validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()).equals("")) {
                
                if(!clsSQLMethods.mCheckIfDetailsExist("SELECT CoverAmount, Category FROM Covers WHERE CoverAmount="+txtCoverAmount.getText().trim()+
                        " AND Category ='"+txtCoverCategory.getText().trim()+"'")) {
                    
                    if(clsSQLMethods.mCreateRecord("INSERT INTO Covers (CoverAmount, Category, Premium)"
                            + " VALUES('"+txtCoverAmount.getText().trim()+"','"+txtCoverCategory.getText().trim()
                            +"','"+txtPremium.getText().trim()+"')")) {
            
                        JOptionPane.showMessageDialog(dialogCovers.this, "Cover Details Added.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Funeral Cover is already recorded", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            } else if(!validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()), 
                        "WARNING", JOptionPane.WARNING_MESSAGE);
                
            } else if(validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()),
                        "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mCoverToUpdate() {
        new clsUpdateCovers(dialogCovers.this).setVisible(true);
    }
    
    private void mSaveUpdate() {
        if(mVerifyInput().equals("")) {
            if(validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()).equals("")
                    && validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()).equals("")) {
                
                if(clsSQLMethods.mUpdateRecord("UPDATE Covers SET CoverAmount="+
                        txtCoverAmount.getText().trim()+", Category ='"+txtCoverCategory.getText().trim()
                            +"', Premium ="+txtPremium.getText().trim()+" WHERE ID ="+intCoverID)) {
                    
                    JOptionPane.showMessageDialog(this, "Cover details have been updated.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if(!validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtCoverAmount.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
                
            } else if(!validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtPremium.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
                        
        } else {
            JOptionPane.showMessageDialog(this, mVerifyInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mSetToGUICoverDetails(int ID) {
        this.intCoverID = ID;
        String[] arrCoverDetails = clsSQLMethods.mFetchRecord("SELECT CoverAmount, Category, Premium FROM Covers WHERE ID ="+ID);
        txtCoverAmount.setText(arrCoverDetails[0]);
        txtCoverCategory.setText(arrCoverDetails[1]);
        txtPremium.setText(arrCoverDetails[2]);
    }
    
    private void mClearInputText() {
        txtCoverAmount.setText("");
        txtCoverCategory.setText("");
        txtPremium.setText("");
    }
    
    private class clsUpdateCovers extends JDialog {
        private clsUpdateCovers(dialogCovers parent) {
            super(parent, "Update covers", ModalityType.APPLICATION_MODAL);
            this.setSize(400, 180); 
            this.setResizable(false);
            this.setLocationRelativeTo(null); 
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
            mCreateUpdateCovers();
        }
        
        private final JComboBox cboCovers = new JComboBox(); 
        
        private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();  
        private final clsGUIDesignMethods gui = new clsGUIDesignMethods(); 
        private final clsModelAndDataMethods modelAndDataMethods = new clsModelAndDataMethods();
        
        private void mCreateUpdateCovers() {
            JPanel jpPanel = new JPanel(new BorderLayout(0, 20));  
            jpPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 
            jpPanel = gui.mPreparePanel(jpPanel);
            jpPanel.add(gui.mCreateLabel("Select Cover to Update", new Font("Tahoma", Font.BOLD, 16)), BorderLayout.NORTH);
        
            JPanel jpCenterPart = new JPanel(new BorderLayout()); 
            jpCenterPart.add(cboCovers, BorderLayout.CENTER); 
            modelAndDataMethods.mLoadToComboBox("SELECT CoverAmount, Category FROM Covers", cboCovers);
                        
            jpPanel.add(jpCenterPart);
            
            JPanel jpLowerPart = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
            jpLowerPart = gui.mPreparePanel(jpLowerPart);
            
            jpLowerPart.add(gui.mCreateButton(100, 25, "Ok", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        String strFirst = cboCovers.getSelectedItem().toString()
                            .substring(0, cboCovers.getSelectedItem().toString()
                                .indexOf(" ")).trim(); 
                    
            
                        String strSecond = cboCovers.getSelectedItem().toString()
                            .substring(cboCovers.getSelectedItem().toString().indexOf(" "),
                                cboCovers.getSelectedItem().toString().trim().length()).trim();
                        
                        mSetToGUICoverDetails(Integer.parseInt(clsSQLMethods.mGetTextField(
                                "SELECT ID FROM Covers WHERE CoverAmount ='"+strFirst+"' AND Category ='"+strSecond+"'")));
                        
                        clsUpdateCovers.this.dispose();
                    }
                }));
                jpPanel.add(jpLowerPart, BorderLayout.SOUTH);
                
                this.add(jpPanel);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jpCovers = new javax.swing.JPanel();
        lblCoversHeading = new javax.swing.JLabel();
        lblCoverAmount = new javax.swing.JLabel();
        txtCoverAmount = new javax.swing.JTextField();
        lblCoverCategory = new javax.swing.JLabel();
        txtCoverCategory = new javax.swing.JTextField();
        lblPremium = new javax.swing.JLabel();
        txtPremium = new javax.swing.JTextField();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpCovers.setBackground(new java.awt.Color(255, 255, 255));

        lblCoversHeading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCoversHeading.setText("Burial Covers");

        lblCoverAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCoverAmount.setText("Cover Amount");

        lblCoverCategory.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCoverCategory.setText("Cover Category");

        lblPremium.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPremium.setText("Premium");

        btnCreate.setBackground(new java.awt.Color(255, 255, 255));
        btnCreate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(255, 255, 255));
        btnClear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCoversLayout = new javax.swing.GroupLayout(jpCovers);
        jpCovers.setLayout(jpCoversLayout);
        jpCoversLayout.setHorizontalGroup(
            jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoversLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCoversLayout.createSequentialGroup()
                        .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCoverCategory)
                            .addComponent(lblPremium, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCoverAmount, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCoverAmount)
                            .addComponent(txtCoverCategory)
                            .addComponent(txtPremium, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addGroup(jpCoversLayout.createSequentialGroup()
                        .addComponent(btnCreate)
                        .addGap(55, 55, 55)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
            .addGroup(jpCoversLayout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(lblCoversHeading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCoversLayout.setVerticalGroup(
            jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCoversLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(lblCoversHeading)
                .addGap(49, 49, 49)
                .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCoverCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpCoversLayout.createSequentialGroup()
                        .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCoverAmount)
                            .addComponent(txtCoverAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addComponent(lblCoverCategory)))
                .addGap(67, 67, 67)
                .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPremium)
                    .addComponent(txtPremium, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jpCoversLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnUpdate)
                    .addComponent(btnClear))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCovers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCovers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        mClearInputText();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        mCreateCover();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
       if(btnUpdate.getText().equals("Update")) {
            
            mCoverToUpdate();
            if(mVerifyInput().equals("")) {
                btnUpdate.setText("Save");
            }
                        
        } else if(btnUpdate.getText().equals("Save")) {
            
            mSaveUpdate();
            btnUpdate.setText("Update");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dialogCovers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialogCovers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialogCovers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialogCovers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialogCovers dialog = new dialogCovers((frmMain) new javax.swing.JFrame());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jpCovers;
    private javax.swing.JLabel lblCoverAmount;
    private javax.swing.JLabel lblCoverCategory;
    private javax.swing.JLabel lblCoversHeading;
    private javax.swing.JLabel lblPremium;
    private javax.swing.JTextField txtCoverAmount;
    private javax.swing.JTextField txtCoverCategory;
    private javax.swing.JTextField txtPremium;
    // End of variables declaration//GEN-END:variables
}
