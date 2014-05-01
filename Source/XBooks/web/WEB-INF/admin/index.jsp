<%-- 
    Document   : index
    Created on : Mar 27, 2013, 1:21:36 PM
    Author     : NoName
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:import var="doc" url="../../WEB-INF/XBooks.xml"/>
<c:import var="AdminBookXSL" url="../../XSL/AdminBooks.xsl"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="./Resources/css/default.css" >
        <link rel="stylesheet" href="./Resources/css/admin.css" >
        <title>Admin Page</title> 
    </head>
    <body>
        <div class="message">
            <div class="content">
                This is a message
            </div>
            <div class="dismiss">
                <a href="javascript:void(0)">Dismiss</a>
            </div>
        </div>
        <div id="busy"><img src="./Resources/images/waiting-animate.gif"/></div>
        <div id="modal-bg"></div>
        <div class="modal" id="working" data-isModal="true"></div>
        <div class="modal" id="addBook">
            <form action="Admin" method="POST" enctype="multipart/form-data">
                <label class="cover">
                    <div class="converDropIn">
                        <img src="./Resources/images/no-cover.png"/>
                    </div>
                </label>
                <label>
                    Title : <input type="text" name="txtTitle" required/>
                </label>
                    <input type="file" name="txtImgUrl" required class="hidden"/>
                <label>
                    ISBN : <input type="text" name="txtISBN" required/>
                </label>
                <label class="cat">
                    Category : <a href="javascript:void(0)" class="btn add-cat"> + </a> 
                    <select name="selectCategory" class="selectCategory" required></select>
                </label>
                <label class="pub">
                    Publisher : 
                    <a href="javascript:void(0)" class="btn add-pub"> + </a>
                    <select name="selectPublisher" class="selectPublisher" required></select>
                </label>
                <label class="authors">
                    Authors : <select name="selectAuthor" class="selectAuthor" multiple required></select>
                    <a href="javascript:void(0)" class="btn add-auth"> + </a>
                </label>
                <label class="desc">
                    <textarea name="txtAreaDescription" required placeholder="Description"></textarea>
                </label>
                <label class="price">
                    Price : <input type="text" min="1" name="txtPrice" required/>
                </label>
                <input type="hidden" value="addBook" name="action"/>
                <input type="submit" value="Add Book"/>
            </form>
        </div>
        <div class="modal">
            <form action="Admin" method="POST">
                <fieldset>Add Author</fieldset>
                <label>
                    Author Name : <input type="text" name="txtAuthorName"/>
                </label>
                <label>
                    Author Description : <textarea name="txtAreaDescription"></textarea>
                </label>
                <input type="hidden" value="addAuthor" name="action"/>
                <input type="submit" value="Add Author"/>
            </form>
        </div>
        <div class="modal">
            <form action="Admin" method="POST">
                <fieldset>Add Category</fieldset>
                <label>
                    Category Name : <input type="text" name="txtCategoryName"/>
                </label>
                <label>
                    Category Description : <textarea name="txtAreaDescription"></textarea>
                </label>
                <input type="hidden" value="addCategory" name="action"/>
                <input type="submit" value="Add Category"/>
            </form>
        </div> 
        <div class="modal">
            <form action="Admin" method="POST">
                <fieldset>Add Publisher</fieldset>
                <label>
                    Publisher Name : <input type="text" name="txtPublisherName"/>
                </label>
                <label>
                    Publisher Description : <textarea name="txtAreaDescription"></textarea>
                </label>
                <input type="hidden" value="addPublisher" name="action"/>
                <input type="submit" value="Add Publisher"/>
            </form>
        </div>
        <div class="modal"  id="editBook">
        <form action="Admin" method="POST" class="edit-form">
            <label class="cover">
                <div class="converDropIn">
                    <img src="./Resources/images/no-cover.png"/>
                </div>
            </label>
            <label>
                Title : <input type="text" name="txtTitle" required/>
            </label>
            <label>
                ISBN : <input type="text" name="txtISBN" required/>
            </label>
            <label class="cat">
                Category : <a href="javascript:void(0)" class="btn add-cat"> + </a>
                <select name="selectCategory" class="selectCategory" required></select>
            </label>
            <label class="pub">
                Publisher :
                <a href="javascript:void(0)" class="btn add-pub"> + </a>
                <select name="selectPublisher" class="selectPublisher" required></select>
            </label>
            <label class="authors">
                Authors : <select name="selectAuthor" class="selectAuthor" multiple required></select>
                <a href="javascript:void(0)" class="btn add-auth"> + </a>
            </label>
            <label class="desc">
                <textarea name="txtAreaDescription" required placeholder="Description"></textarea>
            </label>
            <label class="price">
                Price : <input type="text" min="1" name="txtPrice" required/>
            </label>
            <input type="hidden" value="editBook" name="action"/>
            <input type="hidden" name="txtId"/>
            <input type="submit" value="Edit Book"/>
        </form>
        </div>
        <div class="page-wrapper" style="overflow: hidden;" id="header"> 
            <h1>Hello Admin</h1>
        </div>
        <div class="page-wrapper" style="overflow: hidden;">
            <section>
                <a href="#addBook" data-modal class="btn">
                    Add book
                </a>
                <div class="item-container">
                    <div class="content-container">
                        <x:transform doc="${doc}" xslt="${AdminBookXSL}"/>
                    </div>
                </div>
            </section>
            <footer>

            </footer>
        </div>
        <%@include file="../../WEB-INF/jspf/admin-footer.jspf" %>
    </body>
</html>
