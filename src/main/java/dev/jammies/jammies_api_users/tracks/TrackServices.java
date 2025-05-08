package dev.jammies.jammies_api_users.tracks;

import dev.jammies.jammies_api_users.users.User;
import dev.jammies.jammies_api_users.users.UserServices;
import dev.jammies.jammies_api_users.utils.CloudinaryService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TrackServices {

    private final TrackRepository trackRepository;
    private final CloudinaryService cloudinaryServices;

    public TrackServices(TrackRepository trackRepository, CloudinaryService cloudinaryServices) {
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

            newTrack.setDuration(trackAudioResult.get("duration").toString());

            var trackCoverResult = cloudinaryServices.upload(track.getCover(), "jammies_track/cover", "image");
            if (trackCoverResult == null || !trackCoverResult.containsKey("secure_url")) {
                throw new IOException("Failed to upload track cover to Cloudinary");
            }
            newTrack.setCover_image(trackCoverResult.get("secure_url").toString());

            Track savedTrack = trackRepository.save(newTrack);




            return new TrackResponse(
                    savedTrack.getId(),
                    savedTrack.getTitle(),
                    savedTrack.getAudio_url(),
                    formatDurationToMinutes(savedTrack.getDuration()),
                    savedTrack.getUser().getUsername(),
                    savedTrack.getTitle(),
                    savedTrack.getCover_image()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error with track data: " + e.getMessage(), e);
        } catch (IOException e) {

            throw new IOException("Error uploading track data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public String formatDurationToMinutes(String durationInString) {
        try {

            double durationInSeconds = Double.parseDouble(durationInString);


            int minutes = (int) durationInSeconds / 60;
            int seconds = (int) durationInSeconds % 60;


            return String.format("%d:%02d", minutes, seconds);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "00:00";
        }
    }
}
