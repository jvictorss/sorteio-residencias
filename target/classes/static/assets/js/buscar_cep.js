$(document).ready(function(){
	var inputsCEP = $('#logradouro, #bairro, #localidade, #uf');
	var validacep = /^[0-9]{8}$/;
	
	function limpa_formulário_cep(alerta) {
	  if (alerta !== undefined) {
	    alert(alerta);
	  }
	  inputsCEP.val('');
	}
	function get(url) {
	  $.get(url, function(data) {
	    if (!("erro" in data)) {
	      if (Object.prototype.toString.call(data) === '[object Array]') {
	        var data = data[0];
	      }
	      $.each(data, function(nome, info) {
	        $('#' + nome).val(nome === 'cep' ? info : info).attr('info', nome === 'cep' ? info : info);
	      });
	    } else {
	      limpa_formulário_cep("CEP não encontrado.");
	    }
	    $('#numero').focus();
	  });
	}
	function limpa_formulário_cep() {
        // Limpa valores do formulário de cep.
        $("#logradouro").val("");
        $("#bairro").val("");
        $("#localidade").val("");
        $("#uf").val("");
        $('#cep').focus();
    }
    
	$('#logradouro,#localidade, #uf').on('blur', function(e) {
		if ($('#logradouro').val() !== $('#logradouro').attr('info') || $('#localidade').val() !== $('#localidade').attr('info') || $('#uf').val() !== $('#uf').attr('info')) {			    
            get('https://viacep.com.br/ws/' + $('#uf').val() + '/' + $('#localidade').val() + '/' + $('#logradouro').val() + '/json/');
		}
	});
	
	$('#cep').on('blur', function(e) {
	  var cep = $('#cep').val().replace(/\D/g, '');
	  if (cep !== "" && validacep.test(cep)) {
	    inputsCEP.val('...');
	    get('https://viacep.com.br/ws/' + cep + '/json/');
	  } else {
	    limpa_formulário_cep(cep == "" ? undefined : "Formato de CEP inválido.");
	    $('#cep').val("");
	    $('#cep').focus();
	  }
	});
});