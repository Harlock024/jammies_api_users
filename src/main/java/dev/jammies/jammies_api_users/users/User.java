package dev.jammies.jammies_api_users.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jammies.jammies_api_users.auth.Token;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Entity
@Table(name= "User")
public class User implements UserDetails,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonIgnore
        @OneToMany(mappedBy = "token" ,fetch = FetchType.LAZY)
    private Set<Token> access_token = new HashSet<>();

    @JsonIgnore
        @OneToMany(mappedBy = "refresh_token",fetch = FetchType.LAZY)
    private Set<Token> refresh_token = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override

    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public Set<Token> getAccess_token() {
        return access_token;
    }

    public void setAccess_token(Set<Token> access_token) {
        this.access_token = access_token;
    }

    public Set<Token> getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(Set<Token> refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
