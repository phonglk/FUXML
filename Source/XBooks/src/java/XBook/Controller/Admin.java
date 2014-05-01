/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Controller;

import XBook.Beans.AuthorBean;
import XBook.Beans.BooksBean;
import XBook.Beans.CategoryBean;
import XBook.Beans.PublisherBean;
import XBook.Commons.Utilities;
import XBook.JaxB.Books.Authors;
import XBook.JaxB.Books.Authors.Author;
import XBook.JaxB.Books.Book;
import XBook.JaxB.Books.Books;
import XBook.JaxB.Books.Category;
import XBook.JaxB.Books.Publisher;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.batik.gvt.text.ArabicTextHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author NoName
 */
public class Admin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        try {
            ServletContext context = getServletContext();
            String realContextPath = context.getRealPath("/");
            
            if ("getAuthor".equalsIgnoreCase(action)) {
                Authors authors = new Authors();
                authors = AuthorBean.getAllAuthor();
                response.setContentType("text/xml;charset=UTF-8");
                Utilities.responeResult(response.getWriter(), authors);
            } else if ("getPublisher".equalsIgnoreCase(action)) {
                List<Publisher> publishers = new ArrayList<Publisher>();
                publishers = PublisherBean.getAllAuthor();
                response.setContentType("text/xml;charset=UTF-8");
                Utilities.responeResult(response.getWriter(), publishers);
            } else if ("getCategory".equalsIgnoreCase(action)) {
                List<Category> categories = new ArrayList<Category>();
                categories = CategoryBean.getAllCategory();
                response.setContentType("text/xml;charset=UTF-8");
                Utilities.responeResult(response.getWriter(), categories);
            }else if ("addAuthor".equalsIgnoreCase(action)) {
                String authorName = request.getParameter("txtAuthorName");
                String description = request.getParameter("txtAreaDescription");
                Author author = new Author();
                author.setName(authorName);
                author.setDescription(description);
                response.setContentType("text/xml;charset=UTF-8");
                Utilities.responeResult(response.getWriter(), AuthorBean.insertAuthor(author));
            }else if ("addCategory".equalsIgnoreCase(action)) {
                String authorName = request.getParameter("txtCategoryName");
                String description = request.getParameter("txtAreaDescription");
                Category category = new Category();
                category.setName(authorName);
                category.setDescription(description);
                response.setContentType("text/xml;charset=UTF-8");
                Utilities.responeResult(response.getWriter(), CategoryBean.insertCategory(category));
            }else if ("addPublisher".equalsIgnoreCase(action)) {
                String authorName = request.getParameter("txtPublisherName");
                String description = request.getParameter("txtAreaDescription");
                Publisher publisher = new Publisher();
                publisher.setName(authorName);
                publisher.setDescription(description);

                response.setContentType("text/xml;charset=UTF-8");
                
                Utilities.responeResult(response.getWriter(), PublisherBean.insertCategory(publisher));
            }else if ("delBook".equalsIgnoreCase(action)) {
                String id = request.getParameter("txtId");
                response.setContentType("text/xml;charset=UTF-8");
                
                Utilities.responeResult(response.getWriter(), BooksBean.delBook(id));
                
                String realPath = realContextPath + "WEB-INF/XBooks.xml";
                Books book = BooksBean.getAllJAXBBooks();
                Utilities.buildBooksXML(book, realPath);
                
            }else if ("editBook".equalsIgnoreCase(action)) {
                String id = request.getParameter("txtId");
                String title = request.getParameter("txtTitle");
                String isbn = request.getParameter("txtISBN");
                String[] authors = request.getParameterValues("selectAuthor");
                String category = request.getParameter("selectCategory");
                String publisher = request.getParameter("selectPublisher");
                String description = request.getParameter("txtAreaDescription");
                String price = request.getParameter("txtPrice");
                
                response.setContentType("text/xml;charset=UTF-8");

//                Utilities.responeResult(response.getWriter(), );
                BooksBean.updateBook(Integer.parseInt(id), isbn, title, Integer.parseInt(category), Float.parseFloat(price), Integer.parseInt(publisher), description, authors);
                String realPath = realContextPath + "WEB-INF/XBooks.xml";
                Books book = BooksBean.getAllJAXBBooks();
                Utilities.buildBooksXML(book, realPath);
                response.sendRedirect("Admin");
                return;
            }else if (isMultipart) {
                String isbn = "", title = "", description = "", imgUrl = "";
                int cateId = 0, pubId = 0;
                ArrayList<Integer> authorId = new ArrayList<Integer>();
                float price = 0;
                Book book = null;
                //Upload and get file path on serve
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                try {
                    List items = upload.parseRequest(request);
                    Iterator iterator = items.iterator();
                    while (iterator.hasNext()) {
                        FileItem item = (FileItem) iterator.next();

                        if (!item.isFormField()) {
                            String fileName = item.getName();

                            String root = getServletContext().getRealPath("/");
                            File path = new File(root + "/uploads");
                            if (!path.exists()) {
                                boolean status = path.mkdirs();
                            }

                            File uploadedFile = new File(path + "/" + fileName);
                            imgUrl = "uploads/" + fileName;
                            item.write(uploadedFile);
                        } else {
                            String fieldname = item.getFieldName();
                            String fieldvalue = item.getString();
                            if (fieldname.equalsIgnoreCase("txtISBN")) {
                                isbn = fieldvalue;
                            } else if (fieldname.equalsIgnoreCase("txtTitle")) {
                                title = fieldvalue;
                            } else if (fieldname.equalsIgnoreCase("txtAreaDescription")) {
                                description = fieldvalue;
                            } else if (fieldname.equalsIgnoreCase("selectCategory")) {
                                cateId = Integer.parseInt(fieldvalue);
                            } else if (fieldname.equalsIgnoreCase("selectAuthor")) {
                                authorId.add(Integer.parseInt(fieldvalue));
                            } else if (fieldname.equalsIgnoreCase("selectPublisher")) {
                                pubId = Integer.parseInt(fieldvalue);
                            } else if (fieldname.equalsIgnoreCase("txtPrice")) {
                                price = Float.parseFloat(fieldvalue);
                            }
                        }
                    }
                    //Add book
                    book = BooksBean.addBook(isbn, title, imgUrl, cateId, price, pubId, description, authorId);
                    String filePath = realContextPath + "WEB-INF/XBooks.xml";
                    Utilities.appendBook(book, filePath);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (book == null) {
//                    Utilities.responeResult(out, false);
                } else {
//                    Utilities.responeResult(out, true);
                }
                response.sendRedirect("Admin");
                return;
            }else{
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/admin/index.jsp");
            rd.forward(request, response);
            }
        } finally {
            response.sendRedirect("Admin");
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
