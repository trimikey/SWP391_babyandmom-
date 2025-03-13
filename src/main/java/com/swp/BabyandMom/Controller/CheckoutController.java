package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.OrderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import com.swp.BabyandMom.Service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CheckoutController {

    private final PayOS payOS;
    private final OrderService orderService;

    @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> checkout(@RequestBody Map<String, Long> requestBody, HttpServletRequest request) {
        try {
            Long orderId = requestBody.get("orderId");
            if (orderId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing orderId"));
            }

            List<OrderResponseDTO> orderList = orderService.getOrderById(orderId);
            if (orderList.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
            }

            OrderResponseDTO order = orderList.get(0);
            int price = order.getTotalPrice().intValue();

            final String baseUrl = getBaseUrl(request);
            final String productName = "Membership Package";
            final String description = "Membership payment";

            orderService.updatePaymentStatus(orderId, PaymentStatus.PENDING);

            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder().name(productName).quantity(1).price(price).build();
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(price)
                    .description(description)
                    .returnUrl(baseUrl + "/api/order/payment-success/" + orderId)
                    .cancelUrl(baseUrl + "/api/order/cancel/" + orderId)
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return ResponseEntity.ok(Map.of("checkoutUrl", data.getCheckoutUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }


    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}
