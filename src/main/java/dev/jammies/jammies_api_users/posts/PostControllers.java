package dev.jammies.jammies_api_users.posts;


import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4321")
@RestController
@RequestMapping("api/post")

public class PostControllers {
    private final PostRepository postRepository;
    private final TrackRepository trackRepository;
    private final PostServices postServices;

    public PostControllers(PostRepository postRepository, TrackRepository trackRepository, PostServices postServices) {
        this.postRepository = postRepository;
        this.trackRepository = trackRepository;
        this.postServices = postServices;
    }

    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return postServices.listPosts() != null ? new ResponseEntity<>(postServices.listPosts(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto post, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (post.getType().equals("track")) {
            Track track = trackRepository.findById(post.getTrack_id()).orElse(null);
            if (track != null) {


            }
        }
        return new ResponseEntity<>(postServices.createPost(post, user), HttpStatus.CREATED);
    }
    @NotNull
    private static PostResponseDto getPostResponseDto(PostDto post, Post savedPost) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(savedPost.getUser().getId());
        userDto.setUsername(savedPost.getUser().getUsername());
        userDto.setEmail(savedPost.getUser().getEmail());

        PostResponseDto dto;
        if (savedPost.getTrack() != null) {
            dto = new PostResponseDto(savedPost.getId(), post.getType(), post.getContent(), userDto, savedPost.getTrack().getId(), savedPost.getCreatedAt());
        } else {
            dto = new PostResponseDto(savedPost.getId(), post.getType(), post.getContent(), userDto, savedPost.getCreatedAt());
        }
        return dto;
    }
    @PutMapping
    public ResponseEntity<Post> updatePost(Post post, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user == null || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(Post post, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user == null || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        postRepository.delete(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponseDto> getPostById(UUID id) {
        return new ResponseEntity<>(postServices.getPostById(id), HttpStatus.OK);
    }


    @GetMapping("/user/")
    public ResponseEntity<Optional<List<PostResponseDto>>> getPostsbyUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<List<PostResponseDto>> posts = postServices.getPostByUser(user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}