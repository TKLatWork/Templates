package my.templdate.ctrl;

import lombok.extern.java.Log;
import my.templdate.model.Hello;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class HelloCtrl {

    public static final String URL_HELLO = "/api/hello";

    @PostMapping(URL_HELLO)
    public Hello hello(Hello hello){
        log.info(URL_HELLO + ToStringBuilder.reflectionToString(hello));
        return hello;
    }

}
