package com.swp.BabyandMom.Utils;

import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Entity.Subscription;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserUtils {
    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
