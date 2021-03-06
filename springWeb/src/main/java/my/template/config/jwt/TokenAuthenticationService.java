package my.template.config.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import my.template.config.AppConfig;
import my.template.model.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class TokenAuthenticationService {

    static private final String TOKEN_PREFIX = "Bearer";        // Token前缀
    static public final String HEADER_STRING = "Authorization";// 存放Token的Header Key

    @Autowired
    private AppConfig appConfig;

    public JWTTokenInfo genTokenInfo(String username, Authentication auth){
        //jwt的长度是有限制的
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Date expiration = new Date(System.currentTimeMillis() + appConfig.getJwt().getExpirationTime());
        // 生成JWT
        String jwt = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", String.join(",", authorities))
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(expiration)
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, appConfig.getJwt().getSecret())
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
            log.info("reply login:" + e.getMessage());
        }
    }

    // 尝试从JWT信息生成auth对象
    public Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(HEADER_STRING);
        //对于无法提交header的时候
        if(StringUtils.isBlank(token)){
            token = request.getParameter(HEADER_STRING);
        }
        return genAuthWithJwtToken(token);
    }

    private Authentication genAuthWithJwtToken(String token){
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            Claims claims = Jwts.parser()
                    // 验签
                    .setSigningKey(appConfig.getJwt().getSecret())
                    // 去掉 Bearer
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = claims.getSubject();
            // 得到 权限（角色）
            List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            if(user != null){
                authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
                //可以在这里把用户身份丢进去
                //authenticationToken.setDetails(AccountDataUtilFactory.getByAccountName(user));
            }
        }catch (Exception e){
            log.warn("Gen Auth For jwtToken Fail:" + e.getMessage(), e);
        }
        return authenticationToken;
    }
}