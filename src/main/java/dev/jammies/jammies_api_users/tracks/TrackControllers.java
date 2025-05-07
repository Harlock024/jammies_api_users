package dev.jammies.jammies_api_users.tracks;


import dev.jammies.jammies_api_users.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/track")
public class TrackControllers {

    private final TrackServices trackServices;


    public TrackControllers(TrackServices trackServices) {
        this.trackServices = trackServices;
    }

//    @CrossOrigin(origins =  "http://localhost:4321")
//    @GetMapping
//    public ResponseEntity<List<Track>> getTracks() throws IOException{
//        List<Track> tracks = trackRepository.findAll();
//        return new ResponseEntity<>(tracks, HttpStatus.OK);
//    }


//    @CrossOrigin(origins =  "http://localhost:4321")
//    @GetMapping("/{id}")
//    public ResponseEntity<Track> getTrack(@PathVariable UUID id){
//        Track track = trackRepository.findById(id).orElse(null);
//        return new ResponseEntity<>(track, HttpStatus.OK);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackResponse> createTrack(
            @ModelAttribute UploadTrackRequest track,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        TrackResponse newTrack = trackServices.uploadTrack(track, user);
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


