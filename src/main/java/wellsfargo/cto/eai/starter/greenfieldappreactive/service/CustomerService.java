package wellsfargo.cto.eai.starter.greenfieldappreactive.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Address;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.CustomerWithZipCode;

import java.time.Duration;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private static final String CUSTOMER_URL_TEMPLATE = "/customer/{id}";
    private static final String BROKEN_URL_TEMPLATE = "/customer-broken/{id}";

    public static final int DELAY_MILLIS = 100;
    public static final int MAX_RETRY_ATTEMPTS = 3;

    private final WebClient webClientWithTimeout;

    private final WebClient webClientSleuth;

    public Flux<ServerSentEvent<String>> consumeServerSentEvent() {
         ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        return webClientWithTimeout.get()
                .uri("/stream-sse")
                .retrieve()
                .bodyToFlux(type);

    }

    /**
     * The below method uses {@link WebClient} to retrieve
     * Customer object Asynchronously using {@link Mono}
     * @param id
     * @return Mono<Customer>
     */
    public Mono<Customer> getCustomerById(final String id) {
        return webClientWithTimeout
                .get()
                .uri(CUSTOMER_URL_TEMPLATE, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    /**
     * The below method uses {@link WebClient} to retrieve
     * Customer object Synchronously using {@link Mono#block()}
     * @param id
     * @return Mono<Customer>
     */
    public Customer getCustomerByIdSync(final String id) {
        return webClientWithTimeout
                .get()
                .uri(CUSTOMER_URL_TEMPLATE, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class)
                .block();
    }

    /**
     * Retrieves Customer object with {@link WebClient}
     * Synchronously using {@link Mono#block()}
     * and It also configures {@link Retry} If there is issue
     * while calling services
     * @param id
     * @return Mono<Customer>
     */
    public Customer getCustomerWithRetry(final String id) {
        return webClientWithTimeout
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Customer.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    /**
     * Retrieves Customer object using {@link WebClient}
     * Synchronously using {@link Mono#block()}
     * and It also configures fallback method if service returns
     * an error.
     * @param id
     * @return Mono<Customer>
     */
    public Customer getCustomerWithFallBack(final String id) {
        return webClientWithTimeout
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(Customer.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new Customer()))
                .block();
    }

    public Mono<Address> getAddress(String zipCode) {
        return webClientWithTimeout.get()
                .uri("/address/{zipCode}", zipCode)
                .retrieve()
                .bodyToMono(Address.class);
    }


    /**
     * Retrieves Flux of Customers using ParallelFlux.
     * Parallel() should be used together with runOn method.
     * @param customerIds
     * @return Flux<Customer>
     */
    public Flux<Customer> getCustomers(List<String> customerIds) {
        return Flux.fromIterable(customerIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::getCustomerById)
                .ordered((u1, u2) -> Integer.parseInt(u2.getCustomerId()) - Integer.parseInt(u1.getCustomerId()));
    }

    /**
     * Retrieves Mono of CustomerWithZipCode By combining.
     * Mono of Customer and Monog of Address
     * @param customerId
     * @param zipCode
     * @return Mono<CustomerWithZipCode>
     */
    public Mono<CustomerWithZipCode> getCustomerWithAddress(String customerId, String zipCode) {
        Mono<Customer> customer = getCustomerById(customerId);
        Mono<Address> address = getAddress(zipCode);

        return Mono.zip(customer, address, (customer1, address1) -> new CustomerWithZipCode(customer1, address1));
    }

    public Mono<String> sleuthCustomerService() {
        return webClientSleuth
                .get()
                .uri("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }
}