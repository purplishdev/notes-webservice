package me.michalwozniak.noteswebservice.note;

import org.springframework.data.repository.CrudRepository;

interface NoteRepository extends CrudRepository<Note, Integer> {
}
