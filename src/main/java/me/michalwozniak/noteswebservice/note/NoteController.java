package me.michalwozniak.noteswebservice.note;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class NoteController {

    private final NoteService noteService;

    @GetMapping("api/notes")
    public ResponseEntity<Object> getNotes() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("api/notes/{id}")
    public ResponseEntity<Object> getNoteById(@PathVariable Integer id) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("api/notes/{id}/history")
    public ResponseEntity<Object> getNoteHistoryById(@PathVariable Integer id) {
        return ResponseEntity.notFound().build();
    }

    @PostMapping("api/notes")
    public ResponseEntity<Object> createNote() {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("api/notes/{id}")
    public ResponseEntity<Object> updateNoteById(@PathVariable Integer id) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("api/notes/{id}")
    public ResponseEntity<Object> deleteNoteById(@PathVariable Integer id) {
        return ResponseEntity.notFound().build();
    }
}
