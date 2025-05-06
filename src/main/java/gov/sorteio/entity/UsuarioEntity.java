package gov.sorteio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.sorteio.utils.Cpf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "usuario")
@SequenceGenerator(
        name = "sq_user_id",
        sequenceName = "sq_user_id",
        allocationSize = 1
)
public class UsuarioEntity extends BaseEntity implements UserDetails {
    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    @NotBlank
    @Cpf
    private String cpf;

    @NotNull
    @NotBlank
    private String sexo;

    @Column(unique = true)
    @NotNull
    @NotBlank
    @Email
    private String email;

    private String senha;

    @NotNull
    @NotBlank
    private String tipo;

    private String observacao;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.tipo.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.senha;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
