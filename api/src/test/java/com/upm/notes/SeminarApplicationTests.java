package com.upm.notes;

import com.upm.notes.model.Note;
import com.upm.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeminarApplicationTests {

    @Autowired
    private NoteService noteService;

    @Test
    void testNoteCrudOperations() {
        Note created = noteService.createNote("Local Test Note");
        assertNotNull(created.getId());
        assertEquals("Local Test Note", created.getText());
        assertNotNull(created.getCreatedAt());

        List<Note> list = noteService.listNotes();
        assertFalse(list.isEmpty());

        Note fetched = noteService.getNoteById(created.getId());
        assertEquals("Local Test Note", fetched.getText());

        Note updated = noteService.updateNote(created.getId(), "Updated Note");
        assertEquals("Updated Note", updated.getText());

        noteService.deleteNote(created.getId());
        assertThrows(Exception.class, () -> noteService.getNoteById(created.getId()));
    }
}
