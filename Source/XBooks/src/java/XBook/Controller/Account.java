/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Controller;

import XBook.Beans.UserBean;
import XBook.Commons.Constants;
import XBook.Commons.Utilities;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author NoName
 */
public class Account extends HttpServlet {
   
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
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            if(action.equals("login")){
                String username = request.getParameter("txtUsername");
                String password = request.getParameter("txtPassword");

                UserBean user = null;

                user = UserBean.login(username, password);
                if(user == null){
                        Utilities.responeResult(response.getWriter(), false);
                }else{
                    session.setAttribute(Constants.SESSION_USER, user);
                    Utilities.responeResult(response.getWriter(), user);
                }
            }else if(action.equals("register")){
                String username = request.getParameter("txtUsername");
                String password = request.getParameter("txtPassword");
                String address = request.getParameter("txtAddress");
                String phone = request.getParameter("txtPhone");
                String fullname = request.getParameter("txtFullName");
                String email = request.getParameter("txtEmail");

                UserBean user = null;
                user = UserBean.register(username, password, email, false, fullname, address, phone);

                if(user == null){
                        Utilities.responeResult(response.getWriter(), false);
                }else{
                    session.setAttribute(Constants.SESSION_USER, user);
                    Utilities.responeResult(response.getWriter(), user);
                }
            }
            if(action.equals("logout")){
                session.invalidate();
                Utilities.responeResult(response.getWriter(), true);
            }
            if(action.equals("checkUserExist")){
                String username = request.getParameter("txtUsername");
                if(UserBean.checkUserExist(username)){
                    Utilities.responeResult(response.getWriter(), true);
                }else{
                    Utilities.responeResult(response.getWriter(), false);
                }
            }
            if(action.equals("checkEmailExist")){
                String email = request.getParameter("txtEmail");
                if(UserBean.checkEmailExist(email)){
                    Utilities.responeResult(response.getWriter(), true);
                }else{
                    Utilities.responeResult(response.getWriter(), false);
                }
            }
        } finally { 
            out.close();
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
    @Override
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
