package me.michalwozniak.noteswebservice;

public interface Api {
    String BASE = "/api/v1";

    String NOTES = BASE + "/notes";
    String NOTE = NOTES + "/{noteId}";
    String NOTE_HISTORY = NOTE + "/history";
}
