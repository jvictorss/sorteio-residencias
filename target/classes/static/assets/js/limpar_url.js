$(document).ready(function() {
    var url_atual = window.location.href;
    var new_url = url_atual.replace(/(\?).*/gm,"");
    history.pushState({}, null, new_url);
})