package my.template.config;

import lombok.Data;
import my.template.config.jwt.JwtConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {

    private String name;
    private JwtConfig jwt;

}
