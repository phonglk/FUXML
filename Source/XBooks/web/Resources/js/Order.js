function BookOrderModel(json){
    var self = this;
    self.title = json.Title
    self.id = json.Id;
    self.price = json.Price;
    self.imageUrl = json.ImageUrl;
    self.quantity = mvvm.changeable(json.Quantity);
    self.quantity.toJSON = function(){
        return self.quantity();
    }
}
function OrderModel(){
    var localStorage_name = "xbook_cart_data";
    var self = this;
    this.books = mvvm.changeable([]);
    self.totalPriceObservee = {};
    self.totalPrice = mvvm.changeable(function(){
        var rs = 0;
        var books = self.books();
        for(var i=0;i<books.length;i++){
            rs+=parseInt(books[i].quantity()) * parseFloat(books[i].price);
        }
        return parseFloat(rs).toFixed(2);
    },self)
    self.totalPriceObservee = self._returnedObservee;
    self.remove=function(bookid){
        var bs = self.books();
        var idxToDelete = -1;
        bs.forEach(function(book,i){
            if(book.id == bookid){
                idxToDelete = i;
            }
        })
        if(idxToDelete>-1){
            self.books().remove(idxToDelete);
            updateItemBehavior();
            monitoringItems();
            self.saveToStorage();
            if(self.books().length<=7){
                bringCartItemIntoView(bringCartItemIntoView($(".cart-item:first-child")));
            }
        }
    }
    self.prepareOrder=function(json){
        var books = self.books();
        var isBookExists = false;
        var rs = {
            idx:-1,
            bookId:-1,
            result:false
        }
        for(var i=0;i<books.length;i++){
            var bookorder = books[i];
            if(bookorder.id == json.Id){
                var Q = parseInt(bookorder.quantity())+json.Quantity;
                rs.idx = i;
                rs.bookId = bookorder.id
                if(Q <= 10){
                    rs.result = true;
                }else{
                    message("You can not buy more than 10 item per title. Please contact us if you want to buy in bulk.")
                }
                return rs;
            }
        }
        if(books.length<10){
            rs.result = true;
            return rs;
        }else{
            alert("You only can buy 10 title\nPlease call us if you want buy more (0909-090-XBOOK)");
            return rs;
        }
    }
    self.empty=function(){
        while(self.books().length>0)self.books().remove(0);
        self.saveToStorage();
    }
    self.saveToStorage=function(){
        localStorage.setItem(localStorage_name,JSON.stringify(self.books()))
    };
    self.loadFromStorage=function(){
        var data = localStorage.getItem(localStorage_name);
        if(data!=null){
            var storagedOrder = Array.prototype.slice.apply(JSON.parse(data));
            storagedOrder.forEach(function(book){
                var bookJSON = translateProperties(book);
                self.books().push(new BookOrderModel(bookJSON));
            })
            updateItemBehavior();
        }
    }
    self.toXML = function(){
        var bookArray = Array.prototype.slice.apply(JSON.parse(JSON.stringify(Order.books())));
        var XML = "<Cart xmlns='http://xml.netbeans.org/schema/Cart'>";
        bookArray.forEach(function(book){
            XML+="<Item>"
                XML+="<Quantity>"+book.quantity+"</Quantity>"
                XML+="<bookId>"+book.id+"</bookId>"
            XML+="</Item>"
        })
        XML+="</Cart>"
        return XML;
    }
}
var Order = new OrderModel();
function translateProperties(object){
    var obj = {};
    for(name in object){
        if(object.hasOwnProperty(name)){
            obj[name.toTitleCase()] = object[name]
        }
    }
    return obj;
}
function updateItemBehavior(){
    addToolTip();
    itemMouseAction();
    preventInputNumber();
    itemDeleteBinding();
    monitorQuantityChange();
}
function order_core(json){
    var books = Order.books();
    var po = Order.prepareOrder(json);
    if(po.result==true){
        if(po.bookId==-1){
            books.push(new BookOrderModel(json));
            updateItemBehavior();
        }else{
            var book = Order.books()[po.idx];
            var Q = parseInt(book.quantity())+json.Quantity;
            book.quantity(Q);
            Order.totalPriceObservee.notify();
        }
        Order.saveToStorage();
    }
}
function monitorQuantityChange(){
    $$("[data-bind*='quantity']:not([data-monitorQuantity])").forEach(function(e){
        e.addEventListener("change", function(){
            Order.totalPriceObservee.notify();
            Order.saveToStorage();
        })
        e.attr("data-monitorQuantity","true");
    })
}
function monitoringItems(){
    if(ac.lastHash == "checkout" && Order.books().length == 0){
        go("!");
    }
}
function addToolTip(){
    $$(".cart-item").forEach(function(e){
        e.attr("title",e.find(".tooltip").innerText);
    })
}
function itemDeleteBinding(){
    $$(".cart-item").forEach(function(e){
        e.find(".btn.delete").addEventListener("click",function(){
            Order.remove(parseInt(e.find("[data-id]").data("id")));
        })
    })
}
function itemMouseAction(){
    $$(".cart-item").forEach(function(e){
        e.addEventListener("mouseout", function(evt){
            e.find("input").blur();
        })
        e.addEventListener("mouseover", function(evt){
            e.find("input").focus();
        })
    })
}
onLoad(function(){
    applyBindings(Order, $("#cart"));
    applyBindings(Order.totalPrice, $("[data-bind*='totalPrice']"));
    $$(".item-container .item .btn.order").forEach(function(e){
        e.addEventListener("click", function(evt){
            e.parent(".item").removeClass("active");
            var itemData = JSON.parse(e.parent(".item").find(".data").data("json"));
            itemData.Quantity = 1;

            if(Order.prepareOrder(itemData).result==true){
                itemMoveFromGridToCart(e.parent(".item"),function(){
                    order_core(itemData);
                }); // calling animation function
            }
            evt.stopPropagation();
            return false;
        })
    })
    $(".btn.add-cart").addEventListener("click", function(evt){
        var bookId = $("#book-detail [data-id]").data("id");
        var qty = parseInt($("#book-detail .quantity").value);
        
        for(var i=0;i<qty;i++){
            var item = $(".item-container .item [data-id='"+bookId+"']")
            if(item){
                var item = item.parent(".item");
                setTimeout(function(){
                    item.find(".btn.order").click();
                },200)
            }
        }
        go("!");
//        var itemData = JSON.parse($(".item [data-id='"+bookId+"']").data("json"));
//            itemData.Quantity = parseInt($("#book-detail .quantity").value);
//            order_core(itemData);
    })
    $(".btn.clear-cart").addEventListener("click", function(evt){
        if(confirm("Are you sure you want to remove all items of your cart ?")){
            Order.empty();
        }
    })
    var step = 111.4;
    $("#cart .next").addEventListener("click", function(){
        var curPos = parseInt($("#cart .content-container").style.marginLeft);
        if(isNaN(curPos))curPos = 0;
        var length = $$("#cart .content-container .cart-item").length
        var limit = -(length - 6)*step;
        if(curPos>limit){
            $("#cart .content-container").style.marginLeft=(curPos - step)+"px";
        }
    })
    $("#cart .prev").addEventListener("click", function(){
        var curPos = parseInt($("#cart .content-container").style.marginLeft);
        if(isNaN(curPos))curPos = 0;
        if(curPos <= -step){
            $("#cart .content-container").style.marginLeft=(curPos + step)+"px";
        }else{
            if(curPos <=0){
                $("#cart .content-container").style.marginLeft="0px";
            }
        }
    })
})