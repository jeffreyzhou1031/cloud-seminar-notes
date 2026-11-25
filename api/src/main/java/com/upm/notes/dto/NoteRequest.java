package com.upm.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoteRequest {

    @NotBlank(message = "text cannot be empty")
    @Size(min = 1, max = 500, message = "text must be 1-500 characters")
    private String text;

    public NoteRequest() {
    }

    public NoteRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
