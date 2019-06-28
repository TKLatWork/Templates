package my.template.config.auth;

import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@Log
public class MyUserDetailsService implements UserDetailsService {

    public static final String ADMIN = "admin";
    //"123"
    public static final String PWD = "$2a$10$35mkyLRF0y.3X6tweEsUHOZ.7eSjlxH4oR/nsOBKpAgGkQtmuqLy.";

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //正常是在这里查询用户信息
        if(ADMIN.equals(s)) {
            return new User(ADMIN, PWD, Collections.emptyList());
        }else{
            return null;
        }
    }
}
