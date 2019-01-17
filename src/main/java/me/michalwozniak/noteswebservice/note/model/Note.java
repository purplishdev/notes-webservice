package me.michalwozniak.noteswebservice.note.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notes")

@Getter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@ToString(exclude = "historyNotes")
public class Note implements Identifiable<Integer> {

    /**
     * Immutable entity key
     */
    @Column(nullable = false, updatable = false)
    private final UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private boolean deleted;

    private LocalDateTime modified;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY)
    private List<HistoryNote> historyNotes = new ArrayList<>();

    protected Note() { }

    public Note(@NonNull String title, @NonNull String content) {
        this.title = title;
        this.content = content;
        this.version = 0;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void delete() {
        deleted = true;
    }

    @PrePersist
    protected void prePersist() {
        this.created = LocalDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void preUpdate() {
        this.version += 1;
        this.modified = LocalDateTime.now(ZoneOffset.UTC);
    }
}
