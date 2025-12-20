package com.rafael.nailspro.webapp.model.entity.user;

import com.rafael.nailspro.webapp.model.enums.UserRole;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private UserRole userRole;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));

        if (this.userRole == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSIONAL"));
        }

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
