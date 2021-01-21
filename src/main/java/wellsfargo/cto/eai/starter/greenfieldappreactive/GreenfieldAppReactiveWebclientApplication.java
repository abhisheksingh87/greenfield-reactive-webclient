package wellsfargo.cto.eai.starter.greenfieldappreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;

@SpringBootApplication
public class GreenfieldAppReactiveWebclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenfieldAppReactiveWebclientApplication.class, args);
    }



}
