/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rayofhope;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Matilda
 */
public class clsDatabaseMethods {
    
    public Connection mConnectToDatabase() {
        String strDBConnectionString = "jdbc:mysql://localhost:3306/burial_society_db";
        String strUser = "root";
        String strPassword = "password";
        try {
            return DriverManager.getConnection(strDBConnectionString, 
                    strUser, strPassword);
        } catch(SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage() ,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    
    public boolean mCheckIfDetailsExist(String strQuery) {
        boolean boolStatus = false;
        Statement stStatement = null;
        ResultSet rs = null;
        try{
            stStatement = mConnectToDatabase().prepareStatement(strQuery);
            rs = stStatement.executeQuery(strQuery);
            boolStatus = rs.next();
            stStatement.close();
            rs.close();
        } catch(SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try{
                stStatement.close();
                rs.close();
            } catch(SQLException | NullPointerException ex){
            }
        }
        return boolStatus;
    }
    
    
    public boolean mCreateRecord(String strQuery) {
        Statement stStatement = null;
        try{
            stStatement = mConnectToDatabase().prepareStatement(strQuery);
            stStatement.execute(strQuery);
            stStatement.close();
            return true;
        } catch(SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try{
                stStatement.close();
            } catch(SQLException | NullPointerException ex){
            }
        }
        return false;
    }
         
    public Long mGetNumericField(String strQuery) {
        Statement stStatement = null;
        ResultSet rs = null;
        try{
            stStatement = mConnectToDatabase().prepareStatement(strQuery);
            rs = stStatement.executeQuery(strQuery);
            while(rs.next()){
                return rs.getLong(1);
            }
            stStatement.close();
            rs.close();
        } catch(SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }   finally {
             try{
                    stStatement.close();
                    rs.close();
                } catch(SQLException | NullPointerException ex) {
                }
            }
        return 0L;
    }
    
    public String mGetTextField(String strQuery) {
        Statement stStatement = null;
        ResultSet rs = null;
        try {
            stStatement = mConnectToDatabase().prepareStatement(strQuery);
            rs = stStatement.executeQuery(strQuery);
            while(rs.next()){
                return rs.getString(1);
            }
            stStatement.close();
            rs.close();
        } catch(SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            try{
                stStatement.close();
                rs.close();
            } catch(SQLException | NullPointerException ex) {
            }
        }
        return null;
    }
          
    public String[] mFetchRecord(String strQuery) {
        String[] arrRecordDetails = null;
        try {
            try (Statement stStatement = mConnectToDatabase().prepareStatement(strQuery)) {
                stStatement.execute(strQuery);
                try (ResultSet rs = stStatement.getResultSet()) {
                    ResultSetMetaData rsmt = rs.getMetaData();
                    arrRecordDetails = new String[rsmt.getColumnCount()+1];
                    while(rs.next()) {
                        for(int i = 1; i < arrRecordDetails.length; i++){
                            arrRecordDetails[i] = String.valueOf(rs.getString(i));                    
                        }
                    }
                    stStatement.close();
                    rs.close();
                }
                arrRecordDetails = new clsModelAndDataMethods().mRemoveEmptyIndexes(arrRecordDetails).toArray(
                        new String[new clsModelAndDataMethods().mRemoveEmptyIndexes(
                                        arrRecordDetails).size()]);
                return arrRecordDetails;
            }
	} catch(SQLException | NullPointerException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return arrRecordDetails;
    }
    
    public boolean mUpdateRecord(String strQuery) {
        Statement stStatement = null;
         try {
             stStatement = mConnectToDatabase().prepareStatement(strQuery);
             stStatement.executeUpdate(strQuery);
             stStatement.close();
             return true;
         } catch(SQLException | NullPointerException ex) {
             JOptionPane.showMessageDialog(null, ex.getMessage(), 
                     "Error while updating details", JOptionPane.ERROR_MESSAGE);
         } finally {
             try{
                 stStatement.close();
             }catch(SQLException | NullPointerException ex){
             }
         }
        return false;
    }
    
    public boolean mDeleteRecord(String strQuery) {
        try(Statement stStatement = mConnectToDatabase().prepareStatement(strQuery)) {
            stStatement.execute(strQuery);
            stStatement.close();
            return true;
        } catch(HeadlessException | SQLException | NullPointerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), 
                     "Error while deleting details", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}