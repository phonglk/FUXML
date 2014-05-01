/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Beans;

import XBook.Commons.DBConnection;
import XBook.JaxB.Books.Authors;
import XBook.JaxB.Books.Authors.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author NoName
 */
public class AuthorBean {
    public static Authors getAllAuthor(){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        Authors authors = new Authors();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from authors");
            rs = stm.executeQuery();
            while(rs.next()){
                Author tmpAuthor = new Author();
                tmpAuthor.setId(rs.getString("id"));
                tmpAuthor.setName(rs.getString("Name"));
                tmpAuthor.setDescription(rs.getString("Description"));
                authors.getAuthor().add(tmpAuthor);
            }
            return authors;
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

    public static Author getAuthorById(int id){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        Author author = new Author();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from authors where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            while(rs.next()){
                author.setId(rs.getString("id"));
                author.setName(rs.getString("Name"));
                author.setDescription(rs.getString("Description"));
            }
            return author;
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

    public static Author getAuthorByIdArray(int[] id){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        Author author = new Author();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("SELECT * FROM authors WHERE id IN ?");
            stm.setObject(1, id);
            rs = stm.executeQuery();
            while(rs.next()){
                author.setId(rs.getString("id"));
                author.setName(rs.getString("Name"));
                author.setDescription(rs.getString("Description"));
            }
            return author;
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

    public static boolean insertAuthor(Author author){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO authors VALUES ( ?,?,?)",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, null);
            stm.setString(2, author.getName());
            stm.setString(3, author.getDescription());
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
