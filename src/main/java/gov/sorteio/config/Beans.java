package gov.sorteio.config;

import gov.sorteio.dto.RodapeDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    @Bean
    @ConfigurationProperties(prefix = "rodape")
    public RodapeDTO rodapeDTO() {
        return new RodapeDTO();
    }
}
