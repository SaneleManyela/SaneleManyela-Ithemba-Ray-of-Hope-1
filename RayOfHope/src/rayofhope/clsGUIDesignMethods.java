/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Matilda
 * 
 * 
 */
public class clsGUIDesignMethods {
    
    public JPanel mPreparePanel(JPanel jpPanel) {
        jpPanel.setOpaque(true);
        jpPanel.setBackground(new Color(255, 255, 255));
        return jpPanel;
    }
    
    public JLabel mCreateLabel(String strText, Font f) {
        JLabel lblLabel = new JLabel(strText);
        lblLabel.setHorizontalAlignment(JLabel.CENTER);
        lblLabel.setOpaque(true);
        lblLabel.setBackground(new Color(255, 255, 255));
        return lblLabel;
    }
    
    public JButton mCreateButton(int intWidth, int intHeight,
            String strText, ActionListener listener) {
            
        JButton btnButton = new JButton(strText);
        btnButton.addActionListener(listener);
        btnButton.setPreferredSize(new Dimension(intWidth, intHeight));
            
        btnButton.setBackground(new Color(255, 255, 255));
        btnButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        btnButton.setText(strText);
            
        return btnButton;
    }
    
    public JTextField mTextFieldDimensions(JTextField txt, int intWidth,
            int intHeight, String strToolTip){
            
        txt.setEnabled(true);
        txt.setPreferredSize(new Dimension(intWidth, intHeight));
        txt.setToolTipText(strToolTip);
        return txt;
    }
    
    public JComboBox mComboBoxDimensions(JComboBox cbo, int intWidth, int intHeight) {
        cbo.setSize(new Dimension(intWidth, intHeight));
        return cbo;
    }
        
    public JPanel mAddComponent(String str, Component component) {
        JPanel jpComponent = new JPanel(new GridLayout(1, 2, 40, 0));
        jpComponent = new clsGUIDesignMethods().mPreparePanel(jpComponent);
            
        JLabel lblLabel = new JLabel(str);
        lblLabel.setSize(new Dimension(80, 30));
            
        jpComponent.add(lblLabel);
            component.setSize(new Dimension(200, 30));
            jpComponent.add(component);
            
        return jpComponent;
    }
}