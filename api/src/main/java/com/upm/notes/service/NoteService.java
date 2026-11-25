package com.upm.notes.service;

import com.upm.notes.model.Note;
import com.upm.notes.repository.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> listNotes() {
        return noteRepository.findTop100ByOrderByIdDesc();
    }

    @Transactional
    public Note createNote(String text) {
        String cleanText = (text != null) ? text.trim() : "";
        if (cleanText.isEmpty() || cleanText.length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text must be 1-500 characters");
        }
        Note note = new Note(cleanText);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found with id: " + id));
    }

    @Transactional
    public Note updateNote(Long id, String text) {
        Note note = getNoteById(id);
        String cleanText = (text != null) ? text.trim() : "";
        if (cleanText.isEmpty() || cleanText.length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text must be 1-500 characters");
        }
        note.setText(cleanText);
        return noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found with id: " + id);
        }
        noteRepository.deleteById(id);
    }
}
