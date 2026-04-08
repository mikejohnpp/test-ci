package org.social.Dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotBlank(message = "ProductID không được trống")
    private String productID;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @Min(value = 0, message = "Tổng giá không được âm")
    private Long totalPrice;
}
