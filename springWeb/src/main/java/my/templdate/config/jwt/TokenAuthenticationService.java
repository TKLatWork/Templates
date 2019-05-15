package my.templdate.config.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import my.templdate.model.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log
@ConfigurationProperties(prefix = "app.jwt")
public class TokenAuthenticationService {

    static private final String TOKEN_PREFIX = "Bearer";        // Token前缀
    static public final String HEADER_STRING = "Authorization";// 存放Token的Header Key

    private Long expirationTime;
    private String secret;


    public JWTTokenInfo genTokenInfo(String username, Authentication auth){
        //jwt的长度是有限制的
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        // 生成JWT
        String jwt = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", String.join(",", authorities))
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(expiration)
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return new JWTTokenInfo(jwt, expiration.getTime());
    }

    // JWT生成方法
    public void addAuthentication(HttpServletResponse response, String username, Authentication auth) {
        JWTTokenInfo tokenInfo = genTokenInfo(username, auth);
        // 将 JWT 写入 body
        try {
            ApiResult result = new ApiResult<>(HttpServletResponse.SC_OK, null, tokenInfo);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().println(new Gson().toJson(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JWT验证方法
    public Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(HEADER_STRING);
        //对于无法提交header的时候
        if(StringUtils.isBlank(token)){
            token = request.getParameter(HEADER_STRING);
        }
        Authentication authentication = null;
        if (token != null) {
            try {
                authentication = getAuth(token);
            }catch (Exception e){
                log.info("JWT Token:" + token + "," + e.getMessage());
            }
        }
        return authentication;
    }

    private Authentication getAuth(String token){
        Claims claims = Jwts.parser()
                // 验签
                .setSigningKey(secret)
                // 去掉 Bearer
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
        String user = claims.getSubject();
        // 得到 权限（角色）
        List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
        UsernamePasswordAuthenticationToken authenticationToken = null;
        if(user != null){
            authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            //可以在这里把用户身份丢进去
            //authenticationToken.setDetails(AccountDataUtilFactory.getByAccountName(user));
        }
        return authenticationToken;
    }
}