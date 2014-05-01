/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package XBook.Commons;

import XBook.Beans.BooksBean;
import XBook.JaxB.Books.Books;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author NoName
 */
public class ContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String realContextPath = context.getRealPath("/");
        String realPath = realContextPath + "WEB-INF/XBooks.xml";
        File booksFile = new File(realPath);
        //Get all book in database via BookBean
        Books book = BooksBean.getAllJAXBBooks();
        //Create file Books xml
        Utilities.buildBooksXML(book, realPath);
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
