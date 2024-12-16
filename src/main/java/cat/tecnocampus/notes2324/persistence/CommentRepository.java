package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.domain.Comment;
import cat.tecnocampus.notes2324.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TODO 3.7 You may need a method to retrieve the comments of a note
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByNote(Note note);
}
