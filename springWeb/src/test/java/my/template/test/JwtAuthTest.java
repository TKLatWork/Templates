package my.template.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import my.template.App;
import my.template.config.jwt.JWTTokenInfo;
import my.template.model.account.Account;
import my.template.model.common.ApiResult;
import my.template.test.util.TestClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;

import static my.template.config.auth.MyUserDetailsService.ADMIN;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@Slf4j
//随机端口独立执行，或者正常执行
@SpringBootTest(classes = App.class)//, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
public class JwtAuthTest {

    public static final String URL_LOGIN = "/login";

    private RestTemplate rt = TestClientUtil.getRestTemplate();

//    @LocalServerPort
    int randomServerPort = 8080;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void gen() {
        String pwd = passwordEncoder.encode("123");
        log.info(pwd);
    }

    @Test
    public void loginTest(){
        Account request = new Account(ADMIN, "123");
        String reply = rt.postForEntity(
                TestClientUtil.getURL(randomServerPort, URL_LOGIN),
                request, String.class).getBody();
        //泛型在这很麻烦
        Type type = new TypeToken<ApiResult<JWTTokenInfo>>(){}.getType();
        ApiResult<JWTTokenInfo> result = new Gson().fromJson(reply, type);
        //检查
        assertThat(result.getResult().getToken(), is(notNullValue()));
    }

    @Test
    public void loginFailTest(){
        Account request = new Account("whatever", "xxx");
        String reply = rt.postForEntity(
                TestClientUtil.getURL(randomServerPort, URL_LOGIN),
                request, String.class).getBody();
        //泛型在这很麻烦
        Type type = new TypeToken<ApiResult<JWTTokenInfo>>(){}.getType();
        ApiResult<JWTTokenInfo> result = new Gson().fromJson(reply, type);
        //检查
        assertThat(result.getStatus(), is(not(HttpServletResponse.SC_OK)));
    }

}
