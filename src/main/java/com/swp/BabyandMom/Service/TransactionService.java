package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.TransactionResponseDTO;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Entity.Transaction;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Entity.Enum.TransactionStatus;
import com.swp.BabyandMom.Repository.OrderRepository;
import com.swp.BabyandMom.Repository.TransactionRepository;
import com.swp.BabyandMom.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    public List<TransactionResponseDTO> getTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Transaction transaction = transactionRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    public Transaction createTransaction(Order order, String paymentMethod, TransactionStatus status) {
        Transaction transaction = new Transaction();
        transaction.setUser(order.getUser());
        transaction.setOrder(order);
        transaction.setTotalPrice(order.getTotalPrice());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setStatus(status);
        transaction.setIsDeleted(false);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);
    }

    private TransactionResponseDTO convertToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getUser().getUserName(),
                transaction.getOrder().getId(),
                transaction.getTotalPrice(),
                transaction.getCreatedAt(),
                transaction.getPaymentMethod(),
                transaction.getStatus(),
                transaction.getIsDeleted()
        );
    }
} 