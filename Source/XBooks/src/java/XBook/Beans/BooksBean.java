/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Beans;

import XBook.Commons.DBConnection;
import XBook.JaxB.Books.Authors;
import XBook.JaxB.Books.Authors.Author;
import XBook.JaxB.Books.Book;
import XBook.JaxB.Books.Books;
import XBook.JaxB.Books.Category;
import XBook.JaxB.Books.Publisher;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author NoName
 */
public class BooksBean {

    public static boolean checkContains(String isbn,Books books){
        for(int i = 0;i < books.getBook().size();i++){
            if(books.getBook().get(i).getISBN().equals(isbn)){
                return true;
            }
        }
        return false;
    }

    public static Books getAllJAXBBooks() {
        Books result = new Books();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            con = DBConnection.getConnection();
            stm = con.prepareStatement("SELECT boo.id,boo.Title, boo.ISBN, boo.ImageUrl, boo.Price, boo.Description,cat.Name AS Category,cat.Description AS CatDes,pub.Name AS Publisher,pub.Description AS PubDes,aut.Name AS Author,aut.Description AS AutDes FROM books AS boo, categories AS cat,publishers AS pub,bookauthor AS boA,authors AS aut WHERE cat.id = boo.CategoryId AND pub.id = boo.PublisherId AND aut.id = boA.AuthorId AND boA.BookId = boo.Id");
            rs = stm.executeQuery();
            while (rs.next()) {
                if(!checkContains(rs.getString("ISBN"), result)){
                    Book tmpBook = new Book();
                    
                    tmpBook.setId(rs.getString("Id"));
                    tmpBook.setISBN(rs.getString("ISBN"));

                    Authors aus = new Authors();
                    Author au = new Author();
                    au.setName(rs.getString("Author"));
                    au.setDescription(rs.getString("AutDes"));
                    aus.getAuthor().add(au);
                    tmpBook.setAuthors(aus);

                    Category cat = new Category();
                    cat.setName(rs.getString("Category"));
                    cat.setDescription(rs.getString("CatDes"));
                    tmpBook.setCategory(cat);

                    Publisher pub = new Publisher();
                    pub.setName(rs.getString("Publisher"));
                    pub.setDescription(rs.getString("PubDes"));
                    tmpBook.setPublisher(pub);
                    
                    tmpBook.setDescription(rs.getString("Description"));
                    tmpBook.setImageUrl(rs.getString("ImageUrl"));
                    tmpBook.setPrice(rs.getBigDecimal("Price"));
                    tmpBook.setTitle(rs.getString("Title"));

                    result.getBook().add(tmpBook);
                }else{
                    for(int i = 0; i < result.getBook().size(); i++){
                        if(result.getBook().get(i).getISBN().equals(rs.getString("ISBN"))){
                            Author tmpAuthor = new Author();
                            tmpAuthor.setName(rs.getString("Author"));
                            tmpAuthor.setDescription(rs.getString("AutDes"));
                            
                            result.getBook().get(i).getAuthors().getAuthor().add(tmpAuthor);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    public static Book addBook(String isbn,String title,String imgUrl,int cateId,Float price,int pubId,String description,ArrayList<Integer> authorId){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Book book = new Book();
        Authors authors = new Authors();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO books VALUES ( ?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, null);
            stm.setString(2, isbn);
            stm.setString(3, title);
            stm.setString(4, imgUrl);
            stm.setInt(5,cateId );
            stm.setFloat(6, price);
            stm.setInt(7, pubId);
            stm.setString(8, description);
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if(rs.next()){
                String id = rs.getInt(1)+"";
                for(int j = 0; j < authorId.size(); j++){
                    authors.getAuthor().add(AuthorBean.getAuthorById(authorId.get(j)));
                }
                book.setId(id);
                book.setAuthors(authors);
                book.setCategory(CategoryBean.getCategoryById(cateId));
                book.setDescription(description);
                book.setISBN(isbn);
                book.setImageUrl(imgUrl);
                book.setPrice(new BigDecimal(price));
                book.setPublisher(PublisherBean.getPublisherById(pubId));
                book.setTitle(title);

                addBookAuthor(id, authors);

                return book;
            }
            return null;
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
        return null;
    }

    public static boolean updateBook(int id,String isbn,String title,int cateId,Float price,int pubId,String description,String[] authorId){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Book book = new Book();
        Authors authors = new Authors();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("UPDATE books SET ISBN = ?,Title = ?,CategoryId = ?,Price = ?,PublisherId = ?,Description = ? WHERE id = ?",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, isbn);
            stm.setString(2, title);
            stm.setInt(3, cateId);
            stm.setFloat(4, price);
            stm.setInt(5,pubId );
            stm.setString(6, description);
            stm.setInt(7, id);
            
            if(stm.executeUpdate() > -1){
                return true;
            }
            return false;
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

    public static void addBookAuthor(String bookId, Authors authors){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try{
            cnn = DBConnection.getConnection();
            for(int i = 0; i <authors.getAuthor().size(); i++){
                stm = cnn.prepareStatement("INSERT INTO bookauthor VALUES ( ?,?,?)",Statement.RETURN_GENERATED_KEYS);
                stm.setString(1, null);
                stm.setString(2, bookId);
                stm.setString(3, authors.getAuthor().get(i).getId());
                stm.executeUpdate();
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
    }
    
    public static boolean delBook(String id){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Book book = new Book();
        Authors authors = new Authors();
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("DELETE FROM books WHERE id = ?",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, id);
            
            if(stm.executeUpdate() > -1){
                return true;
            }
            return false;
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
