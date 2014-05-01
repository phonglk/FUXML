/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.WebService;

import XBook.Commons.Utilities;
import XBook.JaxB.Order.Order;
import XBook.JaxB.Order.Order.OrderDetails;
import XBook.JaxB.Order.OrderDetail;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author NoName
 */

@Path("xbook")
public class ProvideOrder {
    @Context
    private UriInfo context;

    /** Creates a new instance of ProvideOrder */
    public ProvideOrder() {
    }

    /**
     * Retrieves representation of an instance of XBook.WebService.ProvideOrder
     * @return an instance of java.lang.String
     */
    @Path("/getOrder")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Order getOrder(@DefaultValue("") @QueryParam("code") String code,@DefaultValue("") @QueryParam("contactName") String contactName){
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        Order ord = null;
        try {
            con = Utilities.getConnection("jdbc:mysql://localhost:3306/xm_books", "root", "");
            stm = con.prepareStatement("SELECT b.orderID, Title, Price, b.Quantity, b.Id, b.ContactName, b.Address, b.Phone, b.Created, b.Total FROM books RIGHT JOIN"
                    + "(SELECT a.Id As orderID, o.Id, o.BookId, o.Quantity, a.ContactName, a.Address, a.Phone, a.Created, a.Total FROM orderdetail o RIGHT JOIN"
                    + "(SELECT * FROM orders WHERE Code = ? AND ContactName = ?) a ON a.Id = o.OrderId) b ON b.BookId = books.Id");
            stm.setString(1, code);
            stm.setString(2, contactName);
             
            rs = stm.executeQuery();
            ord = new Order();
            OrderDetails orderDetails = new OrderDetails();
            while (rs.next()) {
                OrderDetail books = new OrderDetail();
                books.setTitle(rs.getString("Title"));
                books.setPrice(rs.getBigDecimal("Price"));
                books.setQuantity(BigInteger.valueOf(rs.getInt("Quantity")));
                books.setID(rs.getInt("Id"));

                orderDetails.getOrderDetail().add(books);

                String date = rs.getDate("Created").toString();
                ord.setOrderID(rs.getString("orderID"));
                ord.setCreated(date);
                ord.setContactName(rs.getString("ContactName"));
                ord.setAddress(rs.getString("Address"));
                ord.setPhone(rs.getString("Phone"));
                ord.setTotal(rs.getBigDecimal("Total"));
                ord.setCode(rs.getString("Code")); 

                ord.setOrderDetails(orderDetails);
            } 
            return ord;
        } catch (Exception e) {
        }
        return ord;
    }

    /**
     * PUT method for updating or creating an instance of ProvideOrder
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
