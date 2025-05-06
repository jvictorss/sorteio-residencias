package gov.sorteio.controller;

import gov.sorteio.bo.SorteioBO;
import gov.sorteio.dto.NomeFormDto;
import gov.sorteio.dto.RodapeDTO;
import gov.sorteio.dto.SorteioFormDto;
import gov.sorteio.dto.SorteioRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/sorteio")
@Slf4j
public class SorteioController {

    @Autowired
    private SorteioBO sorteioBO;

    private final List<SorteioRequest> historico = new ArrayList<>();
    @Autowired
    private RodapeDTO rodapeDTO;

    @GetMapping("/")
    public String exibirFormulario(HttpSession session, Model model) {
        session.setAttribute("rodape", rodapeDTO);
        model.addAttribute("theme", "core2.css");
        model.addAttribute("themeBord", "theme-default.css");
        model.addAttribute("sorteioForm", new SorteioFormDto());
        model.addAttribute("historico", historico);
        return "/modulos/sortear/sortear";
    }

    @PostMapping("/sortear")
    public String sortear(@Valid @ModelAttribute("sorteioForm") SorteioRequest sorteioRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mensagemErro", "Erro nos dados do formulário");
            return "/modulos/sortear/sortear";
        }

        try {
            List<NomeFormDto> participantes = new ArrayList<>();
            for (String linha : sorteioRequest.getParticipantes().split("\\n")) {
                String[] dados = linha.split(",");
                if (dados.length == 2) {
                    NomeFormDto participante = new NomeFormDto();
                    participante.setNome(dados[0].trim());
                    participante.setCpf(dados[1].trim());
                    participantes.add(participante);
                } else {
                    model.addAttribute("mensagemErro", "Formato inválido na lista de participantes. Cada linha deve conter 'Nome,CPF'.");
                    return "/modulos/sortear/sortear";
                }
            }

            var sorteioDto = new SorteioFormDto();
            sorteioDto.setQuantidadeSorteio(sorteioRequest.getQuantidadeSorteio());
            sorteioDto.setSorteio(sorteioRequest.getSorteio());

            sorteioDto.setParticipantes(participantes);

            var sorteados = sorteioBO.realizarSorteio(sorteioDto);
            log.info("Sorteados: {}", sorteados);
            model.addAttribute("mensagemSucesso", "Sorteio realizado com sucesso!");
            model.addAttribute("sorteados", sorteados);
            return "/modulos/sortear/sorteio_concluido";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao realizar o sorteio: " + e.getMessage());
        }

        return "/modulos/sortear/sortear";
    }
}