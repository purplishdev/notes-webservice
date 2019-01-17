package me.michalwozniak.noteswebservice.note.hateoas;

import me.michalwozniak.noteswebservice.DtoResourceAssembler;
import me.michalwozniak.noteswebservice.note.NoteController;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class NoteResourceAssembler implements DtoResourceAssembler<Note, NoteDto> {

    @Override
    public void addLinks(NoteDto resource) {
        resource.add(
                linkTo(methodOn(NoteController.class).getNoteById(resource.getNoteId())).withSelfRel(),
                linkTo(methodOn(NoteController.class).getNotes()).withRel("notes"),
                linkTo(methodOn(NoteController.class).getNoteHistoryById(resource.getNoteId())).withRel("noteHistory")
        );
    }

    @Override
    public void addLinks(Resources<NoteDto> resources) {
    }

    @Override
    public NoteDto convertToDto(Note note) {
        return NoteDto.of(note);
    }
}
