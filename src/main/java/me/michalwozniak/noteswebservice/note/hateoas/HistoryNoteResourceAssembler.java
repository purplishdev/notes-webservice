package me.michalwozniak.noteswebservice.note.hateoas;

import me.michalwozniak.noteswebservice.DtoResourceAssembler;
import me.michalwozniak.noteswebservice.note.NoteController;
import me.michalwozniak.noteswebservice.note.dto.HistoryNoteDto;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class HistoryNoteResourceAssembler implements DtoResourceAssembler<HistoryNote, HistoryNoteDto> {

    @Override
    public void addLinks(HistoryNoteDto resource) {
        resource.add(
                linkTo(methodOn(NoteController.class).getNoteHistoryById(resource.getNoteId())).withSelfRel(),
                linkTo(methodOn(NoteController.class).getNoteById(resource.getNoteId())).withRel("note")
        );
    }

    @Override
    public void addLinks(Resources<HistoryNoteDto> resources) {
    }

    @Override
    public HistoryNoteDto convertToDto(HistoryNote historyNote) {
        return HistoryNoteDto.of(historyNote);
    }
}
