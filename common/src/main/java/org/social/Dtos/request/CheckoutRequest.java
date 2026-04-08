package org.social.Dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Tên khách hàng không được trống")
    @Size(min = 3, max = 100, message = "Tên phải từ 3 đến 100 ký tự")
    private String fullname;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được trống")

    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được trống")

    private String address;

    @NotEmpty(message = "Giỏ hàng không được trống")
    @Valid
    private List<OrderItemRequest> items;

    @Min(value = 0, message = "Phí không được âm")
    private Integer fee = 0;

    @Min(value = 0, message = "Giảm giá không được âm")
    private Integer discount = 0;

    private String note = "";
}
