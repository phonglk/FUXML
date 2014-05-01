/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author NoName
 */
public class Utilities {
    
    public static Connection getConnection(String cnnStr,String username,String password){
        Connection cnn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cnn =  DriverManager.getConnection(cnnStr, username, password);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return cnn;
    }
}
