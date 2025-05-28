package dev.jammies.jammies_api_users.posts;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServices {

    private final PostRepository postRepository;

    public PostServices(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<PostResponseDto> listPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtos.add(convertToDto(post));
        }

        return postResponseDtos;
    }

    public PostResponseDto createPostWithTrack(PostDto postDto, User user, Track track) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setCreatedAt(Instant.now());
        post.setType(postDto.getType());
        post.setContent(postDto.getContent());
        post.setTrack(track);
        post.setUser(user);

        return convertToDto(postRepository.save(post));
    }

    public PostResponseDto createPost(PostDto postDto, User user) {
        Post post = new Post();
        post.setType(postDto.getType());
        post.setContent(postDto.getContent());
        post.setUser(user);
        post.setCreatedAt(Instant.now());

        return convertToDto(postRepository.save(post));
    }

    public PostResponseDto getPostById(UUID id) {
        return postRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    public PostResponseDto updatePost(PostDto postDto, UUID id, User user) {
        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost == null || !existingPost.getUser().getId().equals(user.getId())) {
            return null;
        }

        existingPost.setType(postDto.getType());
        existingPost.setContent(postDto.getContent());

        return convertToDto(postRepository.save(existingPost));
    }

    public Boolean deletePost(UUID id, User user) {
        Post post = postRepository.findById(id).orElse(null);

        if (post != null && post.getUser().getId().equals(user.getId())) {
            postRepository.delete(post);
            return true;
        }

        return false;
    }

    public Optional<List<PostResponseDto>> getPostByUser(User user) {
        Optional<List<Post>> postByUser = postRepository.findByUserId(user.getId());

        for (Post post : postByUser.get()) {
            return Optional.of(List.of(convertToDto(post)));
        }
        return Optional.empty();
    }

    private PostResponseDto convertToDto(Post post) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setEmail(post.getUser().getEmail());

        if (post.getTrack() != null) {
            return new PostResponseDto(post.getId(), post.getType(), post.getContent(), userDto,
                    post.getTrack().getId(), post.getCreatedAt());
        }

        return new PostResponseDto(post.getId(), post.getType(), post.getContent(), userDto, post.getCreatedAt());
    }
}
