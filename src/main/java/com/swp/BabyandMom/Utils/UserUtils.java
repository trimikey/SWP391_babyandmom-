package com.swp.BabyandMom.Utils;

import com.swp.BabyandMom.Entity.Enum.PaymentStatus;
import com.swp.BabyandMom.Entity.Order;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Entity.Subscription;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Repository.OrderRepository;
import com.swp.BabyandMom.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class UserUtils {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserUtils(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public User getCurrentAccount() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof User) {
            return (User) object;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public MembershipType getUserMembershipType() {
        User currentUser = getCurrentAccount();
        if (currentUser != null) {
            User userWithSubscriptions = userRepository.findById(currentUser.getId())
                    .orElse(null);

            if (userWithSubscriptions != null && userWithSubscriptions.getSubscriptions() != null) {
                return userWithSubscriptions.getSubscriptions().stream()
                        .filter(Subscription::getIsActive)
                        .findFirst()
                        .map(subscription -> subscription.getMembershipPackage().getType())
                        .orElse(null);
            }
        }
        return null;
    }
    @Transactional(readOnly = true)
    public boolean hasCompletedPayment() {
        User currentUser = getCurrentAccount();
        if (currentUser != null) {
            User userWithOrders = userRepository.findById(currentUser.getId()).orElse(null);

            if (userWithOrders != null && userWithOrders.getSubscriptions() != null) {
                return userWithOrders.getSubscriptions().stream()
                        .map(Subscription::getId)
                        .map(subscriptionId -> findOrderBySubscriptionId(subscriptionId))
                        .filter(Objects::nonNull)
                        .anyMatch(order -> order.getPaymentStatus() == PaymentStatus.COMPLETED);
            }
        }
        return false;
    }
    private Order findOrderBySubscriptionId(Long subscriptionId) {
        return orderRepository.findBySubscriptionId(subscriptionId).orElse(null);
    }
}
