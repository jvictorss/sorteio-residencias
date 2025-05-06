package gov.sorteio.controller;

import gov.sorteio.dto.RodapeDTO;
import gov.sorteio.exceptions.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/v1/login")
public class LoginController {

    private final RodapeDTO rodapeDTO;

    public LoginController(RodapeDTO rodapeDTO) {
        this.rodapeDTO = rodapeDTO;
    }

    @GetMapping
    public String showTela(HttpSession session, Model model) {
        session.setAttribute("rodape", rodapeDTO);
        model.addAttribute("theme", "core2.css");
        model.addAttribute("themeBord", "theme-default.css");
        return "/modulos/login/form_login";
    }

    @GetMapping(path = "/esqueci")
    public String showTelaEsqueci(HttpSession session, Model model) {
        session.setAttribute("rodape", rodapeDTO);
        model.addAttribute("theme", "core2.css");
        model.addAttribute("themeBord", "theme-default.css");
        return "/modulos/login/form_esqueci";
    }

    @GetMapping("/error")
    public String errorLogin(HttpSession session) {
        ExceptionHandler erro = new ExceptionHandler("COD-0000", "Falha no Login", HttpStatus.UNAUTHORIZED,
                "Login e/ou Senha incorretos", "Unauthorized", null);
        session.setAttribute("error", erro);
        return "/modulos/login/form_esqueci";
    }
}