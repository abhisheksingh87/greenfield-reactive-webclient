package wellsfargo.cto.eai.starter.greenfieldappreactive;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GreenfieldAppReactiveWebclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenfieldAppReactiveWebclientApplication.class, args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.alwaysOn();
    }

}
