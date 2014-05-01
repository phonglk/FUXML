
function ajax(o) {
    // o.url
    // o.type
    // o.callback
    // o.queryString
    var self = this;
    self.xhr = null;
    self.callback = o.callback;
    self.queryString = o.queryString || "";
    if(o.data && o.data instanceof Array){

    }
    // Mozilla/Safari
    if (window.XMLHttpRequest) {
        self.xhr = new XMLHttpRequest();
    }

    self.xhr.open(o.type||"POST", o.url, true);
    self.xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    self.xhr.onreadystatechange = function() {
        if (self.xhr.readyState == 4) {
            self.callback.call(self,self.xhr.responseXML);
        }
    }
    self.xhr.send(self.queryString);
    return self;
}

function Tooltip(string,top,left){
    $(".tooltip-label").innerText = string;
    $(".tooltip-label").style.top = top;
    $(".tooltip-label").style.left = left;
}
// automatic submit form from tag a

function preventInputNumber(){
    $$("input[type='number']:not([data-preventInput])").forEach(function(e){
        e.addEventListener("keypress", function(evt){
            evt.preventDefault();
            return false;
        })
        e.attr("data-preventInput","true");
    })
}
function additionalValidationNumber(){
    $$("input[type='number']:not([data-additionalvalid])").forEach(function(e){
        e.addEventListener("keyup", function(evt){
            var wrongInput = false;
            try{
                if(e.valueAsNumber > parseInt(e.max)){
                    e.value = parseInt(e.max);
                    wrongInput = true;
                }else if(e.valueAsNumber < parseInt(e.min)){
                    e.value = parseInt(e.min);
                    wrongInput = true;
                }
            }catch(err){
                e.value = 1;
                wrongInput = true;
            }
            if(wrongInput){
                evt.preventDefault();
                return false;
            }
            return true;
        })
        e.attr("data-additionalvalid","true");
    })
}
onLoad(function(){
    $$("[data-submitform]").forEach(function(a){
        a.addEventListener("click", function(e){
            a.parent("form").find("input[type='submit']").click();
            e.preventDefault();
            return false;
        })
    })
})
String.prototype.toTitleCase=function(){
    return this.substr(0,1).toUpperCase().concat(this.substr(1));
}