// Handling cart effect

// Add action for small buy button
onLoad(function(){
    addAnimationForItem();
    bindingDragDropItemEffect();
})
function addAnimationForItem(){
    $$(".item-container .item").forEach(function(item){
        var btnOrder = item.find(".btn.order")
        item.addEventListener("mouseout", function(){
            item.removeClass("active");
        })
        btnOrder.addEventListener("mousedown", function(){
            item.addClass("active");
        })
    })
}
/*
 * void itemMoveFromGridToCart (Node itemNode, function completeCallback)
 * - purpose:   Doing animation that (look like) move item from grid to cart
 * - how:       Clone an image cover(dummy), set it original position of item that being bought
 *              then set it desired position in cart,
 *              animation will automatically do by css3 transition property
 *              after finish animation, remove dummy and call the callback (if exist)
 */
function itemMoveFromGridToCart(itemNode,completeCallback){
    var itemId = itemNode.find("[data-id]").data("id");
    var aniDummy = document.createElement("img");
        aniDummy.src = itemNode.find(".cover").attr("src");
        aniDummy.className="ani-dummy";
    var itemOffset = itemNode.offset();
        aniDummy.style.top = itemOffset.y+"px";
        aniDummy.style.left = itemOffset.x+"px";
    document.body.appendChild(aniDummy);
    // Find place-will-be position;
    var lastOffset,leftWillBe,topWillBe;
    var cartItem = null;
    var testCartItem = $(".cart-item [data-id='"+itemId+"']");
    if(testCartItem)cartItem = testCartItem.parent(".cart-item");
    bringCartItemIntoView(cartItem, function(){
        if(cartItem){
            // If item already be in cart, dummy move to cart-item' place
            var itemOffset =cartItem.offset();
                leftWillBe = itemOffset.x;
                topWillBe = itemOffset.y;
        }else{
            // It is new item
            var lastCartItem = $(".cart-item:last-child");
            if(lastCartItem == null){
                // but if it is first item in cart
                lastOffset = $(".content-container").offset();
                leftWillBe = lastOffset.x
                topWillBe = lastOffset.y
            }else{
                // else it is not first item in cart
                lastOffset = lastCartItem.offset();
                leftWillBe = (lastOffset.x + 20 + 93)
                topWillBe = lastOffset.y
            }
        }
        aniDummy.style.left = leftWillBe+"px";
        aniDummy.style.top = topWillBe+"px";
        aniDummy.style.width = "93px";
        aniDummy.style.height = "132px";
        aniDummy.addEventListener("webkitTransitionEnd", function(evt){
            if(aniDummy.attr("deleted")==null){
                document.body.removeChild(aniDummy);
                aniDummy.attr("deleted","true")
                if(completeCallback){
                    completeCallback.call();
                }
            }
        });
    })
}
function isCartItemOutOfView(cartItem){
    var view = $("#cart .inner-container");
    if(cartItem.offsetLeft >= view.offsetWidth){
        return false
    }
    if(cartItem.offsetLeft < 0){
        return false
    }
    return true
}
function bringCartItemIntoView(cartItem,callback){
    function needLeft(){
        if(cartItem){
            return cartItem.offsetLeft;
        }else{
            if($(".cart-item:last-child") instanceof Node){
                return $(".cart-item:last-child").offsetLeft+93+20;
            }else{
                if(cartItem == null){
                    return parseInt($(".content-container").style.marginLeft);
                }else{
                    return 0;
                }
            }

        }
    }
    var view = $("#cart .inner-container");
    if(typeof callback=="undefined"){
        callback=function(){};
    };
    function next(){
        if(needLeft() >= view.offsetWidth){
            $(".control.next").click()
            return true;
        }
        callback.call();
        return false;
    }
    function nextCaller(){
        setTimeout(function(){
            if(next())nextCaller();
        },201);
    }
    function prev(){
        if(needLeft() < 0){
            $(".control.prev").click()
            return true;
        }
        callback.call();
        return false;
    }
    function prevCaller(){
        setTimeout(function(){
            if(prev())prevCaller();
        },201);
    }
    if(needLeft() >= view.offsetWidth){
        if(next()){
            nextCaller();
        }
    }else if(needLeft() < 0){
        if(prev()){
            prevCaller();
        }
    }else{
        callback.call();
    }
}

function bindingDragDropItemEffect(){ 
    // TODO: Add Drag me attention text
    $$(".item-container .item").forEach(function(item){
        var attention = document.createElement("div");
        attention.className="drag-me";
        attention.innerText = "Drag Me!";
        item.appendChild(attention);
    })

    var troller = $("#troller");
    var dropingItem = null;
    troller.addEventListener("dragstart", function(){
        alert("Don't catch me please! I am your shopping cart :(")
    })
    troller.addEventListener("dragover", function(evt){
        if (evt.preventDefault) { 
            evt.preventDefault();
        }
        evt.dataTransfer.dropEffect = 'move';
    })
    troller.addEventListener("dragenter", function(evt){
        troller.style.webkitTransform="scale(1.3)";
    })
    troller.addEventListener("dragleave", function(evt){
        troller.style.webkitTransform="scale(1.0)";
    })
    troller.addEventListener("drop", function(evt){
        troller.style.webkitTransform="scale(1)";
        dropingItem.find(".btn.order").click();
    })
    $$(".item-container .item").forEach(function(item){
        item.addEventListener("dragstart", function(){
            item.style.boxShadow="0 0 30px #008acd";
            var offset = item.offset();
            var desiredTop = (offset.y + 80);
            var actualTop = parseInt(troller.style.top);
            var middlePos = document.body.offsetWidth/2;
            var transition = troller.style.webkitTransition;
            dropingItem = item; 
            if(actualTop != desiredTop){
                troller.style.webkitTransition="none";
                troller.style.top = desiredTop + "px"
            }
            var rightPos = 400;
            if(parseInt(offset.x)>=middlePos){
                troller.style.right = (document.body.offsetWidth - troller.offsetWidth)+"px";
                rightPos = -50;
            }else{
                troller.style.right = 0;
            }
            setTimeout(function(){
                troller.style.webkitTransition=transition;
                troller.style.opacity="1";
                troller.style.right= (document.body.offsetWidth - (offset.x + rightPos)) +"px";
            },50)
        })
        item.addEventListener("dragover", function(evt){
            if (evt.preventDefault) {
                evt.preventDefault();
            }
             evt.dataTransfer.dropEffect = 'move';
        })
        item.addEventListener("dragend", function(){
            item.style.boxShadow="0 0 15px black";
            troller.style.opacity="0";
            troller.style.right = "0px";
        })
    })
}