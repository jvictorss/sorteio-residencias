function showDados(result, modificadores){
    let text = '<div class="div-form row">';
    let naoExibir = ["idUser", "idUserAtualizou","datas","horas","distribuicao","hash","senha","token","fotoFrente","fotoVerso","fotoEnvio","fotoRecebimento"];
    $.each(result, function(indice, valor){
        if(naoExibir.indexOf(indice) < 0){
            var vlr = valor;
            if(modificadores != null){
                modificadores.map(function(i, d){
                    if(i.campo == indice){
                        vlr = i.valor
                    }
                });
            }
            if(vlr == null){
                return;
            }
            let result = vlr.toString().includes(";base64,");
            var input = '<label class="form-control">'+ formatar(indice, vlr) + '</label>';
            var isCompleto = false;
            if(result){
                isCompleto = true;
                input = '<img class="form-control" width="80" src="'+vlr+'"></img>';
            }
            let mudarLabel = ["caracteristica","historico","comentario"];
            if(mudarLabel.indexOf(indice.toLowerCase()) >= 0){
                isCompleto = true;
                input = '<textarea class="form-control" rows="4" disabled>'+vlr+'</textarea>';
            }
            var col = "col-md-6";
            if(vlr.length > 50 || isCompleto){
                col = "col-md-12";
            }
            text += '<div class="form-group '+isCompleto+'"><label>'+indice.match(/[A-Z]+[^A-Z]*|[^ A-Z]+/g," ").map(str => str[0].toUpperCase() + str.slice(1)).join(" ") + '</label>' +
            input + '</div>';
        }
    });
    text += '</div>';
    let dados = "<div class='rounded-0 rounded-bottom card'>" +
              "     <article class='card-header'><h5>Informações</h5><div class='modal-header'><button type='button' class='btn-close' data-bs-dismiss='modal' aria-label='Close'></button></article>" +
              "     <article class='card-body'>" + text +
              "     </article>" +
              "     <article id='buttons' class='card-footer'>" +
              "     <button type='button' class='btn btn-secondary' data-bs-dismiss='modal'><i class='fa-solid fa-xmark'></i> Fechar</button>" +
              "     </article>" +
              "</div>";
    $('#div_resultado').html(dados);
    $("#modal_elemento").modal('show');
}

function formatar(indice, valor){
    if(valor == null){
        return "";
    }
    let formatarValor = ["valorunitario","valorsus","valorglobal"];
    let formatarNumero = ["quantidade","quantidadedisponivel"];
    let formatarData = ["criado","atualizado","data","iniciovigencia","finalvigencia"];
    let formatarStatus = ["ativo"];
    let formatarTipo = ["tipo"];
    if(formatarValor.indexOf(indice.toLowerCase()) >= 0){
        return valor.toLocaleString('pt-br',{style: 'currency', currency: 'BRL'});
    }
    if(formatarNumero.indexOf(indice.toLowerCase()) >= 0){
        return valor.toLocaleString();
    }
    if(formatarData.indexOf(indice.toLowerCase()) >= 0){
        return valor.substr(0, 10).split('-').reverse().join('/');
    }
    if(formatarStatus.indexOf(indice.toLowerCase()) >= 0){
        return valor ? "Ativo" : "Inativo";
    }
    if(formatarTipo.indexOf(indice.toLowerCase()) >= 0){
        return valor.replace("ROLE_","");
    }
    return valor;
}