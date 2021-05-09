package wellsfargo.cto.eai.starter.greenfieldappreactive.model;


import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Address {

    private String city;
    private String aptNumber;
    private String street;
    private String country;
    private String State;
    private String zipCode;
}
