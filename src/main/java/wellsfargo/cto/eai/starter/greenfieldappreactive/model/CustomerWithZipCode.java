package wellsfargo.cto.eai.starter.greenfieldappreactive.model;

import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerWithZipCode {
    private Customer customer;
    private Address address;
}
