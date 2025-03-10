package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import com.swp.BabyandMom.Service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CheckoutController {

    private final PayOS payOS;
    private final OrderService orderService;

    @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void checkout(@RequestParam Long orderId, HttpServletRequest request, HttpServletResponse response) {
        try {
            final String baseUrl = getBaseUrl(request);
            final String productName = "Membership Package";
            final String description = "Payment for membership order";
            final String returnUrl = baseUrl + "/success";
            final String cancelUrl = baseUrl + "/cancel";
            final int price = 2000;
            orderService.updatePaymentStatus(orderId, PaymentStatus.PENDING);

            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
            ItemData item = ItemData.builder().name(productName).quantity(1).price(price).build();
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(price)
                    .description(description)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            response.setHeader("Location", data.getCheckoutUrl());
            response.setStatus(302);
        } catch (Exception e) {
            e.printStackTrace();
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
