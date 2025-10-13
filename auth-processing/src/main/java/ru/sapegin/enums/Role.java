package ru.sapegin.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    MASTER("MASTER"),
    GRAND_EMPLOYEE("GRAND_EMPLOYEE"),
    CURRENT_CLIENT("CURRENT_CLIENT"),
    BLOCKED_CLIENT("BLOCKED_CLIENT");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
