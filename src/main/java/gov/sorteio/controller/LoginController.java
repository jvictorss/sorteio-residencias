package gov.sorteio.controller;

import gov.sorteio.exceptions.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/login")
public class LoginController {
    @GetMapping
    public String showTela(){
        return "/modulos/login/form_login";
    }

    @GetMapping(path = "esqueci")
    public String showTelaEsqueci(){
        return "/modulos/login/form_esqueci";
    }

    @GetMapping("/error")
    public String errorLogin(HttpSession session) {
        ExceptionHandler erro = new ExceptionHandler("COD-0000", "Falha no Login", HttpStatus.UNAUTHORIZED,
                "Login e/ou Senha incorretos", "Unauthorized", null);
        session.setAttribute("error", erro);
        return "redirect:/login?exception";
    }
}
