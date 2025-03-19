package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.TransactionResponseDTO;
import com.swp.BabyandMom.Service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get all transactions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get transactions by user ID")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get transaction by order ID")
    public ResponseEntity<TransactionResponseDTO> getTransactionByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(transactionService.getTransactionByOrderId(orderId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
} 