package wellsfargo.cto.eai.starter.greenfieldappreactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Address;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;

@RestController
public class CustomerController {

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
}
