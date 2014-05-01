/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Beans;

import XBook.Commons.DBConnection;
import XBook.JaxB.Books.Category;
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
public class CategoryBean {
    public static List<Category> getAllCategory(){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        List<Category> categories = new ArrayList<Category>();
        try{ 
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from categories");
            rs = stm.executeQuery();
            while(rs.next()){
                Category tmpCategory = new Category();
                tmpCategory.setId(rs.getString("id"));
                tmpCategory.setName(rs.getString("Name"));
                tmpCategory.setDescription(rs.getString("Description"));
                categories.add(tmpCategory);
            }
            return categories;
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

    public static Category getCategoryById(int id){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        Category category = new Category();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from categories where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            while(rs.next()){
                category.setId(rs.getString("id"));
                category.setName(rs.getString("Name"));
                category.setDescription(rs.getString("Description"));
            }
            return category;
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
    
    public static boolean insertCategory(Category category){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO categories VALUES ( ?,?,?)",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, null);
            stm.setString(2, category.getName());
            stm.setString(3, category.getDescription());
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
