//$(document).ready(function(){
//    $("#btn-sun").on("click",function(){
//        $("html").attr("data-bs-theme","light");
//        document.cookie = "theme=light; path=/";
//        $("#icon-theme").removeClass("fa-moon");
//        $("#icon-theme").addClass("fa-sun");
//        document.cookie = "iconTheme=fa-sun; path=/";
//    });
//    $("#btn-moon").on("click",function(){
//        $("html").attr("data-bs-theme","dark");
//        document.cookie = "theme=dark; path=/";
//        $("#icon-theme").removeClass("fa-sun");
//        $("#icon-theme").addClass("fa-moon");
//        document.cookie = "iconTheme=fa-moon; path=/";
//    });
//});
$(document).ready(function(){
    $("#btn-sun").on("click",function(){
        $("#rel-theme1").attr("href","/sorteio/assets/vendor/css/theme-default.css");
        $("#rel-theme2").attr("href","/sorteio/assets/vendor/css/core2.css");
        $("#icon-theme").removeClass("bx-moon");
        $("#icon-theme").addClass("bx-sun");
        $(".logo_img").attr("src","/sorteio/assets/imagens/logo.jpg");
        document.cookie = "themeBordRSD=theme-default.css; path=/";
        document.cookie = "themeRSD=core2.css; path=/";
        document.cookie = "iconTheme=bx-sun; path=/";
    });
    $("#btn-moon").on("click",function(){
        $("#rel-theme1").attr("href","/sorteio/assets/vendor/css/theme-dark.css");
        $("#rel-theme2").attr("href","/sorteio/assets/vendor/css/core-dark.css");
        $("#icon-theme").removeClass("bx-sun");
        $("#icon-theme").addClass("bx-moon");
        $(".logo_img").attr("src","/sorteio/assets/imagens/logo.jpg");
        document.cookie = "themeBordRSD=theme-dark.css; path=/";
        document.cookie = "themeRSD=core-dark.css; path=/";
        document.cookie = "iconTheme=bx-moon; path=/";
    });
});