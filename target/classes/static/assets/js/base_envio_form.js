$(document).ready(function(key, form){
    $('form').each(function() {
        $(this).validate({
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
            $(".dinheiro").mask('###0.00', {reverse: true});
            $("input, textarea, select").prop("disabled", false);
            var metodo = $(".metodo",form).val();
            var retorno = $(".retorno",form).val();
            var usaRetorno = $(".usaRetorno",form).val();
            var url = $(".url",form).val();
            var servico = url.replaceAll("/sorteio/v1/","");
            var dados = $(form).serializeArray();
            //do something here
            $("#div_resultado").html("<div class='d-flex align-items-center justify-content-center ms-auto'>"+
                                                "<div class='spinner-border' role='status' aria-hidden='true'></div>"+
                                                "<strong><pre class='m-0 p-3'> Efetuando operação...</pre></strong></div>");
            $("#modal_elemento").modal("show");
            setTimeout(function() {
    //            form.submit();
                enviar_form(url, retorno, dados, metodo, usaRetorno, servico);
            }, 2000);
          }
        });
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

function error(readyState,status,statusText){
    this.readyState = readyState;
    this.status = status;
    this.statusText = statusText;
}

function getFormData(dados){
    var indexed_array = {};

    $.map(dados, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
    return JSON.stringify(indexed_array);
}

function enviar_form(url, retorno, dados, metodo, usaRetorno, servico){
    $.ajax({
        url : url + "?servico="+servico,
        data: getFormData(dados),
        contentType: 'application/json; charset=utf-8',
        processData: false,
        method: ""+metodo,
        type: ""+metodo,
        dataType: 'json',
        success: function(data){
            if("true" == usaRetorno){
                retorno = retorno + data;
            }
            window.location.href = retorno;
        },
        error: function(r) {
          let mudarRota = ["agenda"];
          if(mudarRota.indexOf(servico) >= 0){
            window.location.href = retorno;
            $("#modal_elemento").modal("show");
          }
          var title = r.responseJSON.title == null ? 'Operação não realizada' : r.responseJSON.title;
          var code = r.responseJSON.codigo == null ? 'COD-0000' : r.responseJSON.codigo;
          var detail = r.responseJSON.detail == null ? 'Falha interna. Favor entrar em contato com suporte técnico' : r.responseJSON.detail;
          $("#div_resultado").html('<h4 class="m-auto alert-danger p-3 text-center">Titulo: <strong>'+title+'</strong></h4>'
                    +'<div class="form-group card p-3"><p class="p-0">'
                    +'<label>Código:</label>'
                    +'<input type="text" class="form-control" value="'+code+'" disabled>'
                    +'<br>'
                    +'<label>Mensagem:</label>'
                    +'<textarea class="form-control" disabled>'+detail+'</textarea>'
                    +'</p></div>'
                    +'<button class="btn btn-primary m-3" data-bs-dismiss="modal">Fechar</button>');
                    autosize(document.querySelectorAll('textarea'));
        }
     });
}