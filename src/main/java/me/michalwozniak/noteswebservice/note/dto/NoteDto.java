package me.michalwozniak.noteswebservice.note.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.michalwozniak.noteswebservice.note.model.Note;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Relation(value = "note", collectionRelation = "notes")
public class NoteDto extends ResourceSupport {

    @JsonIgnore
    private Integer noteId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime modified;

    private LocalDateTime created;

    public NoteDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @JsonProperty("_links")
    public void setLinks(final Map<String, Link> links) {
        links.forEach((rel, link) -> add(new Link(link.getHref(), rel)));
    }

    public Note update(Note note) {
        note.setTitle(title);
        note.setContent(content);
        return note;
    }

    public static NoteDto of(Note note) {
        return NoteDto.builder()
                .noteId(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .modified(note.getModified())
                .created(note.getCreated())
                .build();
    }
}
