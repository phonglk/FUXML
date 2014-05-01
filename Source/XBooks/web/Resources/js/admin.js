/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function loadAjaxAuthor(callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=getAuthor",
        callback:function(data){
            var x = data.documentElement.childNodes;
            var selAuthor = $$(".selectAuthor");
            var opt = "";
            for(var i = 0; i< x[0].childNodes.length; i++){
                var curNode = x[0].childNodes[i];
                opt += "<option value='"+curNode.childNodes[0].textContent+"'>"+curNode.childNodes[1].textContent+"</option>";
            }
            selAuthor.forEach(function(e){
                e.innerHTML = opt;
            })
            if(callback)callback.call();
        }
    });
}
function loadAjaxPublisher(callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=getPublisher",
        callback:function(data){
            var x = data.documentElement.childNodes;
            var selPublisher = $$(".selectPublisher");
            var opt = "";
            for(var i = 0; i< x[0].childNodes.length; i++){
                var curNode = x[0].childNodes[i];
                opt += "<option value='"+curNode.childNodes[0].textContent+"'>"+curNode.childNodes[1].textContent+"</option>";
            }
            selPublisher.forEach(function(e){
                e.innerHTML = opt;
            })
            if(callback)callback.call();
        }
    });
}
function loadAjaxCategory(callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=getCategory",
        callback:function(data){
            var x = data.documentElement.childNodes;
            var selCategory = $$(".selectCategory");
            var opt = "";  
            for(var i = 0; i< x[0].childNodes.length; i++){
                var curNode = x[0].childNodes[i];
                opt += "<option value='"+curNode.childNodes[0].textContent+"'>"+curNode.childNodes[1].textContent+"</option>";
            }
            selCategory.forEach(function(e){
                e.innerHTML = opt;
            })
            if(callback)callback.call();
        }
    });
}
function addCategory(name,callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=addCategory&txtCategoryName="+name+"&txtAreaDescription=",
        callback:function(data){
            loadAjaxCategory(function(){
                callback.call();
            });
        }
    });
}
function addPublisher(name,callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=addPublisher&txtPublisherName="+name+"&txtAreaDescription=",
        callback:function(data){
            loadAjaxPublisher(function(){
                callback.call();
            });
        }
    });
}
function addAuthor(name,callback){
    new ajax({
        type:"POST",
        url:"Admin",
        queryString:"action=addAuthor&txtAuthorName="+name+"&txtAreaDescription=",
        callback:function(data){
            loadAjaxAuthor(function(){
                callback.call();
            });
        }
    });
}
function tableRowEditBinding(){
    $$(".table-row").forEach(function(e){
        e.find(".btn.edit").addEventListener("click",function(){
            $(".edit-form").reset();
            var dataJson = JSON.parse(e.data('json').replace("'","\'"));

            $$(".edit-form .selectCategory option").forEach(function(a){
                if(a.innerHTML.toString() == dataJson['Category']){
                    a.selected=true;
                }
            });
            $$(".edit-form .selectAuthor option").forEach(function(a){
                var listAuthor = dataJson['Authors'];
                listAuthor.forEach(function(s){
                    if(a.innerHTML.toString() == s){
                        a.selected=true;
                    }
                })
            });
            $$(".edit-form .selectPublisher option").forEach(function(a){
                if(a.innerHTML.toString().replace(/&amp;/g, "&") == dataJson['Publisher']){
                    a.selected=true; 
                }
            });
            $(".edit-form .cover img").src = dataJson['ImageUrl'].toString();
            $(".edit-form input[name='txtId']").value = dataJson['Id'].toString();
            $(".edit-form input[name='txtTitle']").value = dataJson['Title'].toString();
            $(".edit-form input[name='txtISBN']").value = dataJson['ISBN'].toString();
            $(".edit-form textarea[name='txtAreaDescription']").value = dataJson['Description'].toString();
            $(".edit-form input[name='txtPrice']").value = dataJson['Price'].toString();
        })
    })
}
function tableRowDeleteBinding(){
    $$(".table-row").forEach(function(e){
        e.find(".btn.delete").addEventListener("click",function(){
            var dataJson = JSON.parse(e.data('json').replace("'","\'"));
            var id = dataJson['Id'].toString();
            new ajax({
                type:"POST",
                url:"Admin",
                queryString:"action=delBook&txtId="+id,
                callback:function(data){
                    var x = data.documentElement.childNodes[0];
                    if(x.textContent == "true"){
                        alert("Delete success");
                        location.reload();
                    }else{
                        alert("Delete fail")
                    }
                }
            });
        })
    })
}


loadAjaxCategory();
loadAjaxPublisher();
loadAjaxAuthor();
tableRowEditBinding();
tableRowDeleteBinding();

ac.add("!",function(id){
    modal_closeAll();
})
ac.add("",function(id){
    modal_closeAll();
})

function AddBook(){
    var da=$("#addBook .cover")
    var fi = $("#addBook [type=file]")
    var img=$("#addBook .cover img")
    var strShadow = da.style.boxShadow;

    fi.addEventListener("invalid", function(e){
        message("The new book must have a cover inage");
    })
    fi.addEventListener("change", function(e){
        var files = fi.files;
        if(files.length>0){
            var f = files[0];
            if(f.type.indexOf("image")>-1){
                fi.files = files;
                previewCover();
            }else{
                alert("Please give book an cover image!");
            }
        }
    })
    da.addEventListener("click", function(e){
        fi.click();
    })
    da.addEventListener("dragover", function(e){
        e.stopPropagation();
        e.preventDefault();
    });
    da.addEventListener("dragenter", function(e){
        e.stopPropagation();
        e.preventDefault();
        da.style.boxShadow = "1px 1px 20px black"
    });
    da.addEventListener("dragleave", function(e){
        e.stopPropagation();
        e.preventDefault();
        da.style.boxShadow = strShadow;
    });
    da.addEventListener("drop", function(e){
        e.stopPropagation();
        e.preventDefault();
        da.style.boxShadow = strShadow;
        
        var dt = e.dataTransfer;
        var files = dt.files;
        if(files.length>0){
            var f = files[0];
            if(f.type.indexOf("image")>-1){
                fi.files = files;
                previewCover();
            }else{
                alert("Please give book an cover image!");
            }
        }
    });
    function previewCover(){
        var files = fi.files;
        if(files.length>0){
            var f = files[0];
            if(f.type.indexOf("image")>-1){
                var reader = new FileReader();
                reader.onload=function(){
                    var data = this.result;
                    var img = document.createElement("img");
                    img.attr("src",data);
                }
                reader.readAsDataURL(f);
            }else{
                alert("Please give book an cover image!");
            }
        }
    }
}

function selectLast($$select){
    $$select.forEach(function(select){
        select.selectedIndex = select.options.length -1;
    })
}
$$(".btn.add-cat").forEach(function(btn){
    btn.addEventListener("click", function(){
        var cat = prompt("New category:","New Category");
        if(cat){
            addCategory(cat, function(){
                selectLast($$(".selectCategory"));
            })
        }
    })
})
$$(".btn.add-pub").forEach(function(btn){
    btn.addEventListener("click", function(){
        var pub = prompt("New Publisher:","New Publisher");
        if(pub){
            addPublisher(pub, function(){
                selectLast($$(".selectPublisher"));
            })
        }
    })
})
$$(".btn.add-auth").forEach(function(btn){
    btn.addEventListener("click", function(){
        var cat = prompt("New Author:","New Author");
        if(cat){
            addAuthor(cat, function(){
                selectLast($$(".selectAuthor"));
            })
        }
    })
})

AddBook();