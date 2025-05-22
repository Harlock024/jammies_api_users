package dev.jammies.jammies_api_users.users;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String avatar_url;
    private String bio;


    public UserResponseDto() {}

    public UserResponseDto(UUID id, String username, String email, String avatar_url,String bio) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar_url = avatar_url;
        this.bio = bio;
    }




}

