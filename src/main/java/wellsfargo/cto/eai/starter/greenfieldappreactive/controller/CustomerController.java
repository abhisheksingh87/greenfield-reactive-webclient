package wellsfargo.cto.eai.starter.greenfieldappreactive.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Address;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;
import wellsfargo.cto.eai.starter.greenfieldappreactive.service.CustomerService;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Customer> getCustomer(@PathVariable String id) {
        return Mono.just(Customer.builder()
                .customerId(id)
                .firstName("alex")
                .lastName("smith")
                .build());
    }

    @GetMapping(value = "/address/{zipCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Address> getAddress(@PathVariable String zipCode) {
        return Mono.just(Address.builder()
                .zipCode(zipCode)
                .street("7856 MontFort Drive")
                .aptNumber("1080")
                .city("Colarado Springs")
                .country("USA")
                .build());
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .retry(Duration.ofHours(2))
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }

    @GetMapping("/sleuth-customer-service")
    public Mono<String> sleuthCustomer() {
        log.info("inside sleuth-customer-service");
        return customerService.sleuthCustomerService();
    }
}
