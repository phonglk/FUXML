<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:import var="doc" url="WEB-INF/XBooks.xml"/>
<c:import var="HomeBookXSL" url="XSL/HomeBooks.xsl"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="./Resources/css/default.css" >
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <meta http-equiv="content-language" content="vi"/>
        <script>
            _User={
                username:"${sessionScope.USER.username}",
                fullName: "${sessionScope.USER.fullName}",
                address: "${sessionScope.USER.address}",
                phone: "${sessionScope.USER.phone}"
            }
        </script>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/header.jspf" %>
        <div id="cart">
        <div class="page-wrapper">
            <div class="item-container">
                <a href="javascript:void(0)" class="control prev"></a>
                <a href="javascript:void(0)" class="control next"></a>
                <div class="inner-container">
                    <div class="content-container">
                        <div data-bind="books:for-each" style="float: left;">
                            <div class="cart-item">
                                <div data-bind="id:attr(data-id)"></div>
                                <img class="cover" src="" data-bind="imageUrl:attr(src)" />
                                <div class="tooltip" data-tooltip=""><span  data-bind="title"></span>( <span  data-bind="price"></span> $ )</div>
                                <div class="label quantity">
                                    <span data-bind="quantity"></span>
                                </div>
                                <div class="edit">
                                    <div class="price">
                                        <span data-bind="price"></span>$
                                    </div>
                                    <div>X</div>
                                    <div>
                                        <input type="number" min="1" max="10" data-bind="quantity:editable" class="edit-quantity" />
                                    </div>
                                    <div style="height:12px"></div>
                                    <center>
                                        <a href="javascript:void(0)" class="btn error delete">Delete</a>
                                    </center>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="action">
                <span class="label">
                    Total: <span class="total-price" data-bind="totalPrice">0</span>$
                </span>
                <span>
                    &nbsp;&nbsp;
                    <a href="javascript:void(0)" class="btn error clear-cart">Clear Cart</a>
                </span>
                &nbsp;&nbsp;
                <a href="javascript:void(0)" class="btn check-out">Check out</a>
            </div>
        </div>
    </div>
    <div class="line-under-cart"></div>
    <div class="page-wrapper" style="overflow: hidden;">
        <section>
            <div class="item-container">
                <div class="content-container">
                    <x:transform doc="${doc}" xslt="${HomeBookXSL}"/>
                </div>
            </div>
        </section>
        <footer>

        </footer>
    </div>
        
        <%@include file="WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
