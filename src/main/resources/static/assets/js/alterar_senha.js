$(document).ready(function(){
    $("#validate_alterar").validate({
      debug: true,
      submitHandler : function(form) {
        $("#div_resultado").html("<div class='d-flex align-items-center justify-content-center ms-auto'>"+
                                            "<div class='spinner-border' role='status' aria-hidden='true'></div>"+
                                            "<strong><pre class='m-0 p-3'> Efetuando operação...</pre></strong></div>");
        $("#modal_elemento").modal("show");
        var dados = $( form ).serialize();
        setTimeout(function() {
            $.ajax({
                url : "/sorteio/v1/usuario/senha",
                data: dados,
                processData: false,
                method: 'POST',
                type: 'POST',
                success: function(data){
                    $("#div_resultado").html('<h4 class="m-auto alert-success p-3 text-center">Titulo: <strong>Operação de alterar senha</strong></h4>'
                            +'<div class="form-group card p-3"><p class="p-0">'
                            +'Senha alterada com sucesso!'
                            +'</p></div>');
                    setTimeout(function() {
                        window.location.href = data;
                    }, 2000);
                },
                error: function(r) {
                    var title = r.responseJSON.title == null ? 'Operação não realizada' : r.responseJSON.title;
                    var code = r.responseJSON.codigo == null ? 'COD-0000' : r.responseJSON.codigo;
                    var detail = r.responseJSON.detail == null ? 'Falha interna. Favor entrar em contato com suporte técnico' : r.responseJSON.detail;
                    $("#div_resultado").html('<h4 class="m-auto alert-danger p-3 text-center">Titulo: <strong>'+title+'</strong></h4>'
                              +'<div class="form-group card p-3"><p class="p-0">'
                              +'<label for="cpf">Código:</label>'
                              +'<input type="text" class="form-control" value="'+code+'" disabled>'
                              +'<br>'
                              +'<label for="cpf">Mensagem:</label>'
                              +'<textarea class="form-control" disabled>'+detail+'</textarea>'
                              +'</p></div>'
                              +'<button class="btn btn-primary m-3" data-bs-dismiss="modal">Fechar</button>');
                              autosize(document.querySelectorAll('textarea'));
                }
             });
        }, 2000);
      }
    });
});