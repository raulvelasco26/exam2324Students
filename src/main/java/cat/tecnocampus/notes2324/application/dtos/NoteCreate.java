package cat.tecnocampus.notes2324.application.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

//TODO 1.1: you may need to add some annotations here
public record NoteCreate(
        long noteId,
        String title,
        String content,
        List<String> tags) {
}
