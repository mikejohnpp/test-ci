package org.social.Dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    @JsonProperty("orderID")
    private Integer orderID;

    @JsonProperty("message")
    private String message;

    @JsonProperty("totalAmount")
    private Long totalAmount;
}
