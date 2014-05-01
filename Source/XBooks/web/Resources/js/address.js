function go(hash){
    console.log("go:"+hash);
    location.hash="#"+ hash;
//    var a =document.createElement("a");
//    a.attr("href","#"+hash);
//    a.attr("style","position:absolute;top:0;visibility:hidden")
//    document.body.appendChild(a);
//    var myEvt = document.createEvent('MouseEvents');
//    myEvt.initEvent(
//       'click'      // event type
//       ,true      // can bubble?
//       ,true      // cancelable?
//    );
//    a.dispatchEvent(myEvt)
////    console.log(a.href);
////    a.click();
//    document.body.removeChild(a);
//    history.pushState({},"XBook","#"+hash);
//    ac.trigger.call();
}
function addressController(){
    var self = this;
    self.originalHash = location.hash.substr(1);
    self.lastHash = ""
    this._acs = [];
    this.add=function(name,handler){
        self._acs.push({name:name,handler:handler});
    }
    self.trigger = function(){
        var params = location.hash.substr(1).split("/");
        var name = params[0];
        params = params.slice(1);
        self._acs.forEach(function(c){
            if(c.name==name){
                console.log("address: "+name);
                c.handler.apply(self,params);
            }
        })
        self.lastHash = name;
    }
    window.onhashchange= self.trigger;
}
ac = new addressController();