/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import javax.swing.*;

/**
 *
 * @author Matilda
 */
public class dialogBeneficiaries extends javax.swing.JDialog {

    /**
     * Creates new form dialogBeneficiaries
     * @param frmParent
     */
    public dialogBeneficiaries(frmMain frmParent) {
        super(frmParent, "Beneficiaries", ModalityType.APPLICATION_MODAL);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.frmMn = frmParent;
        if(clsSQLMethods.mGetTextField("SELECT ROLE FROM Users_Login WHERE ID="+new frmLogin().mGetUserAccId()).equals("Administrator")) {
            clsModelAndDataMethods.mLoadToComboBox("SELECT ID_NUM FROM Principal_Members", cboPrincipalMembers);
        } else {
            clsModelAndDataMethods.mLoadToComboBox("SELECT ID_NUM FROM Principal_Members WHERE Acc_ID ="+frmLogin.mGetUserAccId(), cboPrincipalMembers);
        }
        txtBeneficiaryName.requestFocusInWindow();
    }

    private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    private final clsModelAndDataMethods clsModelAndDataMethods = new clsModelAndDataMethods();
    private final clsValidation validation = new clsValidation();
    
    private final frmLogin frmLogin = new frmLogin();
    private final frmMain frmMn;
    
    private Long lngBeneficiaryID; 
    
    private String[] mGetDetailsFromBeneficiariesForm() {
        return new String[] {
          txtBeneficiaryName.getText().trim(),
            txtBeneficiarySurname.getText().trim(),
            txtBeneficiaryID.getText().trim(),
            cboPrincipalMembers.getSelectedItem().toString(),
            txtRelationship.getText().trim()
        };
    }
    
    public void mSetDetailsForBeneficiaryUpdate(Long lngID) {
        
        this.lngBeneficiaryID = lngID; 
        
        String[] arrBeneficiaryDetails = clsSQLMethods.mFetchRecord(
                "SELECT FName, LName, ID_Num, PM_ID_Num, Relationship FROM Beneficiaries WHERE ID_Num ="+lngID);
        
        txtBeneficiaryName.setText(arrBeneficiaryDetails[0]);
        txtBeneficiarySurname.setText(arrBeneficiaryDetails[1]);
        txtBeneficiaryID.setText(arrBeneficiaryDetails[2]);
        cboPrincipalMembers.setSelectedItem(arrBeneficiaryDetails[3]);
        txtRelationship.setText(arrBeneficiaryDetails[4]);
    }
    
    private String mVerifyBeneficiaryFormInput() {
        if(txtBeneficiaryName.getText().equals("")) {
            return "Provide the name of the beneficiary";
            
        } else if(txtBeneficiarySurname.getText().equals("")) {
            return "Provide the surname of the beneficiary";
            
        } else if(txtBeneficiaryID.getText().equals("")) {
            return "Provide the ID number of the beneficiary";
            
        } else if(txtBeneficiaryID.getText().length() != 13) {
            return "South African ID number is 13 digits";
            
        }else if(txtRelationship.getText().equals("")) {
            return "Provide the relationship status of the beneficiary with the principal";
            
        }
        return "";
    }
    
