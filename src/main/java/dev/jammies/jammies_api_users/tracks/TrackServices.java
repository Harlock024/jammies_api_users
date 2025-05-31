package dev.jammies.jammies_api_users.tracks;

import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserServices;
import dev.jammies.jammies_api_users.utils.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TrackServices {

    private final TrackRepository trackRepository;
    private final CloudinaryService cloudinaryServices;

    public TrackServices(TrackRepository trackRepository, CloudinaryService cloudinaryServices, UserServices userServices) {
        this.trackRepository = trackRepository;
        this.cloudinaryServices = cloudinaryServices;
    }


    public TrackResponse uploadTrack(UploadTrackRequest track, User user) throws IOException {
        try {
            if (track == null || track.getAudio() == null || track.getCover() == null) {
                throw new IllegalArgumentException("Track audio or cover cannot be null");
            }

            Track newTrack = new Track();
            newTrack.setTitle(track.getTitle());
            newTrack.setUser(user);

            var trackAudioResult = cloudinaryServices.upload(track.getAudio(), "jammies_track", "video");
            if (trackAudioResult == null || !trackAudioResult.containsKey("secure_url")) {
                throw new IOException("Failed to upload track audio to Cloudinary");
            }

            newTrack.setAudio_url(trackAudioResult.get("secure_url").toString());

            newTrack.setDuration((Double) trackAudioResult.get("duration"));

            var trackCoverResult = cloudinaryServices.upload(track.getCover(), "jammies_track/cover", "image");
            if (trackCoverResult == null || !trackCoverResult.containsKey("secure_url")) {
                throw new IOException("Failed to upload track cover to Cloudinary");
            }
            newTrack.setCover_image(trackCoverResult.get("secure_url").toString());

            Track savedTrack = trackRepository.save(newTrack);

            return TrackResponseMapper.toResponse(savedTrack, user);


        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error with track data: " + e.getMessage(), e);
        } catch (IOException e) {

            throw new IOException("Error uploading track data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<TrackResponse> getTrackList(User user) {
        List<Track> tracks = trackRepository.findAll();
        return TrackResponseMapper.toResponseList(tracks, user);
    }

    @Transactional
    public TrackResponse getTrack(UUID id, User user) {
        Track track = trackRepository.findById(id).orElseThrow(() -> new RuntimeException("Track not found"));
        return TrackResponseMapper.toResponse(track, user);
    }
}
