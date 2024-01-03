package ru.urfu.pizzaSite.RestApiPizzaApplication.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;

import java.util.Collection;
import java.util.Collections;

public record ClientDetail(Client client) implements UserDetails {

    @Override
    public Client client() {
        return client;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.client.getPassword();
    }

    @Override
    public String getUsername() {
        return this.client.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
