/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Matilda
 * 
 */
public class clsPolicyMemberOperations extends JDialog{
    
    public clsPolicyMemberOperations(frmMain frmParent, String str) {
        super(frmParent, str, Dialog.ModalityType.APPLICATION_MODAL); 
        this.setSize(400, 200); 
        this.setResizable(false);
        this.setLocationRelativeTo(null); 
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mCreateDialog(str); 
    }
    
    private final JComboBox cboMembers = new JComboBox(); 
        
    private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();   
    private final clsGUIDesignMethods gui = new clsGUIDesignMethods(); 
    private final clsModelAndDataMethods modelAndDataMethods = new clsModelAndDataMethods();  
    
    private dialogRegisterPolicy dialogReg;
    private dialogBeneficiaries dialogBeneficiary;
    private final frmLogin frmLogin = new frmLogin();
    
    public void mActiveInstanceOfDialogRegisterPolicy(dialogRegisterPolicy dialogReg) {
        this.dialogReg = dialogReg;
    }
    
    public void mActiveInstanceOfDialogBeneficiaries(dialogBeneficiaries dialogBeneficiary) {
      this.dialogBeneficiary = dialogBeneficiary;  
    }
    
    private void mCreateDialog(String str) {
        JPanel jpPanel = new JPanel(new BorderLayout(0, 20));  
        jpPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        jpPanel = gui.mPreparePanel(jpPanel); 
            
        switch (str) {
            case "Update Principal": case "Update Beneficiary":
                jpPanel.add(gui.mCreateLabel("Select Principal Member to Update", new Font("Tahoma", Font.BOLD, 16)), BorderLayout.NORTH);
                break;
                    
            case "Delete Principal": case "Delete Beneficiary":
                jpPanel.add(gui.mCreateLabel("Select Member to Delete", new Font("Tahoma", Font.BOLD, 16)), BorderLayout.NORTH);
                break;
        }
                        
        JPanel jpCenterPart = new JPanel(new BorderLayout()); 
        jpCenterPart.add(cboMembers, BorderLayout.CENTER); 
            
        if(str.equals("Update Principal") || str.equals("Delete Principal")){
                
            modelAndDataMethods.mLoadToComboBox("SELECT FName, LName FROM Principal_Members", cboMembers);   
            
        } else if(str.equals("Update Beneficiary") && clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("Administrator")
                || str.equals("Delete Beneficiary") && clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()).equals("Administrator")) {
            
            modelAndDataMethods.mLoadToComboBox("SELECT FName, LName FROM Beneficiaries", cboMembers);
            
        } else if(str.equals("Update Beneficiary") && clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId()).equals("General User")
                || str.equals("Delete Beneficiary") && clsSQLMethods.mGetTextField("SELECT Role FROM Users_Login WHERE ID="+frmLogin.mGetUserAccId()).equals("General User")) {
            
            modelAndDataMethods.mLoadToComboBox("SELECT FName, LName FROM Beneficiaries WHERE PM_ID_Num ="+frmLogin.mGetPrincipalMemberID(), cboMembers);
        }
            
        jpPanel.add(jpCenterPart);
            
        JPanel jpLowerPart = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); //A JPanel to contain the lower part of the GUI
        jpLowerPart = gui.mPreparePanel(jpLowerPart);
            
        switch (str) {
            case "Update Principal":
                jpLowerPart.add(gui.mCreateButton(100, 25, "Ok", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mPrincipalMemberDetails();
                        clsPolicyMemberOperations.this.dispose();
                    }
                }));
                jpPanel.add(jpLowerPart, BorderLayout.SOUTH);
                break;
                
            case "Update Beneficiary":
                jpLowerPart.add(gui.mCreateButton(100, 25, "Ok", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mBeneficiaryMemberDetails();
                        clsPolicyMemberOperations.this.dispose();
                    }
                }));
                jpPanel.add(jpLowerPart, BorderLayout.SOUTH);
                break;
                    
            case "Delete Principal":
                jpLowerPart.add(gui.mCreateButton(100, 25, "Delete", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { 
                           
                        Long lngPrincipalMemberID = clsSQLMethods.mGetNumericField("SELECT ID_Num FROM Principal_Members WHERE FName='"+mSelectedOption()[0]
                                                +"' AND LName ='"+mSelectedOption()[1]+"'");
                        
                        if(clsSQLMethods.mCheckIfDetailsExist("SELECT PM_ID_Num FROM Beneficiaries WHERE PM_ID_Num="+lngPrincipalMemberID)) {
                            
                            if(clsSQLMethods.mDeleteRecord("DELETE FROM Beneficiaries WHERE PM_ID_Num ="+lngPrincipalMemberID) 
                                    && clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_Num ="+lngPrincipalMemberID)){
                                
                                JOptionPane.showMessageDialog(clsPolicyMemberOperations.this, 
                                    "All details relating to this Principal Member Policy have been deleted.",
                                        "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            if(clsSQLMethods.mDeleteRecord("DELETE FROM Principal_Members WHERE ID_Num ="+lngPrincipalMemberID)){
                                
                                JOptionPane.showMessageDialog(clsPolicyMemberOperations.this, 
                                    "Principal Member Policy has been deleted.",
                                        "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } 
                        clsPolicyMemberOperations.this.dispose();
                    }
                }));
                jpPanel.add(jpLowerPart, BorderLayout.SOUTH);
                break;
                
            case "Delete Beneficiary":
                jpLowerPart.add(gui.mCreateButton(100, 25, "Delete", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(clsSQLMethods.mDeleteRecord("DELETE FROM Beneficiaries WHERE FName ='"+mSelectedOption()[0]+"' AND LName ='"+mSelectedOption()[1]+"'")) {
                            JOptionPane.showMessageDialog(clsPolicyMemberOperations.this, 
                                    "Beneficiary details have been deleted", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                        }
                        clsPolicyMemberOperations.this.dispose();
                    }
                }));
                jpPanel.add(jpLowerPart, BorderLayout.SOUTH);
                break;
        }
        this.add(jpPanel);
    }

    private String[] mSelectedOption() {
        return new String[] {
            cboMembers.getSelectedItem().toString()
                .substring(0, cboMembers.getSelectedItem().toString()
                    .indexOf(" ")).trim(),
            
            cboMembers.getSelectedItem().toString()
                .substring(cboMembers.getSelectedItem().toString().indexOf(" "),
                    cboMembers.getSelectedItem().toString().trim().length()).trim()
        };
    }
    
    
    private void mPrincipalMemberDetails() {
                
        dialogReg.mSetDetailsForPrincipalMemberUpdate(clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Principal_Members WHERE FName ='"+mSelectedOption()[0]+
                "' AND LName ='"+mSelectedOption()[1]+"'"));
        
    }       
    
    private void mBeneficiaryMemberDetails() {
        dialogBeneficiary.mSetDetailsForBeneficiaryUpdate(clsSQLMethods.mGetNumericField("SELECT ID_NUM FROM Beneficiaries WHERE FName ='"+mSelectedOption()[0]+
                    "' AND LName ='"+mSelectedOption()[1]+"'"));
    }
}