package dev.jammies.jammies_api_users.tracks;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/track")
public class TrackControllers {

    private final TrackRepository trackRepository;


    public TrackControllers(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping
    public ResponseEntity<List<Track>> getTracks(){
        List<Track> tracks = trackRepository.findAll();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrack(@PathVariable UUID id){
        Track track = trackRepository.findById(id).orElse(null);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @PostMapping()
    public ResponseEntity<Track> createTrack(@RequestBody Track track){
        trackRepository.save(track);
        return new ResponseEntity<>(track, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Track> updateTrack(@RequestBody Track track){
        trackRepository.save(track);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @DeleteMapping
    public ResponseEntity<Track> deleteTrack(@RequestBody Track track){
        trackRepository.delete(track);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }

    @CrossOrigin(origins =  "http://localhost:4321")
    @GetMapping("/{user_id}")
    public ResponseEntity<List<Track>> getTracksByUserId(@PathVariable UUID user_id){
        List<Track> tracks = trackRepository.findByUserId(user_id);
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }
    @PostMapping("/search")
    public ResponseEntity<Track> getTrackByTitle(@RequestBody String track){
        Track track1 = trackRepository.findByTitle(track);
        return new ResponseEntity<>(track1, HttpStatus.OK);
    }

}


