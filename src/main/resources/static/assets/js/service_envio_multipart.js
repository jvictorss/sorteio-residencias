$(document).ready(function(){
    $('form').validate({
      debug: true,
      errorPlacement: function(label, element) {
            if (element.attr("type") == "radio") {
                label.addClass('error invalid-feedback');
                element.parent().siblings().last().append(label);
                return;
            }
            label.addClass('error invalid-feedback');
            element.parent().append(label);
      },
      submitHandler : function(form) {
        $("input, textarea, select").prop("disabled", false);
        $("#div_resultado").html("<img src='/sorteio/assets/imagens/ajax-loader.gif' style='height:30px;'></img> Efetuando operação...");
        $("#modal_elemento").modal("show");
        setTimeout(function() {
            // capture o formulário
            var forms = $("#tela_consulta")[0];
            // crie um FormData {Object}
            var data = new FormData(forms);
            $.ajax({
                url : "/sorteio/site/salvarSolicitacao",
                method: "POST",
                type: "POST",
                enctype: 'multipart/form-data',
                data: data,
                processData: false, // impedir que o jQuery tranforma a "data" em querystring
                contentType: false, // desabilitar o cabeçalho "Content-Type"
                cache: false, // desabilitar o "cache"
                success: function(data){

                },
                error: function(r) {
                  erroAjax(r);
                }
            });
        }, 2000);
      }
    });

    jQuery.extend(jQuery.validator.messages, {
        required: "Campo obrigatório."
    });
//    $.validator.addMethod("cRequired", $.validator.methods.required,
//        "Informação necessária!");
//
//    $.validator.addMethod("cDocument", $.validator.methods.documento,
//        "Informar um documento válido!");
//
//    $.validator.addClassRules("requerido", { cRequired: true});
//    $.validator.addClassRules("document", { cRequired: true, cDocument: true });
});

function erroAjax(r){
    if(r.responseJSON != null && r.responseJSON.codigo == "GFT-0004"){
      $("#modal_elemento").modal("hide");
      return;
    }
    var title = r.responseJSON == null ? 'Operação não realizada' : r.responseJSON.title;
    var code = r.responseJSON == null ? 'COD-0000' : r.responseJSON.codigo;
    var detail = r.responseJSON == null ? 'Falha interna. Favor entrar em contato com suporte técnico' : r.responseJSON.detail;
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
    $("#modal_elemento").modal("show");
}