package me.michalwozniak.noteswebservice.note;

import lombok.AllArgsConstructor;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final HistoryNoteRepository historyNoteRepository;

    @Override
    public Note createNote(@Valid NoteDto dto) {
        Note note = new Note(dto.getTitle(), dto.getContent());
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Note> getNoteById(Integer id) {
        return noteRepository.findById(id);
    }

    @Override
    public Optional<Note> updateNoteById(Integer id, @Valid NoteDto dto) {
        return getNoteById(id).map(note -> {
            archiveNoteSnapshot(note);
            return dto.update(note);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public boolean deleteNoteById(Integer id) {
        return getNoteById(id)
                .map(note -> {
                    archiveNoteSnapshot(note);
                    note.delete();
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HistoryNote> getNoteHistoryById(Integer id) {
        return historyNoteRepository.findAllByNoteId(id);
    }

    protected void archiveNoteSnapshot(Note note) {
        HistoryNote historyNote = new HistoryNote(note);
        historyNoteRepository.save(historyNote);
    }
}
