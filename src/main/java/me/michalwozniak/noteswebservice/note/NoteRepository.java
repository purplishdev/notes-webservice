package me.michalwozniak.noteswebservice.note;

import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface NoteRepository extends CrudRepository<Note, Integer> {

    @Query("SELECT note FROM Note note WHERE note.id = :id AND note.deleted = false")
    Optional<Note> findById(@Param("id") Integer id);

    @Query("SELECT note FROM Note note WHERE note.deleted = false")
    List<Note> findAll();
}
