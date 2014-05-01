function xpath(node,exp,type){
    var ref_type = [];
    ref_type["string"] = 2;ref_type["boolean"] = 3;ref_type["number"] = 1;
    var isError = false;
    var rs=null;
    try{
        rs = document.evaluate(exp,node,null,ref_type[type],null)[type+"Value"];
        if(rs=="undefined")rs = null;
    }catch(err){
        isError = true;
        console.log("XPath Error: "+ err.message);
    }
    if(isError){
        return {
            success:!isError,
            result:null
        }
    }else{
        return {
            success:!isError,
            result:rs
        }
    }
}