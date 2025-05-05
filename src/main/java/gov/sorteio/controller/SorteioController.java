package gov.sorteio.controller;
import gov.sorteio.dto.NomeFormDto;
import gov.sorteio.dto.SorteioDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
public class SorteioController {

    private final List<SorteioDto> historico = new ArrayList<>();
    private String nomeSorteado = null;

    @GetMapping("/")
    public String exibirFormulario(Model model) {
        model.addAttribute("nomeFormDto", new NomeFormDto());
        model.addAttribute("historico", historico);
        model.addAttribute("nomeSorteado", nomeSorteado);
        return "/modulos/sorteio";
    }

    @PostMapping("/sortear")
    public String sortearNome(@Valid @ModelAttribute("nomeForm") NomeFormDto nomeFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("historico", historico);
            model.addAttribute("nomeSorteado", nomeSorteado);
            return "/modulos/sorteio";
        }

        List<String> nomesList = Arrays.asList(nomeFormDto.getNomes().split("\\r\\n|\\r|\\n"));
        if (!nomesList.isEmpty()) {
            sortear(nomesList);
        } else {
            model.addAttribute("mensagemErro", "A lista de nomes está vazia.");
        }
        NomeFormDto newForm = new NomeFormDto();
        newForm.setNomes(nomeFormDto.getNomes().replaceAll(nomeSorteado.concat("\\r\\n"),""));
        model.addAttribute("historico", historico);
        model.addAttribute("nomeSorteado", nomeSorteado);
        model.addAttribute("nomeForm", newForm);
        return "/modulos/sorteio";
    }

    private void sortear(List<String> nomesList) {
        Random random = new Random();
        int indiceSorteado = random.nextInt(nomesList.size());
        nomeSorteado = nomesList.get(indiceSorteado).trim();
        if(historico.stream().noneMatch(sorteioDto -> sorteioDto.getNomeSorteado().equalsIgnoreCase(nomeSorteado))){
            historico.add(new SorteioDto(nomeSorteado, LocalDateTime.now()));
            return;
        }
        sortear(nomesList);
    }
}