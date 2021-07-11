/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sanele
 */
public class frmMain extends javax.swing.JFrame {

    /**
     * Creates new form frmMain
     * @param strRole
     */
    public frmMain(String strRole) {
        initComponents();
        this.setTitle("Ray Of Hope Burial Society");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        switch(strRole) {
            case "General User":
                tblPrincipalMbrs = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, DOB, Email, Cover_ID FROM Principal_Members WHERE ID_NUM ="+
                               clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE Acc_ID ="+frmLogin.mGetUserAccId()),
                        tblPrincipalMbrs, dmPrincipalMembers);
                
                tblMultiPurpose = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, Relationship, PM_ID_Num FROM Beneficiaries WHERE PM_ID_Num ="+
                                clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE Acc_ID ="+frmLogin.mGetUserAccId()),
                        tblMultiPurpose, dmMultiPurpose);
                
                btnBeneficiaries.setEnabled(false);
                txtSearch.setVisible(false);
                btnSearch.setVisible(false);
                btnClearSearch.setVisible(false);
                break;
                
            case "Administrator":
                
                tblPrincipalMbrs = modelAndDatabaseMethods.mTable("SELECT ID_Num, FName, LName, DOB, Email, Cover_ID FROM Principal_Members",
                        tblPrincipalMbrs, dmPrincipalMembers);
                
                tblMultiPurpose = modelAndDatabaseMethods.mTable("SELECT ID, CoverAmount, Category, Premium FROM Covers",
                        tblMultiPurpose, dmMultiPurpose);
                
