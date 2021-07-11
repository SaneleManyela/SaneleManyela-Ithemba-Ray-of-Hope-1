/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Matilda
 * 
 *  
 */
public class clsModelAndDataMethods {
    
    private final clsDatabaseMethods clsSQLMethods = new clsDatabaseMethods();
    
    public java.util.List<String> mRemoveEmptyIndexes(String[] array) {
        
        java.util.List<String> values = new ArrayList<>();
        
        try {
            for (String element : array) {
                if (element != null) {
                    values.add(element);
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return values;
    }
    
    public DefaultTableModel mTableData(String strQuery, DefaultTableModel model) {
        try {
            try (Statement stStatement = clsSQLMethods.mConnectToDatabase().prepareStatement(strQuery)) {
                
                ResultSet rs = stStatement.executeQuery(strQuery);
                ResultSetMetaData rsmt = rs.getMetaData();
                int intColumnCount = rsmt.getColumnCount();
                
                for(int i = 1; i <= intColumnCount; i++) {
                    model.addColumn(rsmt.getColumnName(i));
                }
                
                while(rs.next()) {
                    
                    Object[] arrRow = new Object[intColumnCount + 1];
                    for(int i = 1; i <= intColumnCount; i++) {
                        arrRow[i] = (rs.getObject(i));
                    }               
                    
                    String[] arrRowData = new String[arrRow.length];
                    for(int i = 1; i < arrRow.length; i++) {
                        if(!(arrRow[i] == null)) {
                            arrRowData[i] = arrRow[i].toString();
                        }
                    }
                    
                    model.addRow(mRemoveEmptyIndexes(arrRowData).toArray(
                            new String[mRemoveEmptyIndexes(arrRowData).size()]));
                }
                return model;
            } 
        }catch(SQLException | NullPointerException e) {
                
        } 
        return model;
    }  
    
    public JTable mTable(String strQuery, JTable tbl, DefaultTableModel model) {
        
        model = mTableData(strQuery, model);
        tbl.setModel(model);
        tbl.setFillsViewportHeight(true);
        tbl.validate();
        
        return tbl;
    }
    
    public void mLoadToComboBox(String strQuery, JComboBox cbo) {
        try {
            try (Statement stStatement = 
                    clsSQLMethods.mConnectToDatabase().prepareStatement(strQuery)) {
                
                stStatement.execute(strQuery);
                
                try (ResultSet rs = stStatement.getResultSet()) {
                                        
                    if(strQuery.contains(",")) {
                        
                        while(rs.next()) {
                            cbo.addItem(rs.getString(1) + " " + rs.getString(2));
                        }
                        
                    } else {
                        
                        while(rs.next()) {
                            cbo.addItem(rs.getString(1));
                        }
                        
                    }
                    
                    stStatement.close();
                    rs.close();
                }
            }
        } catch(SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(null,"A technical error has been encountered\n"+e.getMessage());
        }
    }
}