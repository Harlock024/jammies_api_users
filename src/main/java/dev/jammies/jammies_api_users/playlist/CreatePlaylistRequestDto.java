package dev.jammies.jammies_api_users.playlist;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CreatePlaylistRequestDto {
    @NotNull
    private String name;
}
