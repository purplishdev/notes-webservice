package me.michalwozniak.noteswebservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import me.michalwozniak.noteswebservice.Api;
import me.michalwozniak.noteswebservice.note.dto.HistoryNoteDto;
import me.michalwozniak.noteswebservice.note.dto.NoteDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
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

    public static final NoteDto NOTE_DTO = new NoteDto("note title", "note content");

    public static final String[] REQUIRED_NOTE_FIELDS = { "title", "content" };

    public static final String HAL_REL_EMBEDDED_NOTES = "_embedded.notes";

    public static final String HAL_REL_HISTORY_NOTES = "noteHistory";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.config = RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> objectMapper
        ));
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder()
                .setContentType("application/hal+json;charset=UTF-8")
                .build();
    }

    @Test
    public void create_note_should_create_and_return_new_note() {
        NoteDto note = createNote(NOTE_DTO);

        assertThat(note).isNotNull();
        assertThat(note).isEqualToComparingOnlyGivenFields(NOTE_DTO, REQUIRED_NOTE_FIELDS);
    }

    @Test
    public void get_note_should_return_existing_note() {
        NoteDto note = createNote(NOTE_DTO);

        NoteDto returnedNote = getNoteByLink(note.getId());

        assertThat(note).isNotNull();
        assertThat(returnedNote).isNotNull();
        assertThat(returnedNote).isEqualToComparingOnlyGivenFields(note, REQUIRED_NOTE_FIELDS);
    }

    @Test
    public void update_note_should_update_note_and_save_old_note_version() {
        NoteDto updatedNoteDto = new NoteDto("updated title", "updated content");

        NoteDto note = createNote(NOTE_DTO);
        updateNoteByLink(note.getId(), updatedNoteDto);
        NoteDto updatedNote = getNoteByLink(note.getId());
        List<HistoryNoteDto> noteHistory = getNoteHistoryByLink(note.getLink(HAL_REL_HISTORY_NOTES));

        assertThat(note).isNotNull();
        assertThat(updatedNote).isNotNull();
        assertThat(updatedNote).isEqualToComparingOnlyGivenFields(updatedNoteDto, REQUIRED_NOTE_FIELDS);
        assertThat(noteHistory).hasSize(1);
        assertThat(noteHistory).element(0).isEqualToComparingOnlyGivenFields(note, REQUIRED_NOTE_FIELDS);
    }

    @Test
    public void delete_note_should_delete_existing_note_and_save_deleted_note_version() {
        NoteDto note = createNote(NOTE_DTO);
        deleteNoteByLink(note.getId());
        getNoteByLink(note.getId(), 404);

        List<HistoryNoteDto> noteHistory = getNoteHistoryByLink(note.getLink(HAL_REL_HISTORY_NOTES));

        assertThat(note).isNotNull();
        assertThat(noteHistory).hasSize(1);
        assertThat(noteHistory).element(0).isEqualToComparingOnlyGivenFields(note, REQUIRED_NOTE_FIELDS);
    }

    @Test
    public void get_all_notes_should_return_single_note() {
        NoteDto note = createNote(NOTE_DTO);
        NoteDto note2 = createNote(NOTE_DTO);
        deleteNoteByLink(note2.getId());
        List<NoteDto> notes = getAllNotes();

        assertThat(notes).hasSize(1);
        assertThat(notes).hasOnlyOneElementSatisfying(returnedNote ->
                assertThat(returnedNote).isEqualToComparingOnlyGivenFields(note, REQUIRED_NOTE_FIELDS));
    }

    private NoteDto createNote(NoteDto noteDto) {
        return given()
                .body(noteDto)
        .when()
                .post(Api.NOTES)
        .then()
                .statusCode(200)
                .extract().body().as(NoteDto.class);
    }

    private NoteDto getNoteByLink(Link id) {
        return when()
                .request(Method.GET, id.getHref())
        .then()
                .statusCode(200)
                .extract().body().as(NoteDto.class);
    }

    private void getNoteByLink(Link id, int expectedStatus) {
        when()
                .request(Method.GET, id.getHref())
        .then()
                .statusCode(expectedStatus);
    }

    private List<NoteDto> getAllNotes() {
        return when()
                .get(Api.NOTES)
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(HAL_REL_EMBEDDED_NOTES, NoteDto.class);
    }

    private NoteDto updateNoteByLink(Link id, NoteDto noteDto) {
        return given()
                .body(noteDto)
        .when()
                .request(Method.PUT, id.getHref())
        .then()
                .statusCode(200)
                .extract().body().as(NoteDto.class);
    }

    private List<HistoryNoteDto> getNoteHistoryByLink(Link id) {
        return when()
                .request(Method.GET, id.getHref())
        .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(HAL_REL_EMBEDDED_NOTES, HistoryNoteDto.class);
    }

    private void deleteNoteByLink(Link id) {
        when()
                .request(Method.DELETE, id.getHref())
        .then()
                .statusCode(200);
    }
}
