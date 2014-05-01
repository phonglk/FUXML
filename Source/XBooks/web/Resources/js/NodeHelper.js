String.prototype.toBoolean=function(){
    return this.toLowerCase().trim()=="true";
}
// using foreach util
forEach = Array.prototype.forEach
// Single Selector
function $ (selector, e) {
    return (e?e:document).querySelector(selector);
}
// Simulating jquery.find
Node.prototype.find=function(selector){
    if(selector.indexOf("@this")>-1){
        var tid = new Date().getTime();
        this.setAttribute("data-tid", tid);
        selector=selector.replace("@this","[data-tid='"+tid+"']");
    }
    return $(selector,this);
}
Node.prototype.findAll=function(selector){
    if(selector.indexOf("@this")>-1){
        var tid = new Date().getTime();
        this.setAttribute("data-tid", tid);
        selector=selector.replace("@this","[data-tid='"+tid+"']");
    }
    return $$(selector,this);
}
// quick get/set for attributes
Node.prototype.attr=function(name,value){
    if(typeof value!="undefined"){
        this.setAttribute(name,value);
        return this;
    }
    return this.getAttribute(name);
}
//quick get data
Node.prototype.data=function(name,value){
    name="data-"+name;
    return this.attr(name,value);
}
Node.prototype.text=function(text){
    if(text){
        this.innerText = text;
    }
    return this.textContent;
}
Node.prototype.hide=function(){
    this.style.display="none";
}
Node.prototype.show=function(){
    this.style.display="block";
}
Node.prototype.offset=function(){
    var parent = this;
    var rs = {
        x:parent.offsetLeft,
        y:parent.offsetTop
    }
    do{
        parent = parent.parent("*");
        var cssPos = window.getComputedStyle(parent)["position"];
        if(cssPos == "absolute" || cssPos == "relative"){
            rs.x+=parent.offsetLeft;
            rs.y+=parent.offsetTop;
        }
    }while(parent !== document.body);
    return rs;
}
// Multi Selector
function $$ (selector, e) {
    return (e?e:document).querySelectorAll(selector);
}
NodeList.prototype.attr=function(name,value){
    this.forEach(function(e){
        e.attr(name,value);
    })
}
NodeList.prototype.toFormData=function(){
    var rs="";
    this.forEach(function(e,i){
        if(e.attr("name")){
            rs+=(i==0?"":"&") + e.attr("name")+"="+e.value;
        }
    })
    return rs;
}
NodeList.prototype.toArray=function(){
    var rs = [];
    forEach.call(this,function(e){
        rs.push(e);
    })
    return rs;
}
NodeList.prototype.forEach=function(f){
    forEach.call(this,f);
}
Object.prototype.toArray=function(){
    var rs=[];
    for(var attr in this){
        if(typeof attr != "undefined")rs.push(attr);
    }
    return rs;
}
// Get parent element [Single]
function $parent(selector,e){
    do{
        e=e["parentElement"];
        if(e.webkitMatchesSelector(selector))return e;
    }while(e!=null)
}
Node.prototype.parent=function(selector){
    return $parent(selector,this);
}
// Add class name
function addClass(className,e){
    try{
        function _addClass(clasplitssName,e){
            if(e.className.split(" ").indexOf(className)==-1)e.className+=" "+className;
        }
        if(e instanceof NodeList){
            for(var i=0;i<e.length;i++)_addClass(className, e[i])
        }else{
            _addClass(className, e);
        }
    }catch(er){
        console.error("Error");
    }
}
Node.prototype.addClass=NodeList.prototype.addClass=function(className){return addClass(className,this)};
Node.prototype.hasClass=function(className){return (this.className.split(" ").indexOf(className) > -1)}
// Remove Class name
function removeClass(className,e){
    try{
        function _removeClass(className,e){
            e.className = e.className.split(" ").filter(function(e){return e!=className}).join(" ");
        }
        if(e instanceof NodeList){
            for(var i=0;i<e.length;i++)_removeClass(className, e[i])
        }else{
            _removeClass(className, e);
        }
    }catch(er){
        console.error("Error");
    }
}
NodeList.prototype.removeClass=Node.prototype.removeClass=function(className){return removeClass(className,this)};
// extracting set of selectors's value into an array
function selector2Array(selectors,e){
//    = ".item:nth-child(1) & .info h2:text()"
//    = ".item:nth-child(1) .cover:attr(src)"
//    support text();attr(name)
    function directorAnalyze(direction){
        // manipulate director
        var arg = direction.split(/[\(,\)]/);
        arg.pop();
        return arg;
    }
    function process(v){
        var moreSel = v.split("&");
        if(moreSel.length > 1){
            var parent = moreSel.shift();
            moreSel.forEach(function(v){
                var combineSelector = parent+v;
                process(combineSelector);
            })
            return;
        }
        var args=v.match(/(.*):(.*)/);
        var sel = args[1];
        var director = directorAnalyze(args[2]);
        var tar = $(sel,e);
        var value = "";
        if(director[0]=="text"){
            value = tar.innerText;
        }else if(director[0]=="attr"){
            value = tar.getAttribute(director[1]);
        }
        rs[rs.length]=value;
    }
    if(!e)e=document;
    var selectorArray = selectors.split(",");
    var rs = [];
    selectorArray.forEach(function(v,i){
        process(v);
    })
    return rs;
}
// On Load
function onLoad(func){
    document.addEventListener("DOMContentLoaded", func);
}