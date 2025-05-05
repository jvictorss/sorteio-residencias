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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity()
@Table(name = "colaborador")
public class ColaboradorEntity extends BaseEntity implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "nome")
    @NotNull
    @NotBlank
    private String nome;

    @Column(name = "cpf")
    @NotNull
    @NotBlank
    @Cpf
    private String cpf;

    @Column(name = "email", unique = true)
    @NotNull
    @NotBlank
    private String email;

    @Column(name = "senha")
    @NotNull
    @NotBlank
    private String senha;

    @Column(name = "sexo")
    @NotNull
    @NotBlank
    private String sexo;

    @Column(name = "observacao")
    private String observacao;

    @Transient
    private String token;

    @Column(name = "tipo")
    @NotNull
    @NotBlank
    private String tipo;

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
