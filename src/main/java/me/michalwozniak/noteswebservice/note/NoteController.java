package me.michalwozniak.noteswebservice.note;

import lombok.AllArgsConstructor;
import me.michalwozniak.noteswebservice.Api;
import me.michalwozniak.noteswebservice.note.dto.HistoryNoteDto;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import me.michalwozniak.noteswebservice.note.hateoas.HistoryNoteResourceAssembler;
import me.michalwozniak.noteswebservice.note.hateoas.NoteResourceAssembler;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class NoteController {

    private final NoteService noteService;

    private final NoteResourceAssembler noteAssembler;

    private final HistoryNoteResourceAssembler historyNoteAssembler;

    @GetMapping(Api.NOTES)
    public ResponseEntity<Resources<NoteDto>> getNotes() {
        List<Note> notes = noteService.getAllNotes();

        Resources<NoteDto> resources = new Resources<>(noteAssembler.toResources(notes));
        resources.add(linkTo(methodOn(NoteController.class).getNotes()).withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @GetMapping(Api.NOTE)
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Integer noteId) {
        return noteService.getNoteById(noteId)
                .map(noteAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(notFound());
    }

    @GetMapping(Api.NOTE_HISTORY)
    public ResponseEntity<Resources<HistoryNoteDto>> getNoteHistoryById(@PathVariable Integer noteId) {
        List<HistoryNote> historyNotes = noteService.getNoteHistoryById(noteId);

        Resources<HistoryNoteDto> resources = new Resources<>(historyNoteAssembler.toResources(historyNotes));
        resources.add(linkTo(methodOn(NoteController.class).getNoteHistoryById(noteId)).withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @PostMapping(Api.NOTES)
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto dto) {
        Note note = noteService.createNote(dto);
        return ResponseEntity.ok(noteAssembler.toResource(note));
    }

    @PutMapping(Api.NOTE)
    public ResponseEntity<NoteDto> updateNoteById(@PathVariable Integer noteId, @Valid @RequestBody NoteDto dto) {
        return noteService.updateNoteById(noteId, dto)
                .map(noteAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(notFound());
    }

    @DeleteMapping(Api.NOTE)
    public ResponseEntity deleteNoteById(@PathVariable Integer noteId) {
        boolean deleted = noteService.deleteNoteById(noteId);
        return deleted ? ok() : notFound();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().build();
    }

    private <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok().build();
    }

    private <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }
}
