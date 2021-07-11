/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author Matilda
 */
public class dialogRegisterPolicy extends JDialog {

    /**
     * Creates new form dialogAddAccount
     * @param frmParent
     */
    public dialogRegisterPolicy(frmMain frmParent) {
        super(frmParent, "Policy Registration", ModalityType.APPLICATION_MODAL);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.frmMn = frmParent;
        modelAndDataMethods.mLoadToComboBox("SELECT DISTINCT CoverAmount FROM Covers", cboCovers);
        modelAndDataMethods.mLoadToComboBox("SELECT Premium FROM Covers ORDER BY Premium ASC", cboPremiums);
        txtFirstName.requestFocusInWindow();
    }
    
    private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    private final clsModelAndDataMethods modelAndDataMethods = new clsModelAndDataMethods();
    private final clsValidation validation = new clsValidation();
    
    private final frmLogin frmLogin = new frmLogin();
    private final frmMain frmMn;

    private Long lngPrincipalID; 
    
    public void mSetPrincipalLoginIDForRegistration(int intPM) {
        mRegistration(intPM);
    }
    
    private String[] mGetPolicyDetailsForUpdate() {
        String[] arrPrincipalMemberUpdateDetails = new String[] {
          txtFirstName.getText().trim(), txtLastName.getText().trim(),
            txtIDNum.getText().trim(), txtAddress.getText().trim(), 
            txtTelNo.getText().trim(), txtEmail.getText().trim(),
            cboCovers.getSelectedItem().toString(),
            cboPremiums.getSelectedItem().toString()
        };
        
        if(JOptionPane.showConfirmDialog(this, "Would you like to update date of birth?", 
                "Update Date of Birth", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
            
            String strDate = JOptionPane.showInputDialog(this, "Enter Date of Birth with the format yyyy-MM-dd", 
                    "Update Date of Birth", JOptionPane.PLAIN_MESSAGE);
            
            if(!strDate.equals("")) {
                
                if(validation.mValidateDate(strDate).equals("")) {
                    
                    if(Period.between((LocalDate.parse((dcDOB.getSelection().toString().substring(1, 11)).replace('/', '-'))),
                            LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() < 18 
                        || Period.between((LocalDate.parse((dcDOB.getSelection().toString().substring(1, 11)).replace('/', '-'))),
                                LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() >= 65) {
                        
                        JOptionPane.showMessageDialog(this, "A Principal member must be 18 or above but less than 65 years old!",
                                "WARNING", JOptionPane.WARNING_MESSAGE); 
                        return arrPrincipalMemberUpdateDetails;
                    } else {   
                        String[] arrAllPrincipalMemberDetails = new String[9];
                        arrAllPrincipalMemberDetails[8] = strDate;
                        arrAllPrincipalMemberDetails = arrPrincipalMemberUpdateDetails;
                        return arrAllPrincipalMemberDetails;
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this, validation.mValidateDate(strDate), "WARNING", JOptionPane.WARNING_MESSAGE);
                    return arrPrincipalMemberUpdateDetails;
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "No date input.\n The date of birth will not be updated", "WARNING", JOptionPane.WARNING_MESSAGE);
                return arrPrincipalMemberUpdateDetails;
            }
        }
        return arrPrincipalMemberUpdateDetails;
    }
    
    public void mSetDetailsForPrincipalMemberUpdate(Long lngID) {
        lngPrincipalID = lngID;
        String[] arrPrincipalMemberDetails = clsSQLMethods.mFetchRecord("SELECT FName, LName, ID_Num, Address, Tel, Email, Cover_ID "
                    + "FROM Principal_Members WHERE ID_Num ="+lngID);
                
        txtFirstName.setText(arrPrincipalMemberDetails[0]);
        txtLastName.setText(arrPrincipalMemberDetails[1]);
        txtIDNum.setText(arrPrincipalMemberDetails[2]);
        txtAddress.setText(arrPrincipalMemberDetails[3]);
        txtTelNo.setText(arrPrincipalMemberDetails[4]);
        txtEmail.setText(arrPrincipalMemberDetails[5]);
        cboCovers.setSelectedItem(clsSQLMethods.mGetNumericField(
                "SELECT CoverAmount FROM Covers WHERE ID ="+arrPrincipalMemberDetails[6]));
        cboPremiums.setSelectedItem(clsSQLMethods.mGetNumericField(
                "SELECT Premium FROM Covers WHERE ID ="+arrPrincipalMemberDetails[6]));
    }
    
    private String mVerifyRegistrationInput() {
        if(txtFirstName.getText().equals("")) {
            return "Please provide First Name!";
            
        } else if(txtLastName.getText().equals("")) {
            return "Please provide Last Name!";
            
        } else if(txtIDNum.getText().equals("")) {
            return "Please provide ID Number!";
            
        } else if(txtIDNum.getText().length() != 13) {
            return "South African ID number is 13 digits";
            
        } else if(txtAddress.getText().equals("")) {
            return "Please provide Address!";
            
        } else if(txtTelNo.getText().equals("")) {
            return "Enter telephone number!";
            
        } else if(txtTelNo.getText().length() != 10) {
            return "A Tel Number must have 10 digits!";
            
        } else if(txtEmail.getText().equals("")) {
            return "Enter email address!";
            
        } else if(!btnUpdatePolicy.getText().equals("Save..")) { 
            try{
                if(Period.between((LocalDate.parse((dcDOB.getSelection().toString().substring(1, 11)).replace('/', '-'))),
                        LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() < 18 
                    || Period.between((LocalDate.parse((dcDOB.getSelection().toString().substring(1, 11)).replace('/', '-'))),
                                LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() >= 65) {
                    return "A Principal member must be 18 or above but less than 65 years old!";
            
                } 
            }catch(DateTimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "";
    }
    
    private void mRegistration(int Acc_ID) {
        if(mVerifyRegistrationInput().equals("")) {
              
                if(!clsSQLMethods.mCheckIfDetailsExist("SELECT * FROM Principal_Members WHERE ID_Num ="+txtIDNum.getText().trim())) {
                    
                    if(validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText().trim()).equals("")
                        && validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText().trim()).equals("")
                            && validation.mValidateContactNumber(txtTelNo.getText().trim()).equals("")
                            && validation.mValidateEmail(txtEmail.getText().trim()).equals("")) {
                
            
                        if(clsSQLMethods.mCreateRecord("INSERT INTO Principal_Members (ID_Num, FName, LName, DOB, Address, Tel, Email, Cover_ID, Acc_ID)"
                                + "VALUES('"+txtIDNum.getText().trim()+"','"+txtFirstName.getText().trim()+"','"+txtLastName.getText().trim()
                                    +"','"+dcDOB.getSelection().toString().substring(1, 11).replace('/', '-')+"','"+txtAddress.getText().trim()+"','"+txtTelNo.getText().trim()
                                    +"','"+txtEmail.getText().trim()+"','"+clsSQLMethods.mGetNumericField(
                                    "SELECT ID FROM Covers WHERE CoverAmount ="+cboCovers.getSelectedItem().toString()+
                                            " AND Premium ="+cboPremiums.getSelectedItem().toString())+"','"+Acc_ID+"')")) {
                    
                            JOptionPane.showMessageDialog(this, "Burial Policy Registered", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            
                            if(JOptionPane.showConfirmDialog(this, "Would you like to register beneficiaries?", "Register Beneficiaries",
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                
                                new dialogBeneficiaries(frmMn).setVisible(true);
                            }
                        }
                    
                    } else if(!validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText().trim()).equals("")) {
                        JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText()),
                            "WARNING", JOptionPane.WARNING_MESSAGE);
              
                    } else if(!validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText()).equals("")) {
                        JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText()), 
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                
                    } else if(!validation.mValidateContactNumber(txtTelNo.getText().trim()).equals("")) {
                        JOptionPane.showMessageDialog(this, validation.mValidateContactNumber(txtTelNo.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
                        
                    }else if(!validation.mValidateEmail(txtEmail.getText().trim()).equals("")) {
                        JOptionPane.showMessageDialog(this, validation.mValidateEmail(txtEmail.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "An account with this ID Number already exists", "WARNING", JOptionPane.WARNING_MESSAGE);
                
                }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyRegistrationInput(),
                    "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mRegisterPolicy(int intAccID) {
        if(clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("General User")) {
            mRegistration(intAccID);
                     
        } else {
            if(mVerifyRegistrationInput().equals("")) {
                if(JOptionPane.showConfirmDialog(this, "Create a login account for this member first",
                    "Create Login Account", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) {
                    new clsSignUp(this, "Sign Up PM").setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Enter policy details first.", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    } 
    
    private void mUpdatePrincipalMemberDetails() {
        if(clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("Administrator")) {
            clsPolicyMemberOperations memberOperations = new clsPolicyMemberOperations(frmMn, "Update Principal");
            memberOperations.mActiveInstanceOfDialogRegisterPolicy(this);
            memberOperations.setVisible(true);
            
        } else if (clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("General User")) {
            mSetDetailsForPrincipalMemberUpdate(
                        clsSQLMethods.mGetNumericField("SELECT ID_Num FROM Principal_Members WHERE ACC_ID="+frmLogin.mGetUserAccId()));
            btnUpdatePolicy.setText("Save..");
        }
    }
    
    private void mSaveUpdateDetails() {
        if(mVerifyRegistrationInput().equals("")) {
            if(validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText().trim()).equals("")
                        && validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText().trim()).equals("")
                    && validation.mValidateContactNumber(txtTelNo.getText().trim()).equals("")
                            && validation.mValidateEmail(txtEmail.getText().trim()).equals("")) {
                
                String[] arrUpdatedDetails = mGetPolicyDetailsForUpdate();
                
                if(arrUpdatedDetails.length == 9) {
                    if(clsSQLMethods.mUpdateRecord("UPDATE Principal_Members SET ID_Num ="+arrUpdatedDetails[2]+", FName='"+arrUpdatedDetails[0]
                            +"', LName ='"+arrUpdatedDetails[1]+"', DOB='"+arrUpdatedDetails[8]+"', Address='"+arrUpdatedDetails[3]
                            +"', Tel='"+arrUpdatedDetails[4]+"', Email='"+arrUpdatedDetails[5]
                            +"', Cover_ID ="+clsSQLMethods.mGetNumericField("SELECT ID FROM Covers WHERE CoverAmount="+arrUpdatedDetails[6]+
                                    " AND Premium ="+arrUpdatedDetails[7])+" WHERE ID_Num ="+lngPrincipalID)) {
                        
                        JOptionPane.showMessageDialog(this, "Principal Member Policy details have been updated.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    if(clsSQLMethods.mUpdateRecord("UPDATE Principal_Members SET ID_Num ="+arrUpdatedDetails[2]+", FName='"+arrUpdatedDetails[0]
                            +"', LName ='"+arrUpdatedDetails[1]+"', Address='"+arrUpdatedDetails[3]+"', Tel='"+arrUpdatedDetails[4]
                            +"', Email='"+arrUpdatedDetails[5]+"', Cover_ID ="+clsSQLMethods.mGetNumericField("SELECT ID FROM Covers WHERE CoverAmount="+arrUpdatedDetails[6]+
                                    " AND Premium ="+arrUpdatedDetails[7])+" WHERE ID_Num ="+lngPrincipalID)) {
                        
                        JOptionPane.showMessageDialog(this, "Principal Member Policy details have been updated.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else if(!validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtIDNum.getText()),
                    "WARNING", JOptionPane.WARNING_MESSAGE);
              
            } else if(!validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mCheckIfFieldIsOnlyDigits(txtTelNo.getText()), 
                    "WARNING", JOptionPane.WARNING_MESSAGE);
                
            } else if(!validation.mValidateContactNumber(txtTelNo.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mValidateContactNumber(txtTelNo.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
                        
            }else if(!validation.mValidateEmail(txtEmail.getText().trim()).equals("")) {
                JOptionPane.showMessageDialog(this, validation.mValidateEmail(txtEmail.getText().trim()), "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyRegistrationInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mDeletePrincipalMemberPolicyDetails() {
        if(clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("Administrator")) {
            
            new clsPolicyMemberOperations(frmMn, "Delete Principal").setVisible(true);
            
        } else {
            if(clsSQLMethods.mCheckIfDetailsExist("SELECT PM_ID_Num FROM Beneficiaries WHERE PM_ID_Num="+
                    clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE ACC_ID ="+frmLogin.mGetUserAccId()))) {
                
                if(clsSQLMethods.mDeleteRecord("DELETE FROM Beneficiaries WHERE PM_ID_Num ="+clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE ACC_ID ="+frmLogin.mGetUserAccId()))
                    && clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_Num ="+clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE ACC_ID ="+frmLogin.mGetUserAccId()))  
                    ){
                                
                    JOptionPane.showMessageDialog(dialogRegisterPolicy.this, "All details relating to this Principal Member Policy have been deleted.",
                                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if(clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_Num ="+
                        clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE ACC_ID ="+frmLogin.mGetUserAccId()))){
                                
                    JOptionPane.showMessageDialog(dialogRegisterPolicy.this, "Principal Member Policy has been deleted.",
                                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    private void mClearPolicyRegForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtIDNum.setText("");
        txtAddress.setText("");
        txtTelNo.setText("");
        txtEmail.setText("");
    }
           
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpRegisterDialog = new javax.swing.JPanel();
        lblPolicyRegHeading = new javax.swing.JLabel();
        lblFirstName = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        lblIDNum = new javax.swing.JLabel();
        lblDOB = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblTelNumber = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblCover = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        txtIDNum = new javax.swing.JTextField();
        dcDOB = new datechooser.beans.DateChooserCombo();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        txtTelNo = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cboCovers = new javax.swing.JComboBox<>();
        jpRegisterPolicyCommands = new javax.swing.JPanel();
        btnRegisterPolicy = new javax.swing.JButton();
        btnUpdatePolicy = new javax.swing.JButton();
        btnDeletePolicy = new javax.swing.JButton();
        btnClearPolicyData = new javax.swing.JButton();
        lblPremiums = new javax.swing.JLabel();
        cboPremiums = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpRegisterDialog.setBackground(new java.awt.Color(255, 255, 255));

        lblPolicyRegHeading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPolicyRegHeading.setText("Register a Policy");

        lblFirstName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFirstName.setText("First Name");

        lblLastName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLastName.setText("Last Name");

        lblIDNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblIDNum.setText("ID Number");

        lblDOB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDOB.setText("Date of Birth");

        lblAddress.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAddress.setText("Address");

        lblTelNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTelNumber.setText("Tel Number");

        lblEmail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEmail.setText("Email");

        lblCover.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCover.setText("Cover");

        txtIDNum.setToolTipText("");

        txtAddress.setColumns(20);
        txtAddress.setRows(5);
        jScrollPane1.setViewportView(txtAddress);

        jpRegisterPolicyCommands.setBackground(new java.awt.Color(255, 255, 255));
        jpRegisterPolicyCommands.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnRegisterPolicy.setBackground(new java.awt.Color(255, 255, 255));
        btnRegisterPolicy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRegisterPolicy.setText("Register");
        btnRegisterPolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterPolicyActionPerformed(evt);
            }
        });

        btnUpdatePolicy.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdatePolicy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdatePolicy.setText("Update");
        btnUpdatePolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePolicyActionPerformed(evt);
            }
        });

        btnDeletePolicy.setBackground(new java.awt.Color(255, 255, 255));
        btnDeletePolicy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeletePolicy.setText("Delete");
        btnDeletePolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePolicyActionPerformed(evt);
            }
        });

        btnClearPolicyData.setBackground(new java.awt.Color(255, 255, 255));
        btnClearPolicyData.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClearPolicyData.setText("Clear");
        btnClearPolicyData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPolicyDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpRegisterPolicyCommandsLayout = new javax.swing.GroupLayout(jpRegisterPolicyCommands);
        jpRegisterPolicyCommands.setLayout(jpRegisterPolicyCommandsLayout);
        jpRegisterPolicyCommandsLayout.setHorizontalGroup(
            jpRegisterPolicyCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRegisterPolicyCommandsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnRegisterPolicy)
                .addGap(15, 15, 15)
                .addComponent(btnUpdatePolicy, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnDeletePolicy, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnClearPolicyData, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jpRegisterPolicyCommandsLayout.setVerticalGroup(
            jpRegisterPolicyCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRegisterPolicyCommandsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jpRegisterPolicyCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegisterPolicy)
                    .addComponent(btnUpdatePolicy)
                    .addComponent(btnDeletePolicy)
                    .addComponent(btnClearPolicyData))
                .addGap(20, 20, 20))
        );

        lblPremiums.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPremiums.setText("Premiums");

        javax.swing.GroupLayout jpRegisterDialogLayout = new javax.swing.GroupLayout(jpRegisterDialog);
        jpRegisterDialog.setLayout(jpRegisterDialogLayout);
        jpRegisterDialogLayout.setHorizontalGroup(
            jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpRegisterPolicyCommands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                        .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPremiums)
                            .addComponent(lblTelNumber)
                            .addComponent(lblEmail)
                            .addComponent(lblCover)
                            .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblFirstName)
                                .addComponent(lblLastName))
                            .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblIDNum)
                                    .addComponent(lblDOB)
                                    .addComponent(lblAddress))))
                        .addGap(197, 197, 197)
                        .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cboPremiums, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelNo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(dcDOB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(txtIDNum, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLastName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cboCovers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(40, 40, 40))
            .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(lblPolicyRegHeading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpRegisterDialogLayout.setVerticalGroup(
            jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblPolicyRegHeading)
                        .addGap(20, 20, 20)
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLastName))
                        .addGap(25, 25, 25)
                        .addComponent(txtIDNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(lblFirstName)
                        .addGap(76, 76, 76)
                        .addComponent(lblIDNum)))
                .addGap(25, 25, 25)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dcDOB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDOB))
                .addGap(25, 25, 25)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtTelNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpRegisterDialogLayout.createSequentialGroup()
                        .addComponent(lblAddress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTelNumber)))
                .addGap(25, 25, 25)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addGap(25, 25, 25)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboCovers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCover))
                .addGap(25, 25, 25)
                .addGroup(jpRegisterDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboPremiums, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPremiums))
                .addGap(30, 30, 30)
                .addComponent(jpRegisterPolicyCommands, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpRegisterDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpRegisterDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearPolicyDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPolicyDataActionPerformed
        mClearPolicyRegForm();
    }//GEN-LAST:event_btnClearPolicyDataActionPerformed

    private void btnRegisterPolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterPolicyActionPerformed
        mRegisterPolicy(frmLogin.mGetUserAccId());
    }//GEN-LAST:event_btnRegisterPolicyActionPerformed

    private void btnUpdatePolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePolicyActionPerformed
        if(btnUpdatePolicy.getText().equals("Update")) {
            mUpdatePrincipalMemberDetails();
            btnUpdatePolicy.setText("Save..");
                
        } else if(btnUpdatePolicy.getText().equals("Save..")) {
            
            mSaveUpdateDetails();
            btnUpdatePolicy.setText("Update");
        }
    }//GEN-LAST:event_btnUpdatePolicyActionPerformed

    private void btnDeletePolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePolicyActionPerformed
        mDeletePrincipalMemberPolicyDetails();
    }//GEN-LAST:event_btnDeletePolicyActionPerformed

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
            java.util.logging.Logger.getLogger(dialogRegisterPolicy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialogRegisterPolicy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialogRegisterPolicy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialogRegisterPolicy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialogRegisterPolicy dialog = new dialogRegisterPolicy((frmMain) new javax.swing.JFrame());
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
    private javax.swing.JButton btnClearPolicyData;
    private javax.swing.JButton btnDeletePolicy;
    private javax.swing.JButton btnRegisterPolicy;
    private javax.swing.JButton btnUpdatePolicy;
    private javax.swing.JComboBox<String> cboCovers;
    private javax.swing.JComboBox<String> cboPremiums;
    private datechooser.beans.DateChooserCombo dcDOB;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpRegisterDialog;
    private javax.swing.JPanel jpRegisterPolicyCommands;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblDOB;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblIDNum;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblPolicyRegHeading;
    private javax.swing.JLabel lblPremiums;
    private javax.swing.JLabel lblTelNumber;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtIDNum;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtTelNo;
    // End of variables declaration//GEN-END:variables
}
