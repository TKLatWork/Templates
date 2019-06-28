package my.template.config.jwt;

import lombok.Data;

@Data
public class JwtConfig {

    private Long expirationTime;
    private String secret;

}
