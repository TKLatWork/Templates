package my.template.ctrl;

import com.google.gson.Gson;
import lombok.extern.java.Log;
import my.template.model.Hello;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class HelloCtrl {

    public static final String URL_HELLO = "/api/hello/public/get";

    @PostMapping(URL_HELLO)
    public Hello hello(@RequestBody Hello hello){
        log.info(URL_HELLO + ToStringBuilder.reflectionToString(hello));
        return new Hello("Server Echo", new Gson().toJson(hello));
    }

}
