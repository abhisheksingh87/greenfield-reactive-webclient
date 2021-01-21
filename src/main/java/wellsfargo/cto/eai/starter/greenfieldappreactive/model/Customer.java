package wellsfargo.cto.eai.starter.greenfieldappreactive.model;

import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Customer {

    private String customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
