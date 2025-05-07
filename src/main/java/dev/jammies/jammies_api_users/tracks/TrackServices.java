package dev.jammies.jammies_api_users.tracks;

import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.utils.CloudinaryService;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TrackServices {

    private final TrackRepository trackRepository;
    private final CloudinaryService cloudinaryServices;

    public TrackServices(TrackRepository trackRepository, CloudinaryService cloudinaryServices) {
        this.trackRepository = trackRepository;
        this.cloudinaryServices = cloudinaryServices;

    }

    public TrackResponse uploadTrack(UploadTrackRequest track, User user) throws IOException {

        Track newTrack = new Track();

        newTrack.setTitle(track.getTitle());
        newTrack.setUser(user);

        Map trackAudioResult = cloudinaryServices.upload(track.getAudio(), "jammies_track", "video");
        newTrack.setAudio_url(trackAudioResult.get("secure_url").toString());

        Map trackCoverResult = cloudinaryServices.upload(track.getCover(), "jammies_track/cover", "image");

        newTrack.setCover_image(trackCoverResult.get("secure_url").toString());
        Track savedTrack = trackRepository.save(newTrack);

        return new TrackResponse(
                savedTrack.getId(),
                savedTrack.getTitle(),
                savedTrack.getAudio_url(),
                "0:0",
                savedTrack.getUser().getName(),
                savedTrack.getTitle(),
                savedTrack.getCover_image()
        );

    }

}
