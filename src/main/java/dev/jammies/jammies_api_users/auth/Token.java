package dev.jammies.jammies_api_users.auth;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jammies.jammies_api_users.users.User;
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name ="token")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String token;
    private Date expirate_at;

    @CreationTimestamp
    @Column(name="created_at")
    private Date createdAt;

    @JsonIgnore
    @ManyToOne
    private User user;




}
