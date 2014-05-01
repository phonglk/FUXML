/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XBook.Commons;

import XBook.Beans.UserBean;
//import XBook.Generate.Order;
import XBook.JaxB.Books.Authors;
import XBook.JaxB.Books.Book;
import XBook.JaxB.Books.Books;
import XBook.JaxB.Books.Category;
import XBook.JaxB.Books.Publisher;
import XBook.JaxB.Cart.Cart;
import XBook.JaxB.Orders.Order;
//import XBook.JaxB.Orders.Orders;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author NoName
 */
public class Utilities {
    //Marshaller Books
    public static boolean buildBooksXML(Books books, String realPath) {

        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(books.getClass());
            Marshaller mar = ctx.createMarshaller();
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(books, new File(realPath));
            return true;
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //Marshaller Orders
    public static boolean buildOrdersXML(Order orders, String realPath) {
           JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(orders.getClass());
            Marshaller mar = ctx.createMarshaller();
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.marshal(orders, new File(realPath));
            return true;
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //Unmarshaller not used
    public static Books parseXMLToBooks(String realPath) {
        Books books = new Books();
        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(books.getClass());
            Unmarshaller unm = ctx.createUnmarshaller();
            File f = new File(realPath);
            books = (Books) unm.unmarshal(f);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return books;
    }
    //Unmarshaller
    public static Cart parseXMLToCart(String xmlStringCart,String pathXSD){
        Cart cart = new Cart();
        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(cart.getClass());
            Unmarshaller unm = ctx.createUnmarshaller();
            //Set card.xsd to validation for unmarshaller
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(pathXSD));
            Validator validator = schema.newValidator();
            InputSource in = new InputSource(new StringReader(xmlStringCart));
            validator.validate(new SAXSource(in));
            
            cart = (Cart) unm.unmarshal(new StreamSource(new StringReader(xmlStringCart)));
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return cart; 
    }
    //Parse xml to dom
    public static Document parserXMLToDom(String filePath)
            throws ParserConfigurationException, SAXException, IOException {
        
        DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
        //dbf.setNamespaceAware(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(new File(filePath));
        return doc;
    }
    //Write xml
    public static void writeXML(Node node, String filePath)
            throws TransformerConfigurationException, TransformerException {
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer trans = tff.newTransformer();

        Source src = new DOMSource(node);
        File file = new File(filePath);
        Result result = new StreamResult(file);
        
        trans.transform(src, result);
    }
    //respone true false
    public static void responeResult(Writer sw, boolean result) {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            if (result) {
                xsw.writeCharacters("true");
            } else {
                xsw.writeCharacters("false");
            }
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //response user
    public static void responeResult(Writer sw, UserBean result) {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            xsw.writeStartElement("user");
            xsw.writeStartElement("id");
            xsw.writeCharacters(result.getId());
            xsw.writeEndElement();
            xsw.writeStartElement("username");
            xsw.writeCharacters(result.getUsername());
            xsw.writeEndElement();
            xsw.writeStartElement("address");
            xsw.writeCharacters(result.getAddress());
            xsw.writeEndElement();
            xsw.writeStartElement("email");
            xsw.writeCharacters(result.getEmail());
            xsw.writeEndElement();
            xsw.writeStartElement("isadmin");
            xsw.writeCharacters(result.getIsAdmin()+"");
            xsw.writeEndElement();
            xsw.writeStartElement("fullname");
            xsw.writeCharacters(result.getFullName());
            xsw.writeEndElement();
            xsw.writeStartElement("phone");
            xsw.writeCharacters(result.getPhone());
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //respone list author
    public static void responeResult(Writer sw, Authors result) {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            xsw.writeStartElement("authors");
            for(int i=0;i < result.getAuthor().size();i++){
                xsw.writeStartElement("author");

                    xsw.writeStartElement("id");
                    xsw.writeCharacters(result.getAuthor().get(i).getId());
                    xsw.writeEndElement();

                    xsw.writeStartElement("name");
                    xsw.writeCharacters(result.getAuthor().get(i).getName());
                    xsw.writeEndElement();

                    xsw.writeStartElement("description");
                    xsw.writeCharacters(result.getAuthor().get(i).getDescription());
                    xsw.writeEndElement();

                xsw.writeEndElement();
            }
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //response list publisher
    public static void responeResult(Writer sw, List<Publisher> result) {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            xsw.writeStartElement("publishers");
            for(int i=0;i < result.size();i++){
                xsw.writeStartElement("publisher");

                    xsw.writeStartElement("id");
                    xsw.writeCharacters(result.get(i).getId());
                    xsw.writeEndElement();

                    xsw.writeStartElement("name");
                    xsw.writeCharacters(result.get(i).getName());
                    xsw.writeEndElement();

                    xsw.writeStartElement("description");
                    xsw.writeCharacters(result.get(i).getDescription());
                    xsw.writeEndElement();

                xsw.writeEndElement();
            }
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //respone list category
    public static void responeResult(PrintWriter writer, List<Category> categories) {
       try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            xsw.writeStartElement("categories");
            for(int i=0;i < categories.size();i++){
                xsw.writeStartElement("category");

                    xsw.writeStartElement("id");
                    xsw.writeCharacters(categories.get(i).getId());
                    xsw.writeEndElement();

                    xsw.writeStartElement("name");
                    xsw.writeCharacters(categories.get(i).getName());
                    xsw.writeEndElement();

                    xsw.writeStartElement("description");
                    xsw.writeCharacters(categories.get(i).getDescription());
                    xsw.writeEndElement();

                xsw.writeEndElement();
            }
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //respone int result = -1 mean false
    public static void responeResult(PrintWriter writer, int result) {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
            xsw.writeStartDocument();
            xsw.writeStartElement("result");
            if(result != -1){
                xsw.writeCharacters(result+"");
            }else{
                xsw.writeCharacters("-1");
            }
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //transform xsl and xml into fo using TrAx
    public static void methodTrAx (String xslPath, String xmlPath, String output, String path)
            throws TransformerException {
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            StreamSource xsltFile = new StreamSource(xslPath);
            Transformer trans = tff.newTransformer(xsltFile);
            trans.setParameter("pathFile", path);

            StreamSource xmlFile = new StreamSource(xmlPath);
            StreamResult htmlFile = new StreamResult(new FileOutputStream(output));
            trans.transform(xmlFile, htmlFile);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }        catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        }
    }
    //append xml books node
    public static void appendBook(Book book,String filePath) throws TransformerException{
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Element root = document.getDocumentElement();
            
            Element bookElement = document.createElement("Book");
            
            Element bookId = document.createElement("Id");

            bookId.setTextContent(book.getId());
            bookElement.appendChild(bookId);

            Element bookISBN = document.createElement("ISBN");
            bookISBN.setTextContent(book.getISBN());
            bookElement.appendChild(bookISBN);

            Element bookTitle = document.createElement("Title");
            bookTitle.setTextContent(book.getTitle());
            bookElement.appendChild(bookTitle);

            Element bookPublisher = document.createElement("Publisher");
            Element bookPublisherName = document.createElement("Name");
            bookPublisherName.setTextContent(book.getPublisher().getName());
            bookPublisher.appendChild(bookPublisherName);
            bookElement.appendChild(bookPublisher);

            Element bookAuthors = document.createElement("Authors");
            for(int i = 0; i < book.getAuthors().getAuthor().size(); i++){
                Element bookAuthor = document.createElement("Author");
                Element bookAuthorName = document.createElement("Name");
                bookAuthorName.setTextContent(book.getAuthors().getAuthor().get(i).getName());
                bookAuthor.appendChild(bookAuthorName);
                bookAuthors.appendChild(bookAuthor);
            }
            bookElement.appendChild(bookAuthors);

            Element bookCategory = document.createElement("Category");
            Element bookCategoryName = document.createElement("Name");
            bookCategoryName.setTextContent(book.getCategory().getName());
            bookCategory.appendChild(bookCategoryName);
            bookElement.appendChild(bookCategory);

            Element price = document.createElement("Price");
            price.setTextContent(Float.parseFloat(book.getPrice().toString())+"");
            bookElement.appendChild(price);

            Element bookDescription = document.createElement("Description");
            bookDescription.setTextContent(book.getDescription());
            bookElement.appendChild(bookDescription);

            Element bookImgUrl = document.createElement("ImageUrl");
            bookImgUrl.setTextContent(book.getImageUrl());
            bookElement.appendChild(bookImgUrl);

            root.appendChild(bookElement);

            TransformerFactory transform = TransformerFactory.newInstance();
            Transformer trans = transform.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult rs = new StreamResult(filePath);
            
            DOMSource dom = new DOMSource(document);
            trans.transform(dom, rs); 
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

    public static String randomString( int len)
    {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
           sb.append( AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
