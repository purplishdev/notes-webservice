package me.michalwozniak.noteswebservice.note;

import lombok.AllArgsConstructor;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class NoteController {

    private final NoteService noteService;

    @GetMapping("api/notes")
    public ResponseEntity<List<Note>> getNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("api/notes/{noteId}")
    public ResponseEntity<Note> getNoteById(@PathVariable Integer noteId) {
        return noteService.getNoteById(noteId)
                .map(ResponseEntity::ok)
                .orElse(notFound());
    }

    @GetMapping("api/notes/{noteId}/history")
    public ResponseEntity<List<HistoryNote>> getNoteHistoryById(@PathVariable Integer noteId) {
        return ResponseEntity.ok(noteService.getNoteHistoryById(noteId));
    }

    @PostMapping("api/notes")
    public ResponseEntity<Note> createNote(@Valid @RequestBody NoteDto dto) {
        return ResponseEntity.ok(noteService.createNote(dto));
    }

    @PutMapping("api/notes/{noteId}")
    public ResponseEntity<Note> updateNoteById(@PathVariable Integer noteId,
                                               @Valid @RequestBody NoteDto dto) {
        return noteService.updateNoteById(noteId, dto)
                .map(ResponseEntity::ok)
                .orElse(notFound());
    }

    @DeleteMapping("api/notes/{noteId}")
    public ResponseEntity deleteNoteById(@PathVariable Integer noteId) {
        boolean deleted = noteService.deleteNoteById(noteId);
        return deleted ? ok() : notFound();
    }

    private <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok().build();
    }

    private <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }
}
