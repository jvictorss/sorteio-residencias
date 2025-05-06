package gov.sorteio.controller;

import gov.sorteio.bo.UsuarioBO;
import gov.sorteio.dto.SenhaDTO;
import gov.sorteio.entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/v1/usuario")
public class UsuarioController extends BaseController<UsuarioEntity, Long> {

    private final UsuarioBO usuarioBO;

    @Autowired
    public UsuarioController(UsuarioBO usuarioBO) {
        super(usuarioBO);
        this.usuarioBO = usuarioBO;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> saveObject(@Valid @RequestBody UsuarioEntity objectDTO) throws Exception {
        objectDTO.setSenha(new BCryptPasswordEncoder().encode("123456"));
        Long id = baseBO.manterObject(objectDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping(path = "/editar")
    public String editarUsuario(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();
        model.addAttribute("userInfo", user);
        model.addAttribute("mudarUrl", true);
        if ("ROLE_ADMINISTRADOR".equals(user.getTipo())) {
            model.addAttribute("path", 2);
            model.addAttribute("path2", 1);
            model.addAttribute("editFNC", true);
        } else {
            model.addAttribute("path", 0);
        }
        model.addAttribute("usuario", baseBO.getObjectEntity(user.getId()));
        return "/modulos/usuario/form_usuario";
    }

    @PostMapping(value = "/senha")
    @ResponseBody
    public String alterarSenha(@Valid SenhaDTO senhaDTO) {
        return usuarioBO.alterarSenha(senhaDTO);
    }

    @PostMapping(value = "/recuperar")
    @ResponseBody
    public String recuperarSenha(@NotNull String email, HttpSession session) {
        return usuarioBO.recuperarLogin(email, session);
    }
}