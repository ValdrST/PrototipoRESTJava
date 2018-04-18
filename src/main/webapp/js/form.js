/* global sesionStorage */

$(document).ready(function(){
    $('.sidenav').sidenav();
    if(sessionStorage.getItem('Bearer')!=="")
        $("#form_login").show();
    else
        $("#form_login").show();
    $("#login").click(function(){
        let password = new jsSHA($("#password").val());
        let User = {nombre:$("#nombre").val(), password: password.getHash("SHA-256", "HEX")};
        $.ajax({
            type: "POST",
            url: "api/user/login",
            data: JSON.stringify(User),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(XMLHttpRequest, textStatus, request){
                if(XMLHttpRequest.message === "Usuario o contraseña invalidos")
                    alert("Usuario o contraseña invalidos");
                else if (XMLHttpRequest.message === "sesion iniciada con exito"){
                    alert("sesion iniciada con exito");
                    sessionStorage.setItem('Bearer',request.getResponseHeader('Bearer'));
                    $("#form_login").hide();
                }
                else if(textStatus !== "success"){
                    alert("Error de servidor");
                }
            }
        });
    });
    $('.listar').click(function(){
        console.log("hola");
        $.ajax({
            url: "api/user/getAllUsers",
            method: "GET",
            headers: {
                'Bearer': sessionStorage.getItem('Bearer')
            },
            statusCode: {
                200: function(XMLHttpRequest, textStatus, request){
                    console.log(XMLHttpRequest);
                if(XMLHttpRequest.message === "Refresh Token")
                    console.log(XMLHttpRequest);
                    sessionStorage.setItem('Bearer',request.getResponseHeader('Bearer'));
            },
                403: function(XMLHttpRequest){
                    console.log(XMLHttpRequest);
                    alert("sesion no valida");
                },
                401: function(XMLHttpRequest){
                    alert("sesion no iniciada");
                    sessionStorage.setItem('Bearer',"");
                    $("#form_login").show();
                }
            }
        });
    });
  });