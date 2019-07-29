# web服务模板

# 主要库

    spring-boot
    commons
    lombok
    json
    spring-test
    log4j2
    
# 账号系统
    
    JWT token认证
    登陆链
    JWTLoginFilter（WebSecurityConfig addFilterBefore "/login"）
        -> get
            -> authenticationManager(WebSecurityConfigurerAdapter) 
            -> authenticationProvider() 
            -> UserDetailsService(MyUserDetailsService)
        -> onSuccess onFail
            -> TokenAuthenticationService(genJwtToken)
    
    JWT AuthContext链
    JWTAuthenticationFilter（WebSecurityConfig addFilterBefore）
        -> TokenAuthenticationService(getAuthentication)