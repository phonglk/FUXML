/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Controller;

import XBook.Beans.UserBean;
import XBook.Commons.Utilities;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import XBook.Commons.Constants;
import org.apache.fop.apps.MimeConstants;

/**
 *
 * @author NoName
 */
public class User extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/xml;charset=UTF-8");
//        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        try {
            if (action.equalsIgnoreCase("ViewOrder")) {
                String path = getServletContext().getRealPath("/");
                String xmlPath = path + "WEB-INF/OrderDetails.xml";
                String xslPath = path + "XSL/OrderDetails.xsl";
                String foPath = path + "WEB-INF/OrderDetails.fo";

                Utilities.methodTrAx(xslPath, xmlPath, foPath, path);

                // Don't know 2 lines of code below to do what
                File file = new File(foPath);
                FileInputStream input = new FileInputStream(file);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.setContentType("application/pdf");

                FopFactory ff = FopFactory.newInstance();
                FOUserAgent fua = ff.newFOUserAgent();

                Fop fop = ff.newFop(MimeConstants.MIME_PDF, fua, out);
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer trans = tff.newTransformer();
                File fo = new File(foPath);
                Source src = new StreamSource(fo);
                Result result = new SAXResult(fop.getDefaultHandler());
                trans.transform(src, result);

                byte[] content = out.toByteArray();
                response.setContentLength(content.length);
                response.getOutputStream().write(content);
                response.getOutputStream().flush();
            } else if ("updateUser".equals(action)) {
                if (session.getAttribute(Constants.SESSION_USER) != null) {
                    String fullname = request.getParameter("txtContactName");
                    String address = request.getParameter("txtAddress");
                    String phone = request.getParameter("txtPhone");
                    UserBean user = (UserBean)session.getAttribute(Constants.SESSION_USER);

                    if(UserBean.updateUser(user.getId(), fullname, address, phone)){
                        user.setFullName(fullname);
                        user.setAddress(address);
                        user.setPhone(phone);

                        session.removeAttribute(Constants.SESSION_USER);
                        session.setAttribute(Constants.SESSION_USER, user);
                        
                        Utilities.responeResult(response.getWriter(), true);
                    }else{
                        Utilities.responeResult(response.getWriter(), false);
                    }
                }else{
                    Utilities.responeResult(response.getWriter(), false); 
                }
            }
        } catch (TransformerException ex) {
            ex.printStackTrace();
        } catch (FOPException ex) {
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
