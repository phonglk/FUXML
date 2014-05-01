// Modal
function initModal(){
    $("#modal-bg").addEventListener("click", function(){
        if($(".modal.show").data("isModal")=="true")return;
        go("!");
//        removeClass("show",$("#modal-bg"));
//        removeClass("show",$$(".modal"));
    })
    forEach.call($$(".modal .close"),function(e){
        e.addEventListener("click", function(){
            modal_close($parent(".modal",e));
            go("!");
        })
    })
    forEach.call($$("[data-modal]"),function(e){
        e.addEventListener("click", function(){
            var dataTarget = e.attr("href");
            if(dataTarget.indexOf("?")==0){
                dataTarget=dataTarget.substr(1);
            }
            modal($(dataTarget));
        })
    })
}
function modal_bg_show(){
    addClass("show",$("#modal-bg"));
}
function modal($tar){
    modal_closeAll();
    modal_bg_show();
    addClass("show",$tar);
    return {
        close:function(){
            modal_close($tar);
        }
    }
}
function modal_close($tar){
    removeClass("show",$tar);
    if($(".modal.show")==null)removeClass("show",$("#modal-bg"));
}
function modal_closeAll(){
    removeClass("show",$$(".modal"));
    removeClass("show",$("#modal-bg"));
}
onLoad(initModal);
// Modal.End
// Processing Modal
function onBusy(isOn){
    if(isOn){
        $("#busy").addClass("show")
    }else{
        $("#busy").removeClass("show")
    }
}
//