package my.templdate.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenInfo {
    private String token;
    private Long expireDateTime;
}
