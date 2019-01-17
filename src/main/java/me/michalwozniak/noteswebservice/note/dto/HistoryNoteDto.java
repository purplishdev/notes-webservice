package me.michalwozniak.noteswebservice.note.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Relation(value = "note", collectionRelation = "notes")
public class HistoryNoteDto extends ResourceSupport {

    @JsonIgnore
    private Integer noteId;

    private String title;

    private String content;

    private Integer version;

    @JsonProperty("_links")
    public void setLinks(final Map<String, Link> links) {
        links.forEach((rel, link) -> add(new Link(link.getHref(), rel)));
    }

    public static HistoryNoteDto of(HistoryNote note) {
        return HistoryNoteDto.builder()
                .noteId(note.getNote().getId())
                .title(note.getTitle())
                .content(note.getContent())
                .version(note.getVersion())
                .build();
    }
}
