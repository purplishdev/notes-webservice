package me.michalwozniak.noteswebservice.integration;

import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import me.michalwozniak.noteswebservice.note.model.HistoryNote;
import me.michalwozniak.noteswebservice.note.model.Note;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NoteControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void create_note_should_create_and_return_new_note() {
        NoteDto noteDto = new NoteDto("note title", "note content");

        Note note = createNote(noteDto);

        assertThat(note).isNotNull();
        assertThat(note).isEqualToComparingOnlyGivenFields(
                noteDto, "title", "content");
    }

    @Test
    public void get_note_should_return_existing_note() {
        NoteDto noteDto = new NoteDto("note title", "note content");

        Note note = createNote(noteDto);
        Note returnedNote = getNote(note.getId());

        assertThat(note).isNotNull();
        assertThat(returnedNote).isNotNull();
        assertThat(returnedNote).isEqualToComparingOnlyGivenFields(
                note, "id", "title", "content");
    }

    @Test
    public void update_note_should_update_note_and_save_old_note_version() {
        NoteDto noteDto = new NoteDto("note title", "note content");
        NoteDto updatedNoteDto = new NoteDto("updated title", "updated content");

        Note note = createNote(noteDto);
        updateNote(note.getId(), updatedNoteDto);
        Note updatedNote = getNote(note.getId());
        List<HistoryNote> noteHistory = getNoteHistory(note.getId());

        assertThat(note).isNotNull();
        assertThat(updatedNote).isNotNull();
        assertThat(updatedNote).isEqualToComparingOnlyGivenFields(
                updatedNoteDto, "title", "content");
        assertThat(updatedNote.getVersion()).isEqualTo(1);
        assertThat(noteHistory).hasSize(1);
        assertThat(noteHistory).element(0).isEqualToComparingOnlyGivenFields(note,
                "title", "content", "version");
    }

    @Test
    public void delete_note_should_delete_existing_note_and_save_deleted_note_version() {
        NoteDto noteDto = new NoteDto("note title", "note content");

        Note note = createNote(noteDto);
        deleteNote(note.getId());
        getNote(note.getId(), 404);
        List<HistoryNote> noteHistory = getNoteHistory(note.getId());

        assertThat(note).isNotNull();
        assertThat(noteHistory).hasSize(1);
        assertThat(noteHistory).element(0).isEqualToComparingOnlyGivenFields(note,
                "title", "content");
    }

    @Test
    public void get_all_notes_should_return_single_note() {
        NoteDto noteDto = new NoteDto("note title", "note content");

        Note note = createNote(noteDto);
        Note note2 = createNote(noteDto);
        deleteNote(note2.getId());
        List<Note> notes = getAllNotes();

        assertThat(notes).hasSize(1);
        assertThat(notes).hasOnlyOneElementSatisfying(
                elem -> assertThat(elem).isEqualToComparingFieldByField(note));
    }

    private Note createNote(NoteDto noteDto) {
        return given()
                .body(noteDto)
        .when()
                .post("/api/notes")
        .then()
                .statusCode(200)
                .extract().body().as(Note.class);
    }

    private Note getNote(int id) {
        return when()
                .get("/api/notes/" + id)
        .then()
                .statusCode(200)
                .extract().body().as(Note.class);
    }

    private void getNote(int id, int expectedStatus) {
        when()
                .get("/api/notes/" + id)
        .then()
                .statusCode(expectedStatus);
    }

    private List<Note> getAllNotes() {
        return when()
                .get("/api/notes/")
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("$", Note.class);
    }

    private Note updateNote(int id, NoteDto noteDto) {
        return given()
                .body(noteDto)
        .when()
                .put("/api/notes/" + id)
        .then()
                .statusCode(200)
                .extract().body().as(Note.class);
    }

    private List<HistoryNote> getNoteHistory(int id) {
        return when()
                .get("/api/notes/" + id + "/history")
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("$", HistoryNote.class);
    }

    private void deleteNote(int id) {
        when()
                .delete("/api/notes/" + id)
        .then()
                .statusCode(200);
    }
}
