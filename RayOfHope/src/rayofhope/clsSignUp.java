/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Sanele
 */
public class clsSignUp extends JDialog{
    public clsSignUp(JFrame frmParent, String stUserAction) {
        super(frmParent, Dialog.ModalityType.APPLICATION_MODAL);
        if(frmLogin.mGetUserAccId() != 0) {
            this.setTitle("Update Login Account");
        } else {
            this.setTitle("Sign Up");
        }
        mClassSignUp(stUserAction);
    }
    
    public clsSignUp(dialogRegisterPolicy dialogParent, String strAction) {
       super(dialogParent, "Sign Up", Dialog.ModalityType.APPLICATION_MODAL);
       mClassSignUp(strAction);
       this.registerPolicy = dialogParent;
    }
    
    private void mClassSignUp(String stUserAction) {
        this.setSize(400, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.strAction = stUserAction;
        mCreateSignUpInterface();
        if(strAction.equals("Update")) {
            mSetDetailsToGUI();
        }
        if(strAction.equals("Update")) {
            cboRole.setEnabled(false);
        }
        txtUsername.requestFocusInWindow();
    }
    
    private final JTextField txtUsername = new JTextField();
    private final JTextField txtPassword = new JTextField();
    private final JComboBox cboRole = new JComboBox(new String[] {"General User", "Administrator"});
    
    private String strAction;
    
    clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    clsGUIDesignMethods gui = new clsGUIDesignMethods();
    dialogRegisterPolicy registerPolicy;
    frmLogin frmLogin = new frmLogin();
    
    private void mCreateSignUpInterface() {
        JPanel jpPanel = new JPanel(new BorderLayout(10, 20));
        jpPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        jpPanel = gui.mPreparePanel(jpPanel);
        
        jpPanel.add(mWindowTop(), BorderLayout.NORTH);
        jpPanel.add(mWindowCenter(), BorderLayout.CENTER);
        jpPanel.add(mWindowBottom(), BorderLayout.SOUTH);
            
        this.add(jpPanel);
    }
    
    private JPanel mWindowTop() {
        JPanel jpTop = new JPanel(new BorderLayout());
        jpTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        jpTop = gui.mPreparePanel(jpTop);
        
        switch(strAction) {
            case "Sign Up": 
                jpTop.add(gui.mCreateLabel("Sign Up", new Font("Tahoma", Font.BOLD, 18)),
                    BorderLayout.NORTH);
                break;
            
            case "Sign Up PM":
                jpTop.add(gui.mCreateLabel("Sign Up A Principal Member", new Font("Tahoma", Font.BOLD, 18)), 
                        BorderLayout.NORTH);
                break;
            
            case "Update":
                jpTop.add(gui.mCreateLabel("Update", new Font("Tahoma", Font.BOLD, 18)),
                    BorderLayout.NORTH);
                break;
        }
            
        return jpTop;
    }
    
    private JPanel mWindowCenter() {
        JPanel jpCenter = new JPanel(new GridLayout(6, 4, 20, 20));
        jpCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        jpCenter = gui.mPreparePanel(jpCenter);
            
        jpCenter.add(gui.mAddComponent("Username", gui.mTextFieldDimensions(txtUsername, 80, 30, "Enter username for account")));
        jpCenter.add(gui.mAddComponent("Password", gui.mTextFieldDimensions(txtPassword, 80, 30, 
                "Enter password for account")));
        jpCenter.add(gui.mAddComponent("Role", gui.mComboBoxDimensions(cboRole, 80, 30)));
        return jpCenter;
    }

    private JPanel mWindowBottom() {
        JPanel jpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        jpPanel.setBackground(new Color(255, 255, 255));
        
        switch(strAction) {
            case "Sign Up": case "Sign Up PM":
                jpPanel.add(gui.mCreateButton(120, 30, "Sign Up", this::mSignUp));
            break;
                
            case "Update":
                jpPanel.add(gui.mCreateButton(120, 30, "Update", this::mUpdateAccount));
                break;
        }
        
        jpPanel.add(gui.mCreateButton(120, 30, "Clear", this::mClear));
        return jpPanel; 
    }
    
    private String mVerifyInput() {
        if(txtUsername.getText().equals("")) {
            return "Please provide an account username!";
        } else if(txtPassword.getText().equals("")) {
            return "Please provide an account password";
        }
        return "";
    }
    
    private void mSetDetailsToGUI() {
        txtUsername.setText(clsSQLMethods.mGetTextField("SELECT Username FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()));
        txtPassword.setText(clsSQLMethods.mGetTextField("SELECT Password FROM Users_Login WHERE ID ="+frmLogin.mGetUserAccId()));
    }
    
    private void mSignUp(ActionEvent e) {
        if(mVerifyInput().equals("")){
            if(!clsSQLMethods.mCheckIfDetailsExist("SELECT Username FROM Users_Login WHERE Username ='"+txtUsername.getText().trim()+"'")) {
                
                if(clsSQLMethods.mCreateRecord("INSERT INTO Users_Login (Username, Password, Role)"
                        + "VALUES('"+txtUsername.getText().trim()+"','"+txtPassword.getText().trim()+"','"+cboRole.getSelectedItem().toString()+"')")) {
                    
                    if(frmLogin.mGetUserAccId() != 0) {
                        JOptionPane.showMessageDialog(this, "Principal Member login account has been created");
                        this.dispose();
                        registerPolicy.mSetPrincipalLoginIDForRegistration(Integer.parseInt(
                                clsSQLMethods.mGetTextField("SELECT ID FROM Users_Login ORDER BY ID DESC LIMIT 1")));
                        
                    } else {
                        JOptionPane.showMessageDialog(this, "You have sucessfully signed up.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Account username already exists.",
                        "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, mVerifyInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mUpdateAccount(ActionEvent e) {
       if(mVerifyInput().equals("")) {
           if(!clsSQLMethods.mGetTextField("SELECT Username FROM Users_Login WHERE ID ='"+
                frmLogin.mGetUserAccId()+"'").equals(txtUsername.getText().trim()) && 
                    clsSQLMethods.mCheckIfDetailsExist("SELECT Username FROM Users_Login"
                        + " WHERE Username='"+txtUsername.getText().trim()+"'")) {
                        
                JOptionPane.showMessageDialog(this, "This username already exists, Provide alternative.", 
                    "WARNING", JOptionPane.WARNING_MESSAGE);
                        
            } else {
                if(clsSQLMethods.mUpdateRecord("UPDATE Users_Login SET Username ='"+txtUsername.getText().trim()+
                        "', Password ='"+txtPassword.getText().trim()+"' WHERE ID ="+frmLogin.mGetUserAccId())) {
                    
                    JOptionPane.showMessageDialog(this, "Account details have been Updated", "INFORMATION",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
       } else {
           JOptionPane.showMessageDialog(this, mVerifyInput(), "WARNING", JOptionPane.WARNING_MESSAGE);
       }
    }
        
    private void mClear(ActionEvent e) {
        txtUsername.setText("");
        txtPassword.setText("");
    }
}
