package wellsfargo.cto.eai.starter.greenfieldappreactive.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Value("${base.url}")
    private String BASE_URL;

    public static final int TIMEOUT = 5000;

    /**
     * Create {@link WebClient} instance using {@link HttpClient} instance
     * with Read and Write Timeouts. The connection timeout is configured
     * using ChannelOption.CONNECT_TIMEOUT_MILLIS. Read and Write timeouts
     * are configured using {@link ReadTimeoutHandler} and {@link WriteTimeoutHandler}
     * @return WebClient
     */
    @Bean
    public WebClient webClientWithTimeout() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * Create {@link WebClient} instance using {@link HttpClient} instance
     * with Read and Write Timeouts. The connection timeout is configured
     * using ChannelOption.CONNECT_TIMEOUT_MILLIS. Read and Write timeouts
     * are configured using {@link ReadTimeoutHandler} and {@link WriteTimeoutHandler}
     * @return WebClient
     */
    @Bean
    @Qualifier("webClientSleuth")
    public WebClient webClientSleuth() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
