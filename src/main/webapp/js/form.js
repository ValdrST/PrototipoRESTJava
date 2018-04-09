$(document).ready(function(){
    $('.sidenav').sidenav();
    $("#login").click(function(){
        let password = new jsSHA($("#password").val());
        let User = {nombre:$("#nombre").val(), password: password.getHash("SHA-256", "HEX")};
        $.ajax({
            type: "POST",
            url: "api/user/login",
            data: JSON.stringify(User),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(XMLHttpRequest, textStatus, header){
                if(XMLHttpRequest.message === "Usuario o contraseña invalidos")
                    console.log(XMLHttpRequest);
                else{
                    console.log("hola")
                }
            }
        });
    });
  });