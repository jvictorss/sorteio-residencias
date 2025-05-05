$(document).ready(function(){
    $(document).on('click','#srv-editar',function(){
        var id = $(this).data("id");
        var servico = $(this).data("servico");
        var sd1 = $(this).data("sd1");
        var sd2 = $(this).data("sd2");
        var descricao = $(this).data("nome");
        var pathTB = $(this).data("path");
        var path = "atualizar";
        if(pathTB !== undefined){
            path = pathTB;
        }
        var titulo = "Deseja realmente Atualizar o item selecionado?";
        var url = "/sorteio/v1/"+servico+"/"+path+"?id="+id+"&servico="+servico;
        var body = servico+ " : "+ descricao;
        modal_show(titulo, body, url, "post")
    });
    $(document).on('click','#srv-familiar',function(){
        var queryParam = $(this).data("param");
        var titulo = "Deseja realmente editar o cadastro do familiar selecionado?";
        var url = "/sorteio/site/back/composicaofamiliar/atualizar/"+queryParam;
        var descricao = $(this).data("nome");
        var body = "Nome : "+ descricao;
        modal_show(titulo, body, url, "get")
    });
    $(document).on('click','#srv-remover-familiar',function(){
        var queryParam = $(this).data("param");
        var titulo = "Deseja realmente remover o cadastro do familiar selecionado?";
        var url = "/sorteio/site/back/composicaofamiliar/remover/"+queryParam;
        var descricao = $(this).data("nome");
        var body = "Nome : "+ descricao;
        modal_show(titulo, body, url, "get")
    });
    $(document).on('click','#srv-generico',function(){
        var servico = $(this).data("servico");
        var descricao = $(this).data("nome");
        var endpoint = $(this).data("url");
        var metodo = $(this).data("metodo");
        var retorno = $(this).data("retorno");
        var usaRetorno = $(this).data("usaretorno");
        var titulo = "Deseja realmente realizar a operação selecionada para o item abaixo?";
        var url = "/sorteio/v1/" + servico + endpoint;
        var body = servico+ " : "+ descricao;
        modal_message(titulo, body, url, retorno, metodo, usaRetorno);
    });

    function modal_show(titulo, body, url, metodo) {
        $('#div_resultado').html(
            "<div class=\"card\">\n" +
            "<form action='" + url + "' method='"+ metodo +"'>" +
            "   <article class='card-header'><h5>" + titulo + "</h5></article>\n" +
            "   <article class='card-body'>\n" +
            "       <p>" + body + "</p>\n" +
            "   </article>\n" +
            "   <article id='buttons' class='card-footer'></article>\n" +
            "</form>"+
            "</div>"
        );
        $("#buttons").html("<button class='btn btn-success' type='submit'> Sim </button>\n" +
                    "<a type='button' class='btn btn-danger' data-bs-dismiss='modal'> Não</a>");
        $("#modal_elemento").modal('show');
    }

    function modal_message(titulo, body, url, retorno, metodo, usarRetorno) {
        $('#div_resultado').html(
            "<div class=\"card\">\n" +
            "   <article class='card-header'><h5>" + titulo + "</h5></article>\n" +
            "   <article class='card-body'>\n" +
            "       <p>" + body + "</p>\n" +
            "   </article>\n" +
            "   <article id='buttons' class='card-footer'></article>\n" +
            "</div>"
        );
        $("#buttons").html("<button class='btn btn-success' type='button' onclick='enviar(\""+url+"\",\""+retorno+"\",\""+metodo+"\","+usarRetorno+")'> Sim </button>\n" +
                    "<a type='button' class='btn btn-danger' data-bs-dismiss='modal'> Não</a>");
        $("#modal_elemento").modal('show');
    }
});

function enviar(url, retorno, metodo, usaRetorno){
    $("#div_resultado").html("<div class='d-flex align-items-center justify-content-center ms-auto'>"+
                                        "<div class='spinner-border' role='status' aria-hidden='true'></div>"+
                                        "<strong><pre class='m-0 p-3'> Efetuando operação...</pre></strong></div>");
    $("#modal_elemento").modal("show");
    setTimeout(function() {
        $.ajax({
            url : url,
            processData: false,
            method: metodo,
            type: metodo,
            success: function(data){
                if(usaRetorno){
                    retorno = data;
                }
                window.location.href = retorno;
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