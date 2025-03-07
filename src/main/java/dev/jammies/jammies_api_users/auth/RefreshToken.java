package dev.jammies.jammies_api_users.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jammies.jammies_api_users.users.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="refresh_token")
public class RefreshToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private String refreshToken;
    private  Boolean revoked ;
    private Date revoked_at ;
    @CreatedDate
    @Column(name="crated_at")
    private Date created_at;

    @JsonIgnore
    @ManyToOne
    private User user;




}
