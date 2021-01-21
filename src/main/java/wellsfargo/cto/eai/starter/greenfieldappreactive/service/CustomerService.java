package wellsfargo.cto.eai.starter.greenfieldappreactive.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private static final String CUSTOMER_URL_TEMPLATE = "/customer/{id}";
    private static final String BROKEN_URL_TEMPLATE = "/customer-broken/{id}";

    public static final int DELAY_MILLIS = 100;
    public static final int MAX_RETRY_ATTEMPTS = 3;

    private final WebClient webClient;

    public Mono<Customer> getCustomerById(final String id) {
        return webClient
                .get()
                .uri(CUSTOMER_URL_TEMPLATE, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Customer getCustomerByIdSync(final String id) {
        return webClient
                .get()
                .uri(CUSTOMER_URL_TEMPLATE, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class)
                .block();
    }

    public Customer getCustomerWithRetry(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Customer.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public Customer getCustomerWithFallBack(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Customer.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new Customer()))
                .block();
    }

    public Customer getCustomerWithErrorHandling(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Customer.class)
                .block();
    }
}