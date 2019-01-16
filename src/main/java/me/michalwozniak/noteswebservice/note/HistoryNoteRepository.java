package me.michalwozniak.noteswebservice.note;

import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface HistoryNoteRepository extends CrudRepository<HistoryNote, Integer> {

    List<HistoryNote> findAllByNoteId(Integer id);
}
