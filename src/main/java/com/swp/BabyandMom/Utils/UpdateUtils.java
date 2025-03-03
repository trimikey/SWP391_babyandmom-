package com.swp.BabyandMom.Utils;

import com.swp.BabyandMom.DTO.UpdateProfileRequestDTO;
import com.swp.BabyandMom.Entity.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UpdateUtils {

    public static User updateAccount(UpdateProfileRequestDTO updateRequestDTO, User user) {
        if (StringUtils.hasText(updateRequestDTO.getFullName())) {
            user.setFullName(updateRequestDTO.getFullName());
        }
        if (StringUtils.hasText(updateRequestDTO.getUserName())) {
            user.setUserName(updateRequestDTO.getUserName());
        }
        if (StringUtils.hasText(updateRequestDTO.getEmail())) {
            user.setEmail(updateRequestDTO.getEmail());
        }
        if (StringUtils.hasText(updateRequestDTO.getPhone())) {
            user.setPhoneNumber(updateRequestDTO.getPhone());
        }
        return user;
    }
}

