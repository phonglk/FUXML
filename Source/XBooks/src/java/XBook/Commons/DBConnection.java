package XBook.Commons;

//import com.mysql.jdbc.PreparedStatement;
//import com.mysql.jdbc.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String cnnStr = "jdbc:mysql://localhost:3306/xm_books";
    private static String username = "root";
    private static String password = "";
    private static Connection cnn = null;

    public static Connection getConnection(){
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
