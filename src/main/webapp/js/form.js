$(document).ready(function(){
    $('.sidenav').sidenav();
    $("#login").click(function(){
        let password = new jsSHA($("#password").val());
        let User = {nombre:$("#nombre").val(), password: password.getHash("SHA-512", "HEX")};
        console.log(User.password);
        $.ajax({
            type: "POST",
            url: "api/user/login",
            data: JSON.stringify(User),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(result){
                if(result.message === "Usuario o contraseña invalidos")
                    console.log(window.location.pathname);
                else
                    console.log("sesion validada");
            }
        });
    });
  });