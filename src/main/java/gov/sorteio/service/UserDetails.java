package gov.sorteio.service;

import gov.sorteio.entity.ColaboradorEntity;
import gov.sorteio.enums.ErrorCodesEnum;
import gov.sorteio.exceptions.GenericException;
import gov.sorteio.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetails implements UserDetailsService {

    private final ColaboradorRepository repository;

    @Autowired
    public UserDetails(ColaboradorRepository repository) {
        this.repository = repository;
    }

    @Override
    public ColaboradorEntity loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<ColaboradorEntity> user = repository.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException(email + " Não encontrado."));

        if("Inativo".equals(user.get().getAtivo())){
            throw GenericException.builder()
                    .code(ErrorCodesEnum.COD_USUARIO.getCodigo())
                    .title("Erro ao efetuar login")
                    .detail("Acesso inativo, favor entrar em contato com o seu RH.")
                    .trace(null)
                    .build();
        }

        return user.get();
    }
}