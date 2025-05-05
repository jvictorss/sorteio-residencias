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
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sorteio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Sorteio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nomeSorteado;

    @Column(nullable = false)
    private LocalDateTime dataSorteio;

    @Column(nullable = false)
    private UUID usuarioId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeSorteado() {
        return nomeSorteado;
    }

    public void setNomeSorteado(String nomeSorteado) {
        this.nomeSorteado = nomeSorteado;
    }

    public LocalDateTime getDataSorteio() {
        return dataSorteio;
    }

    public void setDataSorteio(LocalDateTime dataSorteio) {
        this.dataSorteio = dataSorteio;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}
