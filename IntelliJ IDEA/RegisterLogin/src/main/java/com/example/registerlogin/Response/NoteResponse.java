package com.example.registerlogin.Response;

import com.example.registerlogin.entity.Note;
import lombok.Data;

import java.util.List;

@Data
public class NoteResponse<T>{
    private boolean success;
    private String message;
    private Note note;
}
