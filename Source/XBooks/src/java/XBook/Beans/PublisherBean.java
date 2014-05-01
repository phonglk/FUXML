/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Beans;

import XBook.Commons.DBConnection;
import XBook.JaxB.Books.Publisher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NoName
 */
public class PublisherBean {

    public static List<Publisher> getAllAuthor(){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        List<Publisher> publishers = new ArrayList<Publisher>();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from publishers");
            rs = stm.executeQuery();
            while(rs.next()){
                Publisher tmpPublisher = new Publisher();
                tmpPublisher.setId(rs.getString("id"));
                tmpPublisher.setName(rs.getString("Name"));
                tmpPublisher.setDescription(rs.getString("Description"));
                publishers.add(tmpPublisher);
            } 
            return publishers;
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
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
        return null;
    }

    public static Publisher getPublisherById(int id){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        Publisher publisher = new Publisher();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from publishers where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            while(rs.next()){
                publisher.setId(rs.getString("id"));
                publisher.setName(rs.getString("Name"));
                publisher.setDescription(rs.getString("Description"));
            }
            return publisher;
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
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
        return null;
    }

    public static boolean insertCategory(Publisher publisher){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO publishers VALUES ( ?,?,?)",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, null);
            stm.setString(2, publisher.getName());
            stm.setString(3, publisher.getDescription());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if(rs.next()){
                return true;
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
