package me.michalwozniak.noteswebservice.note;

import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
public interface NoteService {

    Note createNote(@Valid NoteDto dto);

    Optional<Note> getNoteById(Integer id);

    Optional<Note> updateNoteById(Integer id, @Valid NoteDto dto);

    List<Note> getAllNotes();

    boolean deleteNoteById(Integer id);

    List<HistoryNote> getNoteHistoryById(Integer id);
}
