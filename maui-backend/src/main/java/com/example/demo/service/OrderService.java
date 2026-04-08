package com.example.demo.service;

import org.social.Dtos.request.CheckoutRequest;
import org.social.Dtos.request.OrderItemRequest;
import org.social.Dtos.response.CheckoutResponse;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.social.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final Integer PROCESSING_STATUS_ID = 2;
    private static final Integer COD_PAYMENT_ID = 1;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            System.out.println(u.getUsername());
            User user  =  userRepository.findByEmail(u.getUsername())
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Người dùng không tồn tại");
                    });

            // 2. Lấy trạng thái đơn hàng (Đang xử lí)
            OrderStatus status = orderStatusRepository.findById(PROCESSING_STATUS_ID)
                    .orElseThrow(() -> {
                        log.error("OrderStatus not found with ID: {}", PROCESSING_STATUS_ID);
                        return new ResourceNotFoundException("Trạng thái đơn hàng không tồn tại");
                    });

            // 3. Lấy phương thức thanh toán (COD)
            Payment payment = paymentRepository.findById(COD_PAYMENT_ID)
                    .orElseThrow(() -> {
                        log.error("Payment not found with ID: {}", COD_PAYMENT_ID);
                        return new ResourceNotFoundException("Phương thức thanh toán không tồn tại");
                    });

            // 4. Validate sản phẩm và tính tổng giá
            Long totalPrice = 0L;

            for (OrderItemRequest item : request.getItems()) {
                Product product = productRepository.findById(item.getProductID())
                        .orElseThrow(() -> {
                            log.error("Product not found with ID: {}", item.getProductID());
                            return new ResourceNotFoundException(
                                    "Sản phẩm " + item.getProductID() + " không tồn tại");
                        });

                // Kiểm tra tồn kho
                if (product.getQuanlityStock() < item.getQuantity()) {
                    log.warn("Insufficient stock for product: {}, requested: {}, available: {}",
                            item.getProductID(), item.getQuantity(), product.getQuanlityStock());
                    throw new IllegalArgumentException(
                            "Sản phẩm '" + product.getName() + "' không đủ số lượng. " +
                                    "Còn lại: " + product.getQuanlityStock() + " cái");
                }

                totalPrice += item.getTotalPrice();
                log.info("Validated product: {}, quantity: {}, price: {}",
                        item.getProductID(), item.getQuantity(), item.getTotalPrice());
            }

            // 5. Tính giá cuối cùng (tổng + phí - giảm giá)
            Long finalPrice = totalPrice + (request.getFee() != null ? request.getFee() : 0)
                    - (request.getDiscount() != null ? request.getDiscount() : 0);

            if (finalPrice < 0) {
                log.error("Final price is negative: {}", finalPrice);
                throw new IllegalArgumentException("Giá cuối cùng không được âm");
            }

            // 6. Tạo đơn hàng
            Order order = new Order();
            order.setUserID(user);
            order.setFullname(request.getFullname());
            order.setEmail(request.getEmail());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setAddress(request.getAddress());
            order.setOrderStatusID(status);
            order.setPaymentID(payment.getId());
            order.setTotalPrice(BigDecimal.valueOf(finalPrice));
            order.setCreateAt(LocalDate.now());
            order.setFee(request.getFee() != null ? request.getFee() : 0);
            order.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0);
            order.setNote(request.getNote() != null ? request.getNote() : "");

            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully with ID: {}, total price: {}", savedOrder.getId(), finalPrice);

            // 7. Tạo chi tiết đơn hàng và cập nhật tồn kho
            for (OrderItemRequest item : request.getItems()) {
                Product product = productRepository.findById(item.getProductID())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Sản phẩm " + item.getProductID() + " không tồn tại"));

                // Tạo OrderDetailId
                OrderDetailId detailId = new OrderDetailId();
                detailId.setOrderID(savedOrder.getId());
                detailId.setProductID(item.getProductID());

                // Tạo OrderDetail
                OrderDetail detail = new OrderDetail();
                detail.setId(detailId);
                detail.setOrderID(savedOrder);
                detail.setProductID(product);
                detail.setQuantity(item.getQuantity());
                detail.setTotalPrice(BigDecimal.valueOf(item.getTotalPrice()));

                orderDetailRepository.save(detail);
                log.info("OrderDetail created: order={}, product={}, quantity={}",
                        savedOrder.getId(), item.getProductID(), item.getQuantity());

                // Cập nhật tồn kho sản phẩm
                product.setQuanlityStock(product.getQuanlityStock() - item.getQuantity());
                product.setQuanlitySell(product.getQuanlitySell() + item.getQuantity());
                productRepository.save(product);
                log.info("Product stock updated: {}, new stock: {}, new sell: {}",
                        item.getProductID(), product.getQuanlityStock(), product.getQuanlitySell());
            }

            log.info("Checkout completed successfully for order ID: {}", savedOrder.getId());

            return new CheckoutResponse(
                    savedOrder.getId(),
                    "Đơn hàng được tạo thành công",
                    finalPrice
            );

        } catch (NumberFormatException e) {
            log.error("Invalid phone number format", e);
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Checkout failed with error", e);
            throw new RuntimeException("Thanh toán thất bại. Vui lòng thử lại");
        }
    }

    public Order getOrderById(Integer orderId) {
        log.info("Fetching order with ID: {}", orderId);
        return orderRepository.findOrderById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new ResourceNotFoundException("Đơn hàng không tồn tại");
                });
    }

    public List<Order> getUserOrders(Integer userId) {
        log.info("Fetching all orders for user ID: {}", userId);
        return orderRepository.findByUserID_Id(userId);
    }
}