                btnCovers.setEnabled(false);
                btnClearSearch.setEnabled(false);
                break;
        }
        txtSearch.requestFocusInWindow();
    }
    
    private DefaultTableModel dmPrincipalMembers = new DefaultTableModel();
    private DefaultTableModel dmMultiPurpose = new DefaultTableModel();
    
    private String strRole;
    
    clsModelAndDataMethods modelAndDatabaseMethods = new clsModelAndDataMethods();
    clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    frmLogin frmLogin = new frmLogin();
    
    public void mSetFilteredModel(DefaultTableModel dmModel) {
        dmMultiPurpose = dmModel;
    }
    
    public void mUserAccessControl(String strUserRole) {
        
        strRole = strUserRole;
        
        switch(strUserRole) {
            case "General User":
                mnuFile.setVisible(true);
                mnuCovers.setVisible(false);
                mnuRegisterPolicy.setVisible(true);
                mnuItemCreateAccount.setVisible(false);
                mnuItemUpdateLoginDetails.setVisible(true);
                mnuItemDeleteLoginDetails.setVisible(true);
                break;
                
            case "Administrator":
                mnuFile.setVisible(true);
                mnuCovers.setVisible(true);
                mnuRegisterPolicy.setVisible(true);
                mnuItemCreateAccount.setVisible(false);
                mnuItemUpdateLoginDetails.setVisible(true);
                mnuItemDeleteLoginDetails.setVisible(true);
                break;
                
            default:
                mnuFile.setEnabled(true);
                mnuCovers.setEnabled(false);
                mnuRegisterPolicy.setEnabled(false);
                mnuItemCreateAccount.setEnabled(true);
                mnuItemUpdateLoginDetails.setEnabled(false);
                mnuItemDeleteLoginDetails.setEnabled(false);
                break;
        }
    }
    
    private void mSearch() {
        if(!txtSearch.getText().equals("")) {
            if(txtSearch.getText().contains(" ")) {
                
                String strFName = txtSearch.getText().substring(0,
                        txtSearch.getText().indexOf(" ")).trim();
                
                String strLName = txtSearch.getText().substring(txtSearch.getText().indexOf(" "), 
                        txtSearch.getText().length()).trim();
                
                dmPrincipalMembers = new DefaultTableModel();
                tblPrincipalMbrs = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, DOB, Email, Cover_ID FROM Principal_Members WHERE FName LIKE '%"+strFName+"%' AND LName LIKE '%"+strLName+"%'",
                        tblPrincipalMbrs, dmPrincipalMembers);
                
            } else {
                dmPrincipalMembers = new DefaultTableModel();
                tblPrincipalMbrs = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, DOB, Email, Cover_ID FROM Principal_Members WHERE FName LIKE '%"+txtSearch.getText().trim()+"%' OR LName LIKE '%"+txtSearch.getText().trim()+"%'",
                        tblPrincipalMbrs, dmPrincipalMembers);
            }
            btnSearch.setEnabled(false);
            btnClearSearch.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Enter word to search.", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mClearSearch() {
        dmPrincipalMembers = new DefaultTableModel();
        tblPrincipalMbrs = modelAndDatabaseMethods.mTable("SELECT ID_Num, FName, LName, DOB, Email, Cover_ID FROM Principal_Members",
                        tblPrincipalMbrs, dmPrincipalMembers);
        btnClearSearch.setEnabled(false);
        btnSearch.setEnabled(true);
        txtSearch.setText("");
    }
    
    private void mCoversData() {
        dmMultiPurpose = new DefaultTableModel();
        tblMultiPurpose = modelAndDatabaseMethods.mTable("SELECT ID, CoverAmount, Category, Premium FROM Covers",
                        tblMultiPurpose, dmMultiPurpose);
        
        btnBeneficiaries.setEnabled(true);
        btnCovers.setEnabled(false);
    }
    
    private void mBeneficiariesData() {
        dmMultiPurpose = new DefaultTableModel();
        
        if(strRole.equals("General User")) {
            tblMultiPurpose = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, Relationship, PM_ID_Num FROM Beneficiaries WHERE PM_ID_Num ="+
                                clsSQLMethods.mGetNumericField("SELECT ID_Num FROM Principal_Members WHERE ACC_ID ="+frmLogin.mGetUserAccId()),
                        tblMultiPurpose, dmMultiPurpose);
            
        } else if(strRole.equals("Administrator")) {
            
            tblMultiPurpose = modelAndDatabaseMethods.mTable(
                        "SELECT ID_Num, FName, LName, Relationship, PM_ID_Num FROM Beneficiaries",
                        tblMultiPurpose, dmMultiPurpose);
        }
        btnCovers.setEnabled(true);
        btnBeneficiaries.setEnabled(false);
    }
    
    private void mSignUp() {
        new clsSignUp(this, "Sign Up").setVisible(true);
    }
    
    private void mLogout() {
        if(strRole.equals("General User")) {
            frmLogin.mSetPrincipalMemberID(0L);
        }
        frmLogin.mSetUserAccId(0);
        this.dispose();
        frmLogin.setVisible(true);
    }
    
    private void mExit() {
        System.exit(0);
    }
    
    private void mManageCovers() {
        new dialogCovers(frmMain.this).setVisible(true);
    }
    
    private void mRegisterAndManagePolicy() {
        new dialogRegisterPolicy(frmMain.this).setVisible(true);
    }
    
    private void mUpdateLoginAccount () {
        new clsSignUp(frmMain.this, "Update").setVisible(true);
    }
    
    private void mDeleteLoginAccount() {
        if(clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId()).equals("Administrator")) {
            
            if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete your login account?", "Delete Account", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                
                if(clsSQLMethods.mDeleteRecord("DELETE FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId())) {
                    
                    JOptionPane.showMessageDialog(this, "Login account details have been deleted.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    
                }
            }
        } else {
            if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete your login account? \nEverything, including your policy details will be deleted.", "Delete Account", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                
                if(clsSQLMethods.mCheckIfDetailsExist("SELECT PM_ID_NUM FROM Beneficiaries WHERE PM_ID_NUM ="+
                        clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE Acc_ID ="+frmLogin.mGetUserAccId()))) {
                   
                    if(clsSQLMethods.mDeleteRecord("DELETE FROM Beneficiaries WHERE PM_ID_NUM="+frmLogin.mGetPrincipalMemberID())
                            && clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_NUM="+frmLogin.mGetPrincipalMemberID())
                                && clsSQLMethods.mDeleteRecord("DELETE FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId())) {
                
                        JOptionPane.showMessageDialog(this, "Your membership details have been deleted.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                } else {
                    if(clsSQLMethods.mCheckIfDetailsExist("SELECT ID_NUM FROM Principal_Members WHERE Acc_ID ="+frmLogin.mGetUserAccId())) {
                        
                        if(clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_NUM="+frmLogin.mGetPrincipalMemberID())
                                && clsSQLMethods.mDeleteRecord("DELETE FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId())) {
                
                            JOptionPane.showMessageDialog(this, "Your membership details have been deleted.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            
                        }
                    } else {
                        if(clsSQLMethods.mDeleteRecord("DELETE FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId())) {
                
                            JOptionPane.showMessageDialog(this, "Your membership details have been deleted.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            
                        }
                    }
                }
            }
        }
        this.dispose();
        frmLogin.mSetPrincipalMemberID(0L);
        frmLogin.mSetUserAccId(0);
        new frmLogin().setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dsktpMain = new javax.swing.JDesktopPane();
        spPrincipalMembers = new javax.swing.JScrollPane();
        tblPrincipalMbrs = new javax.swing.JTable();
        spMultiPurposeTable = new javax.swing.JScrollPane();
        tblMultiPurpose = new javax.swing.JTable();
        lblPrincipalMembersHeading = new javax.swing.JLabel();
        lblMultiPurposeHeading = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnCovers = new javax.swing.JButton();
        btnBeneficiaries = new javax.swing.JButton();
        btnClearSearch = new javax.swing.JButton();
        mbMenu = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuItemLogout = new javax.swing.JMenuItem();
        mnuItemExit = new javax.swing.JMenuItem();
        mnuCovers = new javax.swing.JMenu();
        mnuItemManageCovers = new javax.swing.JMenuItem();
        mnuRegisterPolicy = new javax.swing.JMenu();
        mnuItemRegisterAndManagePolicy = new javax.swing.JMenuItem();
        mnuItemBeneficiaries = new javax.swing.JMenuItem();
        mnuLoginAccount = new javax.swing.JMenu();
        mnuItemCreateAccount = new javax.swing.JMenuItem();
        mnuItemUpdateLoginDetails = new javax.swing.JMenuItem();
        mnuItemDeleteLoginDetails = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dsktpMain.setBackground(new java.awt.Color(255, 255, 255));
        dsktpMain.setForeground(new java.awt.Color(255, 255, 255));

        tblPrincipalMbrs.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblPrincipalMbrs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPrincipalMbrs.setGridColor(new java.awt.Color(0, 0, 0));
        spPrincipalMembers.setViewportView(tblPrincipalMbrs);

        tblMultiPurpose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblMultiPurpose.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblMultiPurpose.setGridColor(new java.awt.Color(0, 0, 0));
        spMultiPurposeTable.setViewportView(tblMultiPurpose);

        lblPrincipalMembersHeading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPrincipalMembersHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrincipalMembersHeading.setText("Principal Members Table");

        lblMultiPurposeHeading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblMultiPurposeHeading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMultiPurposeHeading.setText("Multi-Purpose Table");

        btnSearch.setBackground(new java.awt.Color(255, 255, 255));
        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSearch.setText("Search Name");
        btnSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCovers.setBackground(new java.awt.Color(255, 255, 255));
        btnCovers.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCovers.setText("Covers");
        btnCovers.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCovers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoversActionPerformed(evt);
            }
        });

        btnBeneficiaries.setBackground(new java.awt.Color(255, 255, 255));
        btnBeneficiaries.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBeneficiaries.setText("Beneficiaries");
        btnBeneficiaries.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBeneficiaries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeneficiariesActionPerformed(evt);
            }
        });

        btnClearSearch.setBackground(new java.awt.Color(255, 255, 255));
        btnClearSearch.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClearSearch.setText("Clear Search");
        btnClearSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });

        dsktpMain.setLayer(spPrincipalMembers, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(spMultiPurposeTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(lblPrincipalMembersHeading, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(lblMultiPurposeHeading, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(btnSearch, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(txtSearch, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(btnCovers, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(btnBeneficiaries, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dsktpMain.setLayer(btnClearSearch, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dsktpMainLayout = new javax.swing.GroupLayout(dsktpMain);
        dsktpMain.setLayout(dsktpMainLayout);
        dsktpMainLayout.setHorizontalGroup(
            dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dsktpMainLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dsktpMainLayout.createSequentialGroup()
                        .addComponent(spPrincipalMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(spMultiPurposeTable, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dsktpMainLayout.createSequentialGroup()
                        .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dsktpMainLayout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(dsktpMainLayout.createSequentialGroup()
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblPrincipalMembersHeading)))
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(429, 429, 429)
                        .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMultiPurposeHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCovers, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBeneficiaries, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        dsktpMainLayout.setVerticalGroup(
            dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dsktpMainLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrincipalMembersHeading)
                    .addComponent(lblMultiPurposeHeading))
                .addGap(40, 40, 40)
                .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCovers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBeneficiaries, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(dsktpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spMultiPurposeTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spPrincipalMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        mbMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        mnuFile.setText("File");

        mnuItemLogout.setText("Logout");
        mnuItemLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLogoutActionPerformed(evt);
            }
        });
        mnuFile.add(mnuItemLogout);

        mnuItemExit.setText("Exit");
        mnuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemExitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuItemExit);

        mbMenu.add(mnuFile);

        mnuCovers.setText("Covers");

        mnuItemManageCovers.setText("Manage Covers");
        mnuItemManageCovers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemManageCoversActionPerformed(evt);
            }
        });
        mnuCovers.add(mnuItemManageCovers);

        mbMenu.add(mnuCovers);

        mnuRegisterPolicy.setText("Register and Manage a Policy");

        mnuItemRegisterAndManagePolicy.setText("Register and Manage Policy");
        mnuItemRegisterAndManagePolicy.setToolTipText("");
        mnuItemRegisterAndManagePolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRegisterAndManagePolicyActionPerformed(evt);
            }
        });
        mnuRegisterPolicy.add(mnuItemRegisterAndManagePolicy);

        mnuItemBeneficiaries.setText("Beneficiaries");
        mnuItemBeneficiaries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemBeneficiariesActionPerformed(evt);
            }
        });
        mnuRegisterPolicy.add(mnuItemBeneficiaries);

        mbMenu.add(mnuRegisterPolicy);

        mnuLoginAccount.setText("Login Account");

        mnuItemCreateAccount.setText("Create Account");
        mnuItemCreateAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemCreateAccountActionPerformed(evt);
            }
        });
        mnuLoginAccount.add(mnuItemCreateAccount);

        mnuItemUpdateLoginDetails.setText("Update Login Details");
        mnuItemUpdateLoginDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemUpdateLoginDetailsActionPerformed(evt);
            }
        });
        mnuLoginAccount.add(mnuItemUpdateLoginDetails);

        mnuItemDeleteLoginDetails.setText("Delete Login Details");
        mnuItemDeleteLoginDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemDeleteLoginDetailsActionPerformed(evt);
            }
        });
        mnuLoginAccount.add(mnuItemDeleteLoginDetails);

        mbMenu.add(mnuLoginAccount);

        setJMenuBar(mbMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dsktpMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dsktpMain)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuItemRegisterAndManagePolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRegisterAndManagePolicyActionPerformed
        mRegisterAndManagePolicy();
    }//GEN-LAST:event_mnuItemRegisterAndManagePolicyActionPerformed

    private void mnuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemExitActionPerformed
        mExit();
    }//GEN-LAST:event_mnuItemExitActionPerformed

    private void mnuItemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLogoutActionPerformed
        mLogout();
    }//GEN-LAST:event_mnuItemLogoutActionPerformed

    private void mnuItemManageCoversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemManageCoversActionPerformed
        mManageCovers();
    }//GEN-LAST:event_mnuItemManageCoversActionPerformed

    private void mnuItemUpdateLoginDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemUpdateLoginDetailsActionPerformed
        mUpdateLoginAccount();
    }//GEN-LAST:event_mnuItemUpdateLoginDetailsActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        mSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnCoversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoversActionPerformed
        mCoversData();
    }//GEN-LAST:event_btnCoversActionPerformed

    private void btnBeneficiariesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeneficiariesActionPerformed
        mBeneficiariesData();
    }//GEN-LAST:event_btnBeneficiariesActionPerformed

    private void mnuItemDeleteLoginDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemDeleteLoginDetailsActionPerformed
        mDeleteLoginAccount();
    }//GEN-LAST:event_mnuItemDeleteLoginDetailsActionPerformed

    private void mnuItemBeneficiariesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemBeneficiariesActionPerformed
        new dialogBeneficiaries(this).setVisible(true);
    }//GEN-LAST:event_mnuItemBeneficiariesActionPerformed

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        mClearSearch();
    }//GEN-LAST:event_btnClearSearchActionPerformed

    private void mnuItemCreateAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemCreateAccountActionPerformed
        mSignUp();
    }//GEN-LAST:event_mnuItemCreateAccountActionPerformed

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
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new frmMain("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeneficiaries;
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnCovers;
    private javax.swing.JButton btnSearch;
    private javax.swing.JDesktopPane dsktpMain;
    private javax.swing.JLabel lblMultiPurposeHeading;
    private javax.swing.JLabel lblPrincipalMembersHeading;
    private javax.swing.JMenuBar mbMenu;
    private javax.swing.JMenu mnuCovers;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuItemBeneficiaries;
    private javax.swing.JMenuItem mnuItemCreateAccount;
    private javax.swing.JMenuItem mnuItemDeleteLoginDetails;
    private javax.swing.JMenuItem mnuItemExit;
    private javax.swing.JMenuItem mnuItemLogout;
    private javax.swing.JMenuItem mnuItemManageCovers;
    private javax.swing.JMenuItem mnuItemRegisterAndManagePolicy;
    private javax.swing.JMenuItem mnuItemUpdateLoginDetails;
    private javax.swing.JMenu mnuLoginAccount;
    private javax.swing.JMenu mnuRegisterPolicy;
    private javax.swing.JScrollPane spMultiPurposeTable;
    private javax.swing.JScrollPane spPrincipalMembers;
    private javax.swing.JTable tblMultiPurpose;
    private javax.swing.JTable tblPrincipalMbrs;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
