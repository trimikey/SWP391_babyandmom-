package com.swp.BabyandMom.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.swp.BabyandMom.Entity.Enum.MembershipType;

@Getter
@Setter
@NoArgsConstructor


public class OrderRequestDTO {
    private MembershipType type;
    private String buyerEmail;

    public MembershipType getType() {
        return type;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}

