package dev.jammies.jammies_api_users.posts;


import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/post")

public class PostControllers {
    private final PostServices postServices;
    private final UsersRepository usersRepository;

    public PostControllers(PostServices postServices, UsersRepository usersRepository) {
        this.postServices = postServices;
        this.usersRepository = usersRepository;
    }

    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return postServices.listPosts() != null ? new ResponseEntity<>(postServices.listPosts(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<PostResponseDto> createPost(@ModelAttribute PostRequestDto post, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new ResponseEntity<>(postServices.createPost(post, managedUser), HttpStatus.CREATED);
    }

//    @PutMapping
//    public ResponseEntity<Post> updatePost(Post post, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        if (user == null || !post.getUser().getId().equals(user.getId())) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        return new ResponseEntity<>(postServices.updatePost(), HttpStatus.OK);
//    }

//    @DeleteMapping
//    public ResponseEntity<Boolean> deletePost(Post post, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
//        if (user == null || !post.getUser().getId().equals(user.getId())) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        postRepository.delete(post);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable UUID id) {
        return new ResponseEntity<>(postServices.getPostById(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Optional<List<PostResponseDto>>> getPostsbyUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<List<PostResponseDto>> posts = postServices.getPostByUser(user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}