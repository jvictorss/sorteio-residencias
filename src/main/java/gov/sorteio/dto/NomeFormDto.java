package gov.sorteio.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NomeFormDto {
    @NotBlank(message = "A lista de nomes não pode estar vazia.")
    @Size(min = 1, message = "Insira pelo menos um nome.")
    private String nomes;

    public String getNomes() {
        return nomes;
    }

    public void setNomes(String nomes) {
        this.nomes = nomes;
    }
}