package gov.sorteio.controller;

import gov.sorteio.entity.SorteadoEntity;
import gov.sorteio.repository.SorteadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResultadoController {

    private final SorteadoRepository sorteadoRepository;

    @GetMapping("/resultado/list_resultado")
    public String listarResultados(Model model) {
        List<SorteadoEntity> lista = sorteadoRepository.findAll();
        model.addAttribute("lista", lista);
        return "modulos/resultado/list_resultado";
    }
}
