package wellsfargo.cto.eai.starter.greenfieldappreactive.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import wellsfargo.cto.eai.starter.greenfieldappreactive.GreenfieldAppReactiveWebclientApplication;
import wellsfargo.cto.eai.starter.greenfieldappreactive.model.Customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = GreenfieldAppReactiveWebclientApplication.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testGetCustomerById() {
        //given

        //when
        Mono<Customer> customer = customerService.getCustomerById("123");

        //then
        StepVerifier
                .create(customer)
                .assertNext(cust -> {
                    assertThat("alex").isEqualTo(cust.getFirstName());
                    assertThat(cust.getCustomerId()).isNotNull();
                })
                .expectComplete()
                .verify();

    }

    @Test
    public void getCustomerByIdSync() {
        //given
        Customer expectedCustomer = Customer.builder()
                .customerId("123")
                .firstName("alex")
                .lastName("smith")
                .build();
        //when
        Customer actualCustomer = customerService.getCustomerByIdSync("123");

        //then

        Assertions.assertThat(actualCustomer).usingRecursiveComparison().isEqualTo(actualCustomer);
    }

    @Test
    public void getCustomerWithRetry() {
        //given

        //when
        assertThrows(IllegalStateException.class, () -> {
            customerService.getCustomerWithRetry("123");
        });

    }

    @Test
    public void getCustomerWithFallBack() {
        //given

        //when
        Customer customer = customerService.getCustomerWithFallBack("123");

        //then

        assertThat(customer).usingRecursiveComparison().isEqualTo(Customer.builder().build());
    }

    @Test
    public void getCustomerWithErrorHandling() {
        //given

        //when
        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerWithRetry("123");
        });
    }
}
