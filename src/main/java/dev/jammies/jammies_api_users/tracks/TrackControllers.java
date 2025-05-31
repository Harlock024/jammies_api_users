package dev.jammies.jammies_api_users.tracks;


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
import java.util.UUID;

@RestController
@RequestMapping("api/track")
public class TrackControllers {

    private final TrackServices trackServices;
    private final UsersRepository usersRepository;


    public TrackControllers(TrackServices trackServices, UsersRepository usersRepository) {
        this.trackServices = trackServices;
        this.usersRepository = usersRepository;
    }


    @GetMapping
    @Transactional
    public ResponseEntity<List<TrackResponse>> getTracks(Authentication auth){
        User user = (User) auth.getPrincipal();

        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<TrackResponse> tracks = trackServices.getTrackList(managedUser);
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }


    @GetMapping("/{track_id}")
    @Transactional
    public ResponseEntity<TrackResponse> getTrack(@PathVariable UUID track_id, Authentication auth) {
        User user = (User) auth.getPrincipal();

        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new ResponseEntity<>(trackServices.getTrack(track_id, managedUser), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackResponse> createTrack(
            @ModelAttribute UploadTrackRequest track,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();

        User managedUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TrackResponse newTrack = trackServices.uploadTrack(track, managedUser);
        return new ResponseEntity<>(newTrack, HttpStatus.CREATED);
    }
//
//    @PutMapping
//    public ResponseEntity<Track> updateTrack(@RequestBody Track track){
//        trackRepository.save(track);
//        return new ResponseEntity<>(track, HttpStatus.OK);
//    }

//    @CrossOrigin(origins =  "http://localhost:4321")
//    @DeleteMapping
//    public ResponseEntity<Track> deleteTrack(@RequestBody Track track){
//        trackRepository.delete(track);
//        return new ResponseEntity<>(track, HttpStatus.OK);
//    }

//    @CrossOrigin(origins =  "http://localhost:4321")
//    @GetMapping("/{user_id}")
//    public ResponseEntity<List<Track>> getTracksByUserId(@PathVariable UUID user_id){
//        List<Track> tracks = trackRepository.findByUserId(user_id);
//        return new ResponseEntity<>(tracks, HttpStatus.OK);
//    }
//    @PostMapping("/search")
//    public ResponseEntity<Track> getTrackByTitle(@RequestBody String track){
//        Track track1 = trackRepository.findByTitle(track);
//        return new ResponseEntity<>(track1, HttpStatus.OK);
//    }

}


