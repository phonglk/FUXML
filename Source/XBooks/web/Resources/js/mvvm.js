//Simple MVVM
window.__otids={};
mvvm = {
    _observees:[],
    changeable:function(val,applyTos){
        this.val = typeof val =="undefine"?"":val;
        return new Observee(val, applyTos);
    }
}
var _log = function(s){
//    console.log(s)
};
mvvm.changeable.toJSON = function(){
    debugger;
}
function ObservableArray(array,ctx){
    var self = this;
    self.push = function(o){
        Array.prototype.push.call(self,o)
        ctx.notify();
    }
    array.forEach(function(e,i){
        self.push(e);
    })
    self.remove = function(index){
        Array.prototype.splice.call(self,index,1);
        ctx.notify();
    }
    return self;
}
ObservableArray.prototype = new Array;
//ObservableArray.prototype.push=function(o){
//    this.push(o);
//    _log("Pushing");
//}
function Observee(data,es){
    this.toJSON = "";
    var self = this;
    this.es = es;
    if(typeof es=="undefined" || typeof es=="object")this.es = [];
    if( typeof es=="object"){
        es._returnedObservee = self;
    }
    this.notify=function(){
        if(self.es instanceof NodeList == true || self.es instanceof Array){
            self.es.forEach(function(e){
                _log("Call from Observee");
                if(e instanceof Observee){
                    e.notify();
                }else{
                    if(self.f!=null){
                        self.data = self.f();
                    }
                    applyBindings(self.data, e,self)
                }
            })
        }else{
            _log("Call from Observee");
            applyBindings(self.data, self.es,self)
        }
    }
    self.action=function(val){
        if(typeof val!="undefined"){
            if(typeof val=="function"){
                self.f=val;
                var ctx = {observee:self};
                val = val.call(ctx,ctx);
            }
            self.data = val;
            self.notify();
        }
        return self.data;
    }
    if(data instanceof Array){
        data = new ObservableArray(data,self);
    }
    this.data = data;
    self.f=null;
    try{
        if(typeof es=="undefined"  || typeof es=="object"){ // not monitored element at declaration
            return function(val){ // return function for flexible usage after
                if(typeof this=="object"){
                    if(typeof this['__e'] !="undefined"){
                        if(typeof self.es=="undefined"){
                            self.es=this.__e;
                        }else{
                            if(self.es instanceof Node){
                                self.es = [self.es];
                            }else if(self.es instanceof NodeList){
                                self.es = self.es.toArray();
                            }
                            self.es.push(this.__e);
                        }
                        return self.action.call(self,self.data);
                    }else{
                        if(typeof val!="undefined"){
                            return self.action.call(self,val);
                        }else{
                            if(arguments.callee.caller.arguments.length > 0 &&
                                typeof arguments.callee.caller.arguments[0].observee !="undefined" &&
                                arguments.callee.caller.arguments[0].observee instanceof Observee){
                                self.es.push(arguments.callee.caller.arguments[0].observee);
                            }
                            if(typeof self.es=="undefined"){
                                return function(val){
                                    if(typeof this=="object" &&
                                        typeof this['__e'] !="undefined"){
                                        self.es=this.__e;
                                        return self.action.call(self,self.data);
                                    }
                                }
                            }else{
                                return self.action.call(self);
                            }
                        }
                    }
                }else{
                    return self.action.call(self,val);
                }
            }
        }else{
            this.notify();
            return self.action;
        }
    }catch(e){
        console.error("Error: "+e.message);
    }
}
function applyBindings(dataModel,e,observee){
    var bindLevel = 0;
    function manipulateParams(params){
        var tarObj=null,
            func=null,
            funcParams=[];
        try{
//            debugger;
            var splitted = params.split(/:/)
            tarObj = splitted[0];
            var bindParam = splitted[1];
            var split2 = bindParam.split(/[\(,\)]/)
            func = split2.shift();split2.pop();
            funcParams=split2;
        }catch(e){};
        return {
            value:tarObj,
            func:func,
            params:funcParams
        }
    }
    function selectFirstDataBind(e){
        var all = e.findAll("@this [data-bind]").toArray();
        var childs = e.findAll("@this [data-bind] [data-bind]").toArray();
        all = all.filter(function(e,i,a){
            return childs.indexOf(e) == -1;
        })
//        var _t = 0;
//        all.forEach(function(e){
//            if(e.data("binded")=="true"){
//                _t++;
//            }
//        })
//        if(_t==0){
//            return;
//        }
        all.forEach(function(e){
            e.data("binded",bindLevel);
        })
        return all;
    }

    if(e==null){
        console.error("Cannot find node");
        return;
    }
    e.findAll("@this [data-bind]").forEach(function(e){
        e.removeAttribute("data-binded");
    })
    var dataBind = manipulateParams(e.getAttribute("data-bind"));
//    _log("Apply model ["+JSON.stringify(dataModel)+"] to node["+e.getAttribute("data-bind")+"]");
    if(["string","number","boolean"].indexOf(typeof dataModel)>-1){
        try{
            _log("Process "+typeof dataModel);
        }catch(err){
            console.error("Error")
        }
//        if(typeof dataModel=="function")dataModel = dataModel();
        if([null,"","text"].indexOf(dataBind.func)>-1){
            e.innerText = dataModel;
        }else if(dataBind.func=="attr"){
            e.setAttribute(dataBind.params[0],dataModel);
        }else if(dataBind.func=="condition"){
            var cond = dataBind.params[0].toBoolean();
            if(cond!=dataModel)e.style.display="none"
            else e.style.removeProperty("display");
        }else if(dataBind.func=="css"){
            e.attr("style",dataModel);
        }else if(dataBind.func=="editable"){
            e.value = dataModel;
            if(e.attr("data-listened")==null){
                e.addEventListener("change", function(evt){
                    try{
                        observee.data = e.value;
                        observee.notify();
                    }catch(err){
                        _log(err);
                    }
                })
                e.attr("data-listened","true");
            }
        }else if(dataBind.func=="click"){
            e.addEventListener("click", function(){
                new Function("data",dataBind.params[0]+".call(this,data)")(dataModel);
            })
        }else{
            console.error("Unsupport function: "+dataBind.func);
        }
    }else{
        if(typeof dataModel == "object"){
            if(dataModel instanceof Array){
                if(dataBind.func=="for-each"){
                    // clone current Node;
                    var currentNode;
                    if(e.getAttribute("data-otid")==null){
                        currentNode = e.cloneNode(true);
                        var otid=new Date().getTime()+"";
                        e.setAttribute("data-otid", otid) ;
                        window.__otids[otid]=e.cloneNode(true);
                    }else{
                        var otid=e.getAttribute("data-otid");
                        currentNode = window.__otids[otid].cloneNode(true);
                    }
                    while(e.hasChildNodes()){
                        e.removeChild(e.lastChild);
                    }
                    dataModel.forEach(function(data,_i,_a){
                        var clonedNode = currentNode.cloneNode(true);
                        var loopData = {}
                        if(typeof data =="object"){
                            loopData=data;
                            loopData["meta-isLast"]=loopData["isLast"]=_i==(_a.length-1);
                            loopData["meta-idx"]=_i;
                            loopData["meta-idx-num"]=_i+1;
                        }else{
                            loopData={
                                value: data,
                                isLast:_i==(_a.length-1)
                            };
                        }

                        applyBindings(loopData, clonedNode);
                        var childsOfClone = clonedNode.childNodes;
                        var length = childsOfClone.length
                        for(var ci=0;ci<length;ci++){
                            e.appendChild(childsOfClone[0]);
                        }
                    })
                }else{
                    console.error("Unsupport function "+dataBind.func)
                }
            }else{//Real Object
                _log("Process object");
                var bindedNode=selectFirstDataBind(e);// select all used binding node
                bindedNode.forEach(function(e){
                    var bindingData = e.getAttribute("data-bind");
                    var params = manipulateParams(bindingData);
                    if(dataModel.toArray().indexOf(params.value)>-1){
                        applyBindings(dataModel[params.value], e)
                    }
                })
//                if(e.findAll("@this [data-bind]:not([data-binded])").length>0){
//                    applyBindings(dataModel,e);
//                }
            }
        }else if(typeof dataModel=="function"){
            var context={
                __e:e
            }
            dataModel.call(context);
        }else{
            console.error("What the type : " + typeof dataModel);
        }
    }
}