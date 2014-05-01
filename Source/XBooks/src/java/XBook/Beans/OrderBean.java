/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Beans;

import XBook.Commons.DBConnection;
import XBook.Commons.Utilities;
import XBook.JaxB.Cart.Cart;
import XBook.JaxB.Orders.Order;
import XBook.JaxB.Orders.OrderDetail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NoName
 */
public class OrderBean {

    private String orderId;
    private String created;
    private String contactName;
    private String address;
    private String phone;
    private String total;
    private String userId;
    private Cart cart;
    private static String url = "Resources/images/logo.png";

    public OrderBean(String orderId, String created, String contactName, String address, String phone, String total) {
        this.orderId = orderId;
        this.created = created;
        this.contactName = contactName;
        this.address = address;
        this.phone = phone;
        this.total = total;

    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderBean(String contactName, String address, String phone, String total) {
        this.contactName = contactName;
        this.address = address;
        this.phone = phone;
        this.total = total;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderBean() {
    }

    public OrderBean(String userId, String contactName, String address, String phone, String total, Cart cart) {
        this.userId = userId;
        this.contactName = contactName;
        this.address = address;
        this.phone = phone;
        this.total = total;
        this.cart = cart;
    }

    public int InsertOrder() {
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        try {
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("INSERT INTO orders VALUES ( ?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            stm.setString(1, null);
            stm.setString(2, this.userId);
            stm.setString(3, this.contactName);
            stm.setString(4, this.address);
            stm.setString(5, this.phone);
            stm.setDate(6, sqlDate);
            stm.setFloat(7, Float.parseFloat(this.total));

            String code = "";

            do{
                code = Utilities.randomString(5);
            }while(checkCodeContain(code));

            stm.setString(8, code);
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();

            if (rs.next()) {
                if (cart != null) {
                    for (int i = 0; i < cart.getItem().size(); i++) {
                        CartBean.insertCartItem(rs.getInt(1) + "", cart.getItem().get(i).getBookId(), cart.getItem().get(i).getQuantity());
                    }
                }
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (cnn != null) {
                    cnn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static Order getOrderXMLbyID(String orderId) {
        Connection cnn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;

        try {
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("SELECT Title, Price, b.Quantity, b.Id, b.ContactName, b.Address, b.Phone, b.Created, b.Total, b.Code FROM books RIGHT JOIN"
                    + "(SELECT o.Id, o.BookId, o.Quantity, a.ContactName, a.Address, a.Phone, a.Created, a.Total, a.Code FROM orderdetail o RIGHT JOIN"
                    + "(SELECT * FROM orders WHERE Id = ?) a ON a.Id = o.OrderId) b ON b.BookId = books.Id");
            stm.setString(1, orderId);
            rs = stm.executeQuery();
            Order ord = new Order();
            Order.OrderDetails ordDetails = new Order.OrderDetails();
//            OrderDetail books = new OrderDetail();
//            List<OrderDetail> listBook = new ArrayList<OrderDetail>();
//            Book books = new Book();
            

            while (rs.next()) {
                OrderDetail books = new OrderDetail();
                double total;
                // Calculate Total of each kind of books
                int quantity = rs.getInt("Quantity");
                float price = rs.getFloat("Price");
                total = quantity * price;
                // format decimal number to 2 decimal places
                DecimalFormat df = new DecimalFormat("#.##");

                books.setTitle(rs.getString("Title"));
                books.setPrice(rs.getBigDecimal("Price"));
                books.setQuantity(BigInteger.valueOf(quantity));
                books.setTotalType(BigDecimal.valueOf(Double.parseDouble(df.format(total))));
                books.setID(rs.getInt("Id"));
                ordDetails.getOrderDetail().add(books);

                String date = rs.getDate("Created").toString();
                ord.setImageURL(url);
                ord.setOrderCode(rs.getString("Code"));
                ord.setCreated(date);
                ord.setContactName(rs.getString("ContactName"));
                ord.setAddress(rs.getString("Address"));
                ord.setPhone(rs.getString("Phone"));
                ord.setTotal(rs.getBigDecimal("Total"));
                ord.setOrderDetails(ordDetails);
            }
            return ord;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (cnn != null) {
                    cnn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
     public static boolean checkCodeContain(String code){
        ResultSet rs = null;
        Connection cnn = null;
        PreparedStatement stm = null;
        try {
            cnn = DBConnection.getConnection();
            stm = cnn.prepareStatement("SELECT * FROM orders WHERE Code = ?", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, code);

            if (stm.executeQuery().next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (cnn != null) {
                    cnn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
