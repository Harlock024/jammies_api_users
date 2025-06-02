package dev.jammies.jammies_api_users.posts;

import dev.jammies.jammies_api_users.tracks.Track;
import dev.jammies.jammies_api_users.tracks.TrackRepository;
import dev.jammies.jammies_api_users.tracks.TrackResponse;
import dev.jammies.jammies_api_users.tracks.TrackResponseMapper;
import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserResponseDto;
import dev.jammies.jammies_api_users.utils.CloudinaryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServices {


    private final PostRepository postRepository;
    private final TrackRepository trackRepository;
    private final CloudinaryService cloudinaryService;


    public PostServices(PostRepository postRepository, CloudinaryService cloudinaryService, TrackRepository trackRepository) {
        this.postRepository = postRepository;
        this.cloudinaryService = cloudinaryService;

        this.trackRepository = trackRepository;
    }

    public List<PostResponseDto> listPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtos.add(convertToDto(post));
        }
        return postResponseDtos;
    }


    @Transactional
    public PostResponseDto createPost(PostRequestDto postDto, User user) throws IOException {
        try {
            Post post = new Post();
            post.setUser(user);

            if (postDto.getContent() != null) {
                post.setContent(postDto.getContent());
            }
            if (postDto.getImage() != null) {
                var postCoverResult = cloudinaryService.upload(postDto.getImage(), "jammies_track/post/images", "image");
                if (postCoverResult == null || !postCoverResult.containsKey("secure_url")) {
                    throw new IOException("Could not upload image to Cloudinary");
                }
                post.setImage(postCoverResult.get("secure_url").toString());
            }
            if (postDto.getTrack_id() != null) {
                Optional<Track> track = trackRepository.findById(postDto.getTrack_id());
                post.setTrack(track.orElse(null));
            }
            return convertToDto(postRepository.save(post));
        } catch (Exception e) {
            throw new IOException("Error creating post: " + e.getMessage(), e);
        }
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

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setPosted_by(userDto);
        if (post.getContent() != null) postResponseDto.setContent(post.getContent());

        if (post.getTrack() != null) {
            TrackResponse track = TrackResponseMapper.toResponse(post.getTrack(), post.getUser());
            postResponseDto.setTrack(track);
        }
        if (post.getImage() != null) {
            postResponseDto.setImage(post.getImage());
        }
        return postResponseDto;
    }
}
