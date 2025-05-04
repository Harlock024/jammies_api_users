package dev.jammies.jammies_api_users.posts;


import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.*;

@CrossOrigin(origins =  "http://localhost:4321")
@RestController
@RequestMapping("api/post")

public class PostControllers {
    private final PostRepository postRepository;
    private final TrackRepository trackRepository;

    public PostControllers(PostRepository postRepository, TrackRepository trackRepository) {
        this.postRepository = postRepository;
        this.trackRepository = trackRepository;
    }

    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getPosts(
               @RequestParam(required = false)String cursor,
                @RequestParam(defaultValue = "10") int limit
    ) {
        Instant cursoInstant = null;
        if(cursor !=null && !cursor.isBlank()){
            try{
                cursoInstant = Instant.parse(cursor);
            }catch (DateTimeException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        Pageable pageable = PageRequest.of(0, limit);
        List<Post> posts = postRepository.findAll(pageable).getContent();
        List<PostResponseDto>  postResponseDtos = new ArrayList<>();

        for (Post post : posts) {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(post.getUser().getId());
            userDto.setUsername(post.getUser().getUsername());
            userDto.setEmail(post.getUser().getEmail());
            if(post.getTrack()!= null) {
                Track track = trackRepository.findById(post.getTrack().getId()).orElse(null);
              PostResponseDto dto = new PostResponseDto(post.getId(),post.getType(),post.getContent(),userDto,post.getCreatedAt());
              postResponseDtos.add(dto);
            }else{
                PostResponseDto dto = new PostResponseDto(post.getId(),post.getType(),post.getContent(),userDto,post.getCreatedAt());
               postResponseDtos.add(dto);
            }

        }
        return  ResponseEntity.ok(postResponseDtos);
    }



    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto post, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
          Post newPost = new Post();
        if(Objects.equals(post.getType(), "track")){
            Track track = trackRepository.findById(post.getTrack_id()).orElse(null);
            newPost.setTrack(track);
        }
        newPost.setType(post.getType());
        newPost.setContent(post.getContent());
        newPost.setUser(user);
        Post savedPost = postRepository.save(newPost);

        PostResponseDto dto = getPostResponseDto(post, savedPost);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @NotNull
    private static PostResponseDto getPostResponseDto(PostDto post, Post savedPost) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(savedPost.getUser().getId());
        userDto.setUsername(savedPost.getUser().getUsername());
        userDto.setEmail(savedPost.getUser().getEmail());

        PostResponseDto dto;
        if(savedPost.getTrack() != null){
            dto = new PostResponseDto(savedPost.getId(),post.getType(), post.getContent(), userDto, savedPost.getTrack().getId(), savedPost.getCreatedAt());
        }else{
            dto = new PostResponseDto(savedPost.getId(),post.getType(), post.getContent(), userDto, savedPost.getCreatedAt());
        }
        return dto;
    }

    @PutMapping
    public ResponseEntity<Post> updatePost(Post post) {
        return  new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(Post post) {
        postRepository.delete(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("{id}")
    public ResponseEntity<PostResponseDto> getPostById(UUID id) {

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserResponseDto user = new UserResponseDto();
        user.setUsername(post.getUser().getUsername());
        user.setEmail(post.getUser().getEmail());
        user.setId(post.getUser().getId());

        PostResponseDto dto = new PostResponseDto(post.getId(),post.getType(),post.getContent(),user,post.getTrack().getId(),post.getCreatedAt());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<List<Post>>> getPostsbyUserId(UUID id) {
        Optional<List<Post>> posts = postRepository.findByUserId(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}