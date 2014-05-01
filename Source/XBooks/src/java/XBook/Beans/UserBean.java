/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Beans;

import XBook.Commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author PhongLK
 */
public class UserBean {
    private String id;
    private String username;
    private String email;
    private boolean isAdmin;
    private String FullName;
    private String Address;
    private String Phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserBean(String id, String username, String email, boolean isAdmin, String FullName, String Address, String Phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.FullName = FullName;
        this.Address = Address;
        this.Phone = Phone;
    }

    public UserBean() {
        
    }

    public UserBean(String FullName, String Address, String Phone) {
        this.FullName = FullName;
        this.Address = Address;
        this.Phone = Phone;
        this.isAdmin = false;
    }



    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static UserBean login(String username,String password){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("select * from users where Username = ? and Password = ?");
            stm.setString(1, username);
            stm.setString(2, password);
            rs = stm.executeQuery();
            if(rs.next()){
                return new UserBean(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getBoolean("isAdmin"),
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("Phone")
                );
            }else{
                return null;
            }
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
    public static UserBean register(String username,String password,String email){
        return register(username, password, email, false, "", "", "");
    }
    public static UserBean register(String username,String password,String email,boolean isAdmin,String fullName,String address,String phone){
        Connection cnn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO users VALUES ( ?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            
            stm.setString(1, null);
            stm.setString(2, username);
            stm.setString(3, password);
            stm.setString(4, email);
            stm.setBoolean(5, isAdmin);
            stm.setString(6, fullName);
            stm.setString(7, address);
            stm.setString(8, phone);
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if(rs.next()){
                String id = rs.getInt(1)+"";
                return new UserBean(
                    id, 
                    username,
                    email,
                    isAdmin,
                    fullName,
                    address,
                    phone
                );
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
        return null;
    }
    public static boolean checkUserExist(String username){
        Connection cnn = null;
        PreparedStatement stm = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("SELECT * FROM users WHERE users.Username like ?");
            stm.setString(1, username);
            if(stm.executeQuery().next()){
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
    public static boolean checkEmailExist(String email){
        Connection cnn = null;
        PreparedStatement stm = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("SELECT * FROM users WHERE users.Email like ?");
            stm.setString(1, email);
            if(stm.executeQuery().next()){
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

    public static boolean updateUser(String id,String fullName,String address,String phone){
        Connection cnn = null;
        PreparedStatement stm = null;
        try{
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("UPDATE users SET FullName = ?, Address = ?, Phone = ? WHERE Id = ?",Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, fullName);
            stm.setString(2, address);
            stm.setString(3, phone);
            stm.setString(4, id);
            
            if(stm.executeUpdate() > -1){
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
