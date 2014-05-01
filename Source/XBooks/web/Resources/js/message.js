var message,message_close;
(function(){
    "use strict"
    var m = $(".message");
    var mContent = m.find(".content");
    var dis = m.find(".dismiss");

    onLoad(function(){
        m.data("time",new Date().getTime());
        dis.addEventListener("click", function(){
            message_close();
        })
    })
    message=function(string){
        var time = 3000;
        mContent.innerText = string;
        if(!m.hasClass("show")){
            m.addClass("show");
        }else{
            mContent.style.color="red";
            mContent.addEventListener("webkitTransitionEnd", function(evt){
                mContent.style.color="#333";
            })
        }
        m.data("time",new Date().getTime());
        setTimeout(function(){
            if((new Date().getTime() - parseInt(m.data("time"))) >= time){
                message_close();
            }
        },time+1)
    }
    message_close=function(){
        m.removeClass("show");
    }
})()