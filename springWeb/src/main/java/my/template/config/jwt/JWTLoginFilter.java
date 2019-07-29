package my.template.config.jwt;

import com.google.gson.Gson;
import my.template.model.account.Account;
import my.template.model.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抓JWT登陆的过滤器
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger LOG = LoggerFactory.getLogger(JWTLoginFilter.class);

    private TokenAuthenticationService authenticationService;

    public JWTLoginFilter(String url, AuthenticationManager authManager,
                          TokenAuthenticationService authenticationService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException{
        Account accountJWTInfo = new Gson().fromJson(req.getReader(), Account.class);
        String username = "";
        String password = "";

        if(accountJWTInfo == null
                || StringUtils.isBlank(accountJWTInfo.getUsername())
                || StringUtils.isBlank(accountJWTInfo.getPassword())){
            LOG.info("/login, EmptyRequest" );
            throw new BadCredentialsException("EmptyRequest");
        } else {
            username = accountJWTInfo.getUsername();
            password = accountJWTInfo.getPassword();
            LOG.info("/login, processing username:" + username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 获得验证对象，这个在WebSecurityConfig里配置
            Authentication authentication = getAuthenticationManager().authenticate(authenticationToken);
            //在这添加具体的账户检查
            return authentication;
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth){
        authenticationService.addAuthentication(res, auth.getName(), auth);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ApiResult result = new ApiResult<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), failed.getMessage(), null);
        //可能的编码问题
        LOG.info("/login, Fail reason:{}", failed.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(result));
    }
}
