/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Beans;

import XBook.Commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author NoName
 */
public class CartBean {

    public static boolean insertCartItem(String orderId, String bookId, int quantity){
        Connection cnn = null;
        PreparedStatement stm = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO orderdetail VALUES ( ?,?,?,?)");
            
            stm.setString(1, null);
            stm.setString(2, orderId );
            stm.setString(3, bookId);
            stm.setInt(4, quantity);

            if(stm.executeUpdate() > 0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stm!=null){
                    stm.close();
                }
                if(cnn!=null){
                    cnn.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
