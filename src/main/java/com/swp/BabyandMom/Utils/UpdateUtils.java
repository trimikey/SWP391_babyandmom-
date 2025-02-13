package com.swp.BabyandMom.Utils;


import com.swp.BabyandMom.DTO.UpdateProfileRequestDTO;
import com.swp.BabyandMom.Entity.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UpdateUtils {

    public static User updateAccount(UpdateProfileRequestDTO updateRequestDTO, User user) {
        if (StringUtils.hasText(updateRequestDTO.getName())) {
            user.setName(updateRequestDTO.getName());
        }
        if (StringUtils.hasText(updateRequestDTO.getEmail())) {
            user.setEmail(updateRequestDTO.getEmail());
        }if (StringUtils.hasText(updateRequestDTO.getPhone())) {
            user.setPhoneNumber(updateRequestDTO.getPhone());
        }if (StringUtils.hasText(updateRequestDTO.getPassword())) {
            user.setPassword(updateRequestDTO.getPassword());
        }
        return user;
    }

}
