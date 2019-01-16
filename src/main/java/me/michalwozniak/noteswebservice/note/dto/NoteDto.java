package me.michalwozniak.noteswebservice.note.dto;

import lombok.Value;
import me.michalwozniak.noteswebservice.note.model.Note;

import javax.validation.constraints.NotBlank;

@Value
public class NoteDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public Note update(Note note) {
        note.setTitle(title);
        note.setContent(content);
        return note;
    }
}
