package me.michalwozniak.noteswebservice.note.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "notes_history")

@Getter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@ToString(exclude = "note")
public class HistoryNote implements Identifiable<Integer> {

    /**
     * Immutable entity key
     */
    @Column(nullable = false, updatable = false)
    private final UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private String title;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private int version;

    @Column(nullable = false, updatable = false)
    private boolean deleted;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "note_id", updatable = false, nullable = false)
    private final Note note;

    protected HistoryNote() {
        note = null;
    }

    public HistoryNote(@NonNull Note note) {
        this.note = note;
        this.title = note.getTitle();
        this.content = note.getContent();
        this.version = note.getVersion();
        this.deleted = note.isDeleted();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @PrePersist
    protected void prePersist() {
        this.created = LocalDateTime.now(ZoneOffset.UTC);
    }
}
