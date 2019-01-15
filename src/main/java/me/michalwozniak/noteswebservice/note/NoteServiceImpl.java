package me.michalwozniak.noteswebservice.note;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
}
