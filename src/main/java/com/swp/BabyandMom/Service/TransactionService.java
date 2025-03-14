package com.swp.BabyandMom.Service;

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

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUser(user);
    }

    public Transaction getTransactionByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return transactionRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
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
        Transaction transaction = getTransactionById(id);
        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);
    }
} 