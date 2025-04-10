package dev.jammies.jammies_api_users.posts;


import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/post")
public class PostControllers {
    private final PostRepository postRepository;
    private final TrackRepository trackRepository;

    public PostControllers(PostRepository postRepository, TrackRepository trackRepository) {
        this.postRepository = postRepository;
        this.trackRepository = trackRepository;
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping()
    public ResponseEntity<List<Post>> getPosts(
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
        return  ResponseEntity.ok(posts);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto post, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
          Post newPost = new Post();

        if(post.getTrack_id() != null){
            Track track = trackRepository.findById(post.getTrack_id()).orElse(null);
                newPost.setTrack(track);
        }
        newPost.setContent(post.getContent());
        newPost.setUser(user);
        Post savedPost = postRepository.save(newPost);

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(savedPost.getId());
        userDto.setUsername(savedPost.getUser().getUsername());
        userDto.setEmail(savedPost.getUser().getEmail());

        PostResponseDto dto;
        if(savedPost.getTrack() != null){
            dto = new PostResponseDto(savedPost.getId(), post.getContent(), userDto, savedPost.getTrack().getId(), savedPost.getCreatedAt());
        }else{
            dto = new PostResponseDto(savedPost.getId(), post.getContent(), userDto, savedPost.getCreatedAt());
        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @CrossOrigin(origins =  "http://localhost:4321")
    @PutMapping
    public ResponseEntity<Post> updatePost(Post post) {
        return  new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(Post post) {
        postRepository.delete(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @CrossOrigin(origins = "http://localhost:4321")
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

        PostResponseDto dto = new PostResponseDto(post.getId(),post.getContent(),user,post.getTrack().getId(),post.getCreatedAt());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<List<Post>>> getPostsbyUserId(UUID id) {
        Optional<List<Post>> posts = postRepository.findByUserId(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
