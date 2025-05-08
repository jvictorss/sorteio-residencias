package gov.sorteio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lottery")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class LotteryEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private Integer quantidadeSorteada;

    @Column(nullable = false)
    private Integer quantidadeParticipantes;

    @Column(nullable = false)
    private String nomeSorteio;

    @Column(nullable = false)
    private LocalDateTime dataSorteio;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(nullable = false)
    private String sorteados;
}
