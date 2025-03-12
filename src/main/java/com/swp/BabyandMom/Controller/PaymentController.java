package com.swp.BabyandMom.Controller;


import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import com.swp.BabyandMom.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PayOS payOS;
    private final OrderService orderService; 

    public PaymentController(PayOS payOS, OrderService orderService) {
        this.payOS = payOS;
        this.orderService = orderService;
    }

    @PostMapping(path = "/payos_transfer_handler")
    public ResponseEntity<ObjectNode> payosTransferHandler(@RequestBody ObjectNode body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);

            Long orderId = data.getOrderCode();
            String status = body.get("status").asText();
            if ("COMPLETED".equalsIgnoreCase(status)) {
                orderService.updatePaymentStatus(orderId, PaymentStatus.COMPLETED);
            } else if ("FAILED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
                orderService.updatePaymentStatus(orderId, PaymentStatus.FAILED);
            }

            // Trả về phản hồi thành công
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
