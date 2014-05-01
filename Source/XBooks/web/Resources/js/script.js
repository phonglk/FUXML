UserModel={
    isLogged:mvvm.changeable(_User.username==""?false:true),
    username:mvvm.changeable(_User.username),
    setUsername:function(username){
        if(username=="")this.isLogged(false);
        else this.isLogged(true);
        this.username(username);
    },
    id:null,
    fullName:_User.fullName,
    phone:_User.phone,
    address:_User.address
}
XSL={
    searchPage:null
}
PageModal={
    Register:{
        enterOptional:mvvm.changeable(false) 
    }
}
function init(){
    applyBindings(UserModel,$(".user-area"));
    applyBindings(UserModel.username,$("[data-bind*='username']"));
    applyBindings(PageModal.Register.enterOptional,$("#register.modal .optional"));
    console.log("Init");
    $$("section .item-container .item").forEach(function(e,i){
        setTimeout(function(){e.addClass("show")},100*i);
    })
    // Action when click on item info
    forEach.call($$("section .item-container .item .info"),function(e){
        e.addEventListener("click", function(mousee){
            go("book/"+e.parent(".item").find("[data-id]").data("id"));
        });
    })
    
    ajaxLogin();
    ajaxRegister();
    ajaxLogout();
    ajaxSearch();
    ajaxCheckout();
    ajaxViewOrder();
    loadXLSDoc();
    preventInputNumber();
    Order.loadFromStorage();
    ac.trigger();
    
}
function showBookDetail(id){
    var jsonData=JSON.parse($(".item-container .item .data[data-id='"+id+"']").data("json"));
    var target = $("#book-detail");
    applyBindings(jsonData,target);
    modal(target);
}
function addToCart(data){
    console.log(data);
    return false;
}
function ajaxLogin(){
    $("#login.modal form").addEventListener("submit", function(e){
        onBusy(true);
        var username = $('#login [name=txtUsername]').value;
        new ajax({
            type:"POST",
            url:"Account",
            queryString:$('#login').findAll("[name]").toFormData(),
            callback:function(data){
                if(xpath(data,"/","string").result!="false"){
                    modal_close($("#login.modal"));
                    UserModel.setUsername(username);
                    UserModel.id=xpath(data,"//user/id","number").result;
                    UserModel.fullName=xpath(data,"//user/fullname","string").result;
                    UserModel.phone=xpath(data,"//user/phone","string").result;
                    UserModel.address=xpath(data,"//user/address","string").result;
                    if(xpath(data,"//user/isadmin","string").result=="true"){
                        if(!$(".admin-menu")){
                            var adminMenu = document.createElement("li");
                            adminMenu.className = "admin-menu";
                            adminMenu.innerHTML = "<a href='Admin'>Admin CP</a>";
                            $("nav ul").appendChild(adminMenu);

                        }
                    }
                }else{
                    $("#login.modal .error").text("*Your login is failed").result;
                }
                go("!");
                onBusy(false)
            }
        })
        e.preventDefault();
        return false;
    })
}
function ajaxRegister(){
    $("#register.modal .option-check").addEventListener("change", function(e){
        PageModal.Register.enterOptional(!PageModal.Register.enterOptional());
        $("#register.modal.show").style.top="-webkit-calc((100% - "+$("#register.modal.show").offsetHeight+"px )/2)"
    })
    $("#register.modal [name=txtPassword]").addEventListener("change", function(e){
        var pat = $("#register.modal [name=txtPassword]").value.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
        $("#register.modal [name=txtRePassword]").attr("pattern","^"+pat+"$")
    })
    $("#register.modal form").addEventListener("submit", function(e){
        onBusy(true);
        var username = $('#register [name=txtUsername]').value;
        new ajax({
            type:"POST",url:"Account",queryString:"action=checkUserExist&txtUsername="+username
            ,callback:function(result){
                onBusy(false)
                if(result.find("result").text().toBoolean()==false){
                    new ajax({
                        type:"POST",url:"Account",queryString:"action=checkEmailExist&txtEmail="+$("#register [name=txtEmail]").value
                        ,callback:function(result){
                            onBusy(false)
                            if(result.find("result").text().toBoolean()==false){
                                new ajax({
                                type:"POST",
                                url:"Account",
                                queryString:$('#register').findAll("[name]").toFormData(),
                                callback:function(data){
                                     if(xpath(data,"/","string").result!="false"){
                                        modal_close($("#register.modal"));
                                        UserModel.setUsername(username);
                                        UserModel.id=xpath(data,"//user/id","number").result;
                                        UserModel.fullName=xpath(data,"//user/fullname","string").result;
                                        UserModel.phone=xpath(data,"//user/phone","string").result
                                        UserModel.address=xpath(data,"//user/address","string").result
                                    }else{
                                        $("#register.modal .error").text("Unexpected Error");
                                    }
                                    onBusy(false)
                                }
                            })
                            }else{
                                $("#register.modal .error").text("Email is duplicated");
                                $("#register.modal [name=txtEmail]").value="";
                                $("#register.modal [name=txtEmail]").focus();
                            }
                        }
                    })
                }else{
                    $("#register.modal .error").text("Username is duplicated");
                    $("#register.modal [name=txtUsername]").value="";
                    $("#register.modal [name=txtUsername]").focus();
                }
            }
        })
        
        e.preventDefault();
        return false;
    })
}
function ajaxLogout(){
    $("#logout").addEventListener("click", function(){
        new ajax({
            type:"POST",
            url:"Account",
            queryString:"action=logout",
            callback:function(data){
                test= data;
                if(data.find("result").text().toBoolean()==true){
                    modal_close($("#login.modal"));
                    UserModel.setUsername("");
                    UserModel.address = UserModel.phone = UserModel.fullName = "";
                    if($(".admin-menu"))$("nav ul").removeChild($(".admin-menu"));
                }
                onBusy(false)
            }
        })
    })
}
function loadXLSDoc(callback){
    new ajax({
        url:"XSL/SearchItems.xsl",
        callback:function(xml){
            XSL.searchPage = xml;
            if(callback)callback.call();
        }
    })
}
function ajaxSearch(){
    $(".search-area form").addEventListener("submit", function(e){
        e.preventDefault();
        go("search/"+$('.search-area form').find("[name=txtTitle]").value);
        return true;
    })
}
function ajaxCheckout(){
    applyBindings(Order, $("#checkoutPage"));
    var popup = $("#confirm-information")
    function checkUserInformationChange(){
        if(UserModel.isLogged()==true){
            if(popup.find('[name="txtContactName"]').value != UserModel.fullName ||
               popup.find('[name="txtPhone"]').value != UserModel.phone ||
               popup.find('[name="txtAddress"]').value != UserModel.address){
               if(confirm("We notice that your delivery information are different to your information\nDo you want to update these information to your account permanently")){
                   var qryStr = "action=updateUser&"+popup.findAll('[name="txtContactName"],[name="txtPhone"],[name="txtAddress"]').toFormData();
                   console.log(qryStr);
                   new ajax({
                        type:"POST",
                        url:"User",
                        queryString: qryStr,
                        callback:function(data){
                            if(xpath(data,"/","string").result!="false"){
                                UserModel.fullName = popup.find('[name="txtContactName"]').value;
                                UserModel.phone = popup.find('[name="txtPhone"]').value;
                                UserModel.address = popup.find('[name="txtAddress"]').value;
                            }else{
                                alert("Sorry, there is some error on server so your information cannot be updated.")
                            }
                        }
                   })
               }
           }
        }
    }
    $(".btn.check-out").addEventListener("click", function(){
        if(ac.lastHash == "checkout")go("!");
        go("checkout");
    })
    $(".btn.confirm-checkout").addEventListener("click", function(){
        go("confirm-infors");
        modal($("#confirm-information"))
        popup.find('[name="txtContactName"]').value = UserModel.fullName;
        popup.find('[name="txtPhone"]').value = UserModel.phone;
        popup.find('[name="txtAddress"]').value = UserModel.address;
    })
    // When user confirms delivery information to complete checkout
    $("#confirm-information form").addEventListener("submit", function(evt){
        onBusy(true)
        checkUserInformationChange();
        popup.find('[name="stringXmlCart"]').value = Order.toXML();
        popup.find('[name="txtTotal"]').value = Order.totalPrice();
        new ajax({
            type:"POST",
            url:"Controller",
            queryString:$("#confirm-information form").findAll("[name]").toFormData(),
            callback:function(data){
                Order.empty();
                onBusy(false);
                var orderId = xpath(data, "/", "string").result;
                $(".pdf-link").attr("href","Controller?orderId="+orderId+"&action=ViewPDF");
                modal($("#order-success"));
            }
        })
        evt.preventDefault();
        return false;
    })
}
function ajaxViewOrder(){
    $("#view-order form").addEventListener("submit",function(evt){
        onBusy(true)
        $("#view-order form [name='code']").value = $("#view-order form [name='code']").value.toString().toUpperCase();
        new ajax({
            type:"GET",
            url:"/resources/xbook/getOrder?"+$("#view-order form").findAll("[name]").toFormData(),
            queryString:"",
            callback:function(data){
                Order.empty();
                onBusy(false);
                go("!")
                var orderId = xpath(data, "*[local-name()='Order']/*[local-name()='OrderID']", "string").result;
                if(orderId!=""){
                    window.open("/XBooks/Controller?orderId="+orderId+"&action=ViewPDF")
                }else{
                    alert("Sorry your combination of your contact name and your code is not valid");
                }

            }
        })
        evt.preventDefault();
        return false;
    })
}
// Address controller
ac.add("book",function(id){
    $("#book-detail .quantity").value=1
    showBookDetail(id);
})
ac.add("checkout",function(){
    if(Order.books().length==0){
        alert("Please have at least one item in your cart to checkout");
        go("!");
    }else{
        modal($("#checkoutPage"));
    }
})
ac.add("confirm-infors",function(){
    if(this.lastHash=="")go("checkout");
})
ac.add("!",function(id){
    modal_closeAll();
})
ac.add("",function(id){
    modal_closeAll();
})
ac.add("login",function(){
    modal($("#login"));
})
ac.add("register",function(){
    modal($("#register"));
})
ac.add("view-order",function(){
    modal($("#view-order"));
})
ac.add("search",function(title){
    function refineHTML(){
        $$("#searchPage .search-result").forEach(function(e){
            var id = e.find("[data-id]").data("id");
            e.parent("a").attr("href","#book/"+id);
        })
    }
    onBusy(true);
    new ajax({
            type:"POST",
            url:"Controller",
            queryString:$('.search-area form').findAll("[name]").toFormData(),
            callback:function(data){
                var xmlResult = data;
                if(xpath(xmlResult,"//Book","string").result==""){
                    alert("Sorry, we do not found any title match with your input :(");
                    onBusy(false);
                    go("");
                }else{
                    loadXLSDoc(function(){
                        var xsltProcessor=new XSLTProcessor();
                            xsltProcessor.importStylesheet(XSL.searchPage);
                        var resultDocument = xsltProcessor.transformToFragment(xmlResult,document);
                        try{
                            $("#searchPage .content").innerHTML = "";
                            $("#searchPage .content").appendChild(resultDocument);
                            refineHTML();
                            onBusy(false);
                            modal($("#searchPage"));
                        }catch(e){
                           alert("Sorry, we do not found any title match with your input :(");
                           onBusy(false);
                           go("");
                        }
                    })
                }
            }
        })
    
})
onLoad(init);