package gov.sorteio.controller;

import gov.sorteio.bo.BaseBO;
import gov.sorteio.dto.SenhaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.Locale;

import static gov.sorteio.constants.Constants.entidades;
import static gov.sorteio.constants.Constants.listEntidades;
import static gov.sorteio.constants.Constants.listModels;
import static gov.sorteio.constants.Constants.pathPai;

@Controller
@RequestMapping(path = "/v1/painel")
public class PainelController {

    @GetMapping
    public String showTelaPrincipal(Model model, HttpSession session){
        model.addAttribute("lk1",true);
        session.setAttribute("nameApi", "Gerenciamento de Sorteio");
        System.out.println("Session nameAPI: " + session.getAttribute("nameAPI"));
        return "/modulos/home/form_dashboard";
    }

    @GetMapping(path = "editar-senha")
    public String showTelaEditarSenha(HttpSession session, Model model) {
        model.addAttribute("senhaDTO", new SenhaDTO());
        session.setAttribute("nameApi", "Gerenciamento de Sorteio");
        return "/modulos/config/form_senha";
    }

    @GetMapping(path = "/{servico}/form")
    public String showTela(@NotBlank @PathVariable("servico") String servico, Model model, HttpSession session){
        model.addAttribute("lk"+pathPai.get(servico),true);
        model.addAttribute("lk_"+servico,true);
        model.addAttribute("metodo", "POST");
        model.addAttribute("usaRetorno", true);
        Object object = entidades.get(servico);
        model.addAttribute(servico, object);
        Class<?> classe = object.getClass();
        Field[] campos = classe.getDeclaredFields();
        for (Field campo : campos) {
            try {
                campo.setAccessible(true);
                Class<?> type = campo.getType();
                if(type.getSimpleName().contains("Entity")) {
                    String entity = type.getSimpleName().replace("Entity","").toLowerCase(Locale.ENGLISH);
                    BaseBO et = (BaseBO) listEntidades.get(entity);
                    model.addAttribute(listModels.get(entity), et.getAll());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return "/modulos/"+servico+"/form_"+servico;
    }

    @GetMapping(path = "/{servico}/list")
    public String showTelaList(@NotBlank @PathVariable("servico") String servico, Model model){
        model.addAttribute("lk"+pathPai.get(servico),true);
        model.addAttribute("lk_"+servico,true);

        return "/modulos/"+servico+"/list_"+servico;
    }
}