    private void mRegisterBeneficiary() {
        if(mVerifyBeneficiaryFormInput().equals("")) {
            if(validation.mCheckIfFieldIsOnlyDigits(mGetDetailsFromBeneficiariesForm()[2]).equals("")) {
                if(clsSQLMethods.mCreateRecord("INSERT INTO Beneficiaries (FName, LName, ID_Num, PM_ID_Num, Relationship)"
                        + "VALUES('"+mGetDetailsFromBeneficiariesForm()[0]+"','"+mGetDetailsFromBeneficiariesForm()[1]+"','"+
                        mGetDetailsFromBeneficiariesForm()[2]+"','"+mGetDetailsFromBeneficiariesForm()[3]
                        +"','"+mGetDetailsFromBeneficiariesForm()[4]+"')")) {
                    
                    JOptionPane.showMessageDialog(this, "Beneficiary has been registered.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(mGetDetailsFromBeneficiariesForm()[2]), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyBeneficiaryFormInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mBeneficiaryToUpdate() {
        clsPolicyMemberOperations memberOperations = new clsPolicyMemberOperations(frmMn, "Update Beneficiary");
        memberOperations.mActiveInstanceOfDialogBeneficiaries(this);
        memberOperations.setVisible(true);
    }
    
    private void mUpdateBeneficiary() {
        if(mVerifyBeneficiaryFormInput().equals("")) {
            if(validation.mCheckIfFieldIsOnlyDigits(mGetDetailsFromBeneficiariesForm()[2]).equals("")) {
                if(clsSQLMethods.mUpdateRecord("UPDATE Beneficiaries SET FName='"+mGetDetailsFromBeneficiariesForm()[0]
                        +"', LName='"+mGetDetailsFromBeneficiariesForm()[1]+"', ID_Num ='"+mGetDetailsFromBeneficiariesForm()[2]+
                    "', PM_ID_Num ='"+mGetDetailsFromBeneficiariesForm()[3]+"', Relationship ='"+mGetDetailsFromBeneficiariesForm()[4]+
                        "' WHERE ID_Num="+lngBeneficiaryID)) {
                    
                    JOptionPane.showMessageDialog(this, "Beneficiary details have been updated", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(mGetDetailsFromBeneficiariesForm()[2]), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyBeneficiaryFormInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mDeleteBeneficiary() {
        new clsPolicyMemberOperations(frmMn, "Beneficiary Delete").setVisible(true);
    }
    
    private void mClearBeneficiariesForm() {
        txtBeneficiaryName.setText("");
        txtBeneficiarySurname.setText("");
        txtBeneficiaryID.setText("");
        txtRelationship.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpBeneficiaries = new javax.swing.JPanel();
        RegBeneficiariesHeading = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblSurname = new javax.swing.JLabel();
        lblBeneficiaryID = new javax.swing.JLabel();
        lblPrincipalMember = new javax.swing.JLabel();
        txtBeneficiaryName = new javax.swing.JTextField();
        txtBeneficiarySurname = new javax.swing.JTextField();
        txtBeneficiaryID = new javax.swing.JTextField();
        lblRelationship = new javax.swing.JLabel();
        cboPrincipalMembers = new javax.swing.JComboBox<>();
        txtRelationship = new javax.swing.JTextField();
        jpBeneficiaryCommands = new javax.swing.JPanel();
        btnRegisterBeneficiary = new javax.swing.JButton();
        btnUpdateBeneficiary = new javax.swing.JButton();
        btnDeleteBeneficiary = new javax.swing.JButton();
        ClearBeneficiaryData = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpBeneficiaries.setBackground(new java.awt.Color(255, 255, 255));

        RegBeneficiariesHeading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RegBeneficiariesHeading.setText("Register Beneficiaries");

        lblName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblName.setText("Name");

        lblSurname.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblSurname.setText("Surname");

        lblBeneficiaryID.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBeneficiaryID.setText("ID Number");
        lblBeneficiaryID.setToolTipText("");

        lblPrincipalMember.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPrincipalMember.setText("Principal Member");

        txtBeneficiarySurname.setToolTipText("");

        lblRelationship.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblRelationship.setText("Relationship");

        jpBeneficiaryCommands.setBackground(new java.awt.Color(255, 255, 255));
        jpBeneficiaryCommands.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnRegisterBeneficiary.setBackground(new java.awt.Color(255, 255, 255));
        btnRegisterBeneficiary.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRegisterBeneficiary.setText("Register");
        btnRegisterBeneficiary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterBeneficiaryActionPerformed(evt);
            }
        });

        btnUpdateBeneficiary.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateBeneficiary.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdateBeneficiary.setText("Update");
        btnUpdateBeneficiary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateBeneficiaryActionPerformed(evt);
            }
        });

        btnDeleteBeneficiary.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteBeneficiary.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeleteBeneficiary.setText("Delete");
        btnDeleteBeneficiary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBeneficiaryActionPerformed(evt);
            }
        });

        ClearBeneficiaryData.setBackground(new java.awt.Color(255, 255, 255));
        ClearBeneficiaryData.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ClearBeneficiaryData.setText("Clear");
        ClearBeneficiaryData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearBeneficiaryDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBeneficiaryCommandsLayout = new javax.swing.GroupLayout(jpBeneficiaryCommands);
        jpBeneficiaryCommands.setLayout(jpBeneficiaryCommandsLayout);
        jpBeneficiaryCommandsLayout.setHorizontalGroup(
            jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBeneficiaryCommandsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeleteBeneficiary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegisterBeneficiary, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdateBeneficiary, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(ClearBeneficiaryData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jpBeneficiaryCommandsLayout.setVerticalGroup(
            jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBeneficiaryCommandsLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegisterBeneficiary)
                    .addComponent(btnUpdateBeneficiary))
                .addGap(20, 20, 20)
                .addGroup(jpBeneficiaryCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteBeneficiary)
                    .addComponent(ClearBeneficiaryData))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpBeneficiariesLayout = new javax.swing.GroupLayout(jpBeneficiaries);
        jpBeneficiaries.setLayout(jpBeneficiariesLayout);
        jpBeneficiariesLayout.setHorizontalGroup(
            jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBeneficiariesLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpBeneficiaryCommands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpBeneficiariesLayout.createSequentialGroup()
                        .addGroup(jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblPrincipalMember, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblBeneficiaryID)
                            .addComponent(lblSurname)
                            .addComponent(lblRelationship))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBeneficiarySurname, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBeneficiaryID, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboPrincipalMembers, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtBeneficiaryName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtRelationship, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
            .addGroup(jpBeneficiariesLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(RegBeneficiariesHeading)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jpBeneficiariesLayout.setVerticalGroup(
            jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBeneficiariesLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(RegBeneficiariesHeading)
                .addGap(29, 29, 29)
                .addGroup(jpBeneficiariesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpBeneficiariesLayout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(28, 28, 28)
                        .addComponent(lblSurname)
                        .addGap(31, 31, 31)
                        .addComponent(lblBeneficiaryID)
                        .addGap(31, 31, 31)
                        .addComponent(lblPrincipalMember)
                        .addGap(31, 31, 31)
                        .addComponent(lblRelationship))
                    .addGroup(jpBeneficiariesLayout.createSequentialGroup()
                        .addComponent(txtBeneficiaryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtBeneficiarySurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtBeneficiaryID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(cboPrincipalMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtRelationship, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jpBeneficiaryCommands, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpBeneficiaries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpBeneficiaries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterBeneficiaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterBeneficiaryActionPerformed
        mRegisterBeneficiary();
    }//GEN-LAST:event_btnRegisterBeneficiaryActionPerformed

    private void btnUpdateBeneficiaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateBeneficiaryActionPerformed
        if(btnUpdateBeneficiary.getText().equals("Update")) {
            mBeneficiaryToUpdate();
            btnUpdateBeneficiary.setText("Save..");
            
        } else {
            mUpdateBeneficiary();
            btnUpdateBeneficiary.setText("Update");
        }
    }//GEN-LAST:event_btnUpdateBeneficiaryActionPerformed

    private void btnDeleteBeneficiaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBeneficiaryActionPerformed
        mDeleteBeneficiary();
    }//GEN-LAST:event_btnDeleteBeneficiaryActionPerformed

    private void ClearBeneficiaryDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearBeneficiaryDataActionPerformed
        mClearBeneficiariesForm();
    }//GEN-LAST:event_ClearBeneficiaryDataActionPerformed

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
            java.util.logging.Logger.getLogger(dialogBeneficiaries.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialogBeneficiaries.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialogBeneficiaries.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialogBeneficiaries.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dialogBeneficiaries dialog = new dialogBeneficiaries((frmMain) new javax.swing.JFrame());
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
    private javax.swing.JButton ClearBeneficiaryData;
    private javax.swing.JLabel RegBeneficiariesHeading;
    private javax.swing.JButton btnDeleteBeneficiary;
    private javax.swing.JButton btnRegisterBeneficiary;
    private javax.swing.JButton btnUpdateBeneficiary;
    private javax.swing.JComboBox<String> cboPrincipalMembers;
    private javax.swing.JPanel jpBeneficiaries;
    private javax.swing.JPanel jpBeneficiaryCommands;
    private javax.swing.JLabel lblBeneficiaryID;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrincipalMember;
    private javax.swing.JLabel lblRelationship;
    private javax.swing.JLabel lblSurname;
    private javax.swing.JTextField txtBeneficiaryID;
    private javax.swing.JTextField txtBeneficiaryName;
    private javax.swing.JTextField txtBeneficiarySurname;
    private javax.swing.JTextField txtRelationship;
    // End of variables declaration//GEN-END:variables
}
