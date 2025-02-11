package com.swp.BabyandMom.Entity.Enum;

public class UserRole {
    private RoleType accountRoleEnum;

    public String getAuthority() {
        return "ROLE_" + accountRoleEnum.toString();
    }
}
