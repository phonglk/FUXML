/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Controller;

import XBook.Beans.OrderBean;
import XBook.Beans.UserBean;
import XBook.Commons.Utilities;
import XBook.JaxB.Cart.Cart;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import XBook.Commons.Constants;
//import XBook.Generate.Order;
import XBook.JaxB.Orders.Order;
//import XBook.JaxB.Orders.Orders;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

/**
 *
 * @author PhongLK
 */
public class Controller extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession();
        try {
            ServletContext context = getServletContext();
            String realContextPath = context.getRealPath("/");
            String realPath = realContextPath + "WEB-INF\\XBooks.xml";

            if (action.equals("getHome")) {
                PrintWriter out = response.getWriter();
                Document doc = Utilities.parserXMLToDom(realPath);

                StringWriter stw = new StringWriter();
                Transformer tf = TransformerFactory.newInstance().newTransformer();
                tf.transform(new DOMSource(doc), new StreamResult(stw));

                out.write("<result>");
                out.write(stw.toString());
                out.write("</result>");
            }
            if (action.equals("search")) {

                Document doc = Utilities.parserXMLToDom(realPath);

                String title = request.getParameter("txtTitle");
                XPathFactory xpf = XPathFactory.newInstance();
                XPath xpath = xpf.newXPath();
                String exp = "//*[local-name()='Title' and contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + title.toLowerCase() + "')]/..";

                NodeList nodeList = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);

                XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(response.getWriter());
                xsw.writeStartDocument();
                xsw.writeStartElement("result");
                xsw.writeStartElement("Books");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node currentNode = nodeList.item(i);//Book
                    xsw.writeStartElement(currentNode.getNodeName());
                    NodeList chilOfCur = currentNode.getChildNodes();//Get all element in Book
                    for (int j = 0; j < chilOfCur.getLength(); j++) {
                        Node childOfChild = chilOfCur.item(j);
                        //Node have no element inside
                        if (childOfChild.getNodeName().equalsIgnoreCase("Id") || childOfChild.getNodeName().equalsIgnoreCase("ISBN") || childOfChild.getNodeName().equalsIgnoreCase("Title") || childOfChild.getNodeName().equalsIgnoreCase("Price") || childOfChild.getNodeName().equalsIgnoreCase("Description") || childOfChild.getNodeName().equalsIgnoreCase("ImageUrl")) {
                            xsw.writeStartElement(childOfChild.getNodeName());
                            xsw.writeCharacters(childOfChild.getTextContent());
                            xsw.writeEndElement();
                        } else if (childOfChild.getNodeName().equalsIgnoreCase("Publisher") || childOfChild.getNodeName().equalsIgnoreCase("Category")) {
                            xsw.writeStartElement(childOfChild.getNodeName());//Node have element inside
                            NodeList pubOrCat = childOfChild.getChildNodes();
                            for (int k = 0; k < pubOrCat.getLength(); k++) {
                                if (pubOrCat.item(k).getNodeName().equalsIgnoreCase("Name") || pubOrCat.item(k).getNodeName().equalsIgnoreCase("Des")) {
                                    xsw.writeStartElement(pubOrCat.item(k).getNodeName());
                                    xsw.writeCharacters(pubOrCat.item(k).getTextContent());
                                    xsw.writeEndElement();
                                }
                            }
                            xsw.writeEndElement();
                        } else if (childOfChild.getNodeName().equalsIgnoreCase("Authors")) {
                            xsw.writeStartElement(childOfChild.getNodeName());//Node have element inside
                            NodeList author = childOfChild.getChildNodes();
                            for (int k = 0; k < author.getLength(); k++) {
                                if (author.item(k).getNodeName().equalsIgnoreCase("Author")) {
                                    xsw.writeStartElement(author.item(k).getNodeName());
                                    for (int h = 0; h < author.item(k).getChildNodes().getLength(); h++) {
                                        if (author.item(k).getChildNodes().item(h).getNodeName().equalsIgnoreCase("Name")) {
                                            xsw.writeStartElement(author.item(k).getChildNodes().item(h).getNodeName());
                                            xsw.writeCharacters(author.item(k).getChildNodes().item(h).getTextContent());
                                            xsw.writeEndElement();
                                        }
                                    }
                                    xsw.writeEndElement();
                                }
                            }
                            xsw.writeEndElement();
                        }
                    }
                    xsw.writeEndElement();
                }
                xsw.writeEndElement();
                xsw.writeEndElement();
                xsw.writeEndDocument();
                xsw.close();
            } else if (action.equals("checkOut")) {
                String stringCart = request.getParameter("stringXmlCart");
                //validation xmlCart String by schema
                Cart cart = Utilities.parseXMLToCart(stringCart,realContextPath+"../../src/java/XBook/Schemas/Cart.xsd");
                //
                if(cart.getItem().size() > 0){
                    OrderBean orderBean = new OrderBean();
                    if (session.getAttribute(Constants.SESSION_USER) != null) {
                        UserBean user = (UserBean) session.getAttribute(Constants.SESSION_USER);
                        orderBean.setUserId(user.getId());
                    }
                    orderBean.setAddress(request.getParameter("txtAddress"));
                    orderBean.setContactName(request.getParameter("txtContactName"));
                    orderBean.setPhone(request.getParameter("txtPhone"));
                    orderBean.setTotal(request.getParameter("txtTotal"));
                    orderBean.setCart(cart);

                    int result = orderBean.InsertOrder();
                    if (result != -1) {
                        Utilities.responeResult(response.getWriter(), result);
                    } else {
                        Utilities.responeResult(response.getWriter(), -1);
                    }
                }else{
                    Utilities.responeResult(response.getWriter(), -1);
                }
            } else if (action.equals("ViewPDF")) {
                String orderId = request.getParameter("orderId");
                OrderBean orderBean = new OrderBean();
                Order orders = new Order();
                orders = orderBean.getOrderXMLbyID(orderId);
                String xmlPath = realContextPath + "WEB-INF/OrderDetails.xml";
                Utilities.buildOrdersXML(orders, xmlPath);

                String xslPath = realContextPath + "XSL/OrderDetails.xsl";
                String foPath = realContextPath + "WEB-INF/OrderDetails.fo";
                Utilities.methodTrAx(xslPath, xmlPath, foPath, realContextPath);

                File file = new File(foPath);
                FileInputStream input = new FileInputStream(file);

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                response.setContentType("application/pdf");

                FopFactory ff = FopFactory.newInstance();
                FOUserAgent fua = ff.newFOUserAgent();

                File config = new File(realContextPath + "WEB-INF/Fo/fop.xconf");
                ff.setUserConfig(config);

                Fop fop = ff.newFop(MimeConstants.MIME_PDF, fua, outStream);
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer trans = tff.newTransformer();
                File fo = new File(foPath);
                Source src = new StreamSource(fo);
                Result result = new SAXResult(fop.getDefaultHandler());
                trans.transform(src, result);

                byte[] content = outStream.toByteArray();
                response.setContentLength(content.length);
                response.getOutputStream().write(content);
                response.getOutputStream().flush();
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } finally {
//            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
