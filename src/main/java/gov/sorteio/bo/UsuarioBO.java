package gov.sorteio.bo;

import gov.sorteio.dto.SenhaDTO;
import gov.sorteio.dto.UsuarioDTO;
import gov.sorteio.entity.UsuarioEntity;
import gov.sorteio.enums.ErrorCodesEnum;
import gov.sorteio.exceptions.InvalidRequestException;
import gov.sorteio.repository.UsuarioRepository;
import gov.sorteio.service.EnviarEmail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

import static gov.sorteio.constants.Constants.ERROR;
import static gov.sorteio.constants.Constants.ROTA_EXCEPTION;
import static gov.sorteio.constants.Constants.TITLE_GENERIC;
import static gov.sorteio.exceptions.MensagemError.gerarErrorDTO;

@Component
public class UsuarioBO extends BaseBO<UsuarioEntity, Long> {
    private static final String ROTA_BASE = "redirect:/v1/painel/usuario/";
    private static final String ROTA_BACK = "redirect:/v1/usuario/";
    private static final String ROTA_SUCESS = "?success=true";
    private static final String CODIGO = ErrorCodesEnum.COD_USUARIO.getCodigo();

    private final UsuarioRepository usuarioRepository;
    private final EnviarEmail serviceEmail;
    private final ModelMapper mapper;

    @Autowired
    public UsuarioBO(UsuarioRepository usuarioRepository, EnviarEmail serviceEmail) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.serviceEmail = serviceEmail;
        this.mapper = new ModelMapper();
    }

    public UsuarioDTO getLogadoDTO(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity user = (UsuarioEntity) auth.getPrincipal();
        return mapper.map(user,UsuarioDTO.class);
    }

    public String alterarSenha(SenhaDTO senhaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();
        boolean validaSenhaAtual = new BCryptPasswordEncoder().matches(senhaDTO.getAtual(), usuario.getSenha());
        if (!validaSenhaAtual) {
            throw InvalidRequestException.builder()
                    .code("ERR-004")
                    .title("Erro ao realizar ALTERAÇÃO da SENHA.")
                    .detail("Senha atual não confere.")
                    .build();
        }
        usuario.setSenha(new BCryptPasswordEncoder().encode(senhaDTO.getNova()));
        usuarioRepository.save(usuario);
        return "/sorteio/logout";
    }

    public String recuperarLogin(String email, HttpSession session) {
        Optional<UsuarioEntity> funcionario = usuarioRepository.findByEmail(email);
        if (funcionario.isPresent()) {
            funcionario.get().setSenha(new BCryptPasswordEncoder().encode("123456"));
            usuarioRepository.save(funcionario.get());
            String titulo = "Recuperação de Login!";
            String mensagem = "Olá <b>" + funcionario.get().getNome() + "</b>," + "<br />\r\n" + "\t<br /> Solicitação de recuperação de acesso realizada com sucesso!\r\n" + "\t<br />\r\n" + "\t<br />\r\n" + "\t<br /> Atenciosamente,\r\n" + "\t<br />\r\n" + "\t<br /> Núcleo de TI\r\n" + "\t<br />\r\n" + "\t<div style='background: #a3ecd3; font-size: 12px; padding: 4px;'>\r\n" + "\t\t<div style=\"border-bottom: 1px solid #FD0; margin-bottom: 2px;\">\r\n" + "\t\t\t<h3>Senha provisória para acessar o sistema:</h3>\r\n" + "\t\t</div>\r\n" + "\t\t<div style=\"padding: 10px;\">\r\n" + "\t\t\tSenha: 123456.\r\n" + "\t</div>\r\n" + "\t<br />\r\n" + "\t<div style='background: #fd0; font-size: 12px; padding: 8px;'>Está é uma mensagem automática. Por favor não responda.</div>";
            String msgError = "Falha no processo de recuperação da senha. Favor confirmar o email informado e tentar novamente em instantes";
            return enviarEmail(email, titulo, mensagem, msgError, session);
        }
        String msg = "Usuário não encontrado no Banco de Dados para recuperação de senha.";
        session.setAttribute(ERROR, gerarErrorDTO(CODIGO, TITLE_GENERIC, msg));
        return "/sorteio/v1/login".concat(ROTA_EXCEPTION);
    }

    public String enviarEmail(String email, String titulo, String mensagem, String msgError, HttpSession session) {
        String envio = serviceEmail.enviar(email, titulo, mensagem);
        if(Objects.isNull(envio)){
            session.setAttribute(ERROR, gerarErrorDTO(ErrorCodesEnum.COD_EMAIL.getCodigo(),TITLE_GENERIC,msgError));
            return "/sorteio/v1/login".concat(ROTA_EXCEPTION);
        }
        return "/sorteio/v1/login";
    }
}
