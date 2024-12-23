package cat.tecnocampus.notes2324.application;

import cat.tecnocampus.notes2324.application.dtos.*;
import cat.tecnocampus.notes2324.application.exceptions.NoteNotFoundException;
import cat.tecnocampus.notes2324.application.exceptions.UserDoesNotOwnNoteException;
import cat.tecnocampus.notes2324.application.exceptions.UserNotFoundException;
import cat.tecnocampus.notes2324.application.mapper.CommentMapper;
import cat.tecnocampus.notes2324.application.mapper.NoteMapper;
import cat.tecnocampus.notes2324.application.mapper.UserMapper;
import cat.tecnocampus.notes2324.domain.Comment;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.Tag;
import cat.tecnocampus.notes2324.domain.User;
import cat.tecnocampus.notes2324.persistence.CommentRepository;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import cat.tecnocampus.notes2324.persistence.TagRepository;
import cat.tecnocampus.notes2324.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotesService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public NotesService(NoteRepository noteRepository, UserRepository userRepository, PermissionService permissionService,
                        TagRepository tagRepository, CommentRepository commentRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    public UserWithOwnedNotesDTO getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return UserMapper.userToUserDTO(user, noteRepository.findAllByOwner(user));
    }

    public List<NoteDTO> getUserNotes(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return noteRepository.findAllByOwner(owner).stream().map(NoteMapper::noteToNoteDTO).toList();
    }

    public void createUserNote(long ownerId, NoteCreate noteCreate) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(noteCreate.title());
        note.setContent(noteCreate.content());
        noteCreate.tags().stream().forEach(t -> note.addTag(new Tag(t)));

        noteRepository.save(note);
    }

    @Transactional
    public void updateUserNote(long ownerId, NoteCreate noteUpdate) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = noteRepository.findById(noteUpdate.noteId()).orElseThrow(() -> new NoteNotFoundException(noteUpdate.noteId()));

        if (note.isOwner(user.getId()) || permissionService.userCanEditNote(user, note)) {
            if (noteUpdate.title() != null) note.setTitle(noteUpdate.title());
            if (noteUpdate.content() != null) note.setContent(noteUpdate.content());
            updateNoteTags(noteUpdate.tags(), note);
        }
    }

    private void updateNoteTags(List<String> tags, Note note) {
        Set<Tag> newTags;
        if (tags != null)
            newTags = tags.stream().map(t -> new Tag(t)).collect(Collectors.toSet());
        else newTags = new HashSet<>();

        // tags to delete = current - new
        Set<Tag> tagsToDelete = new HashSet<>(note.getTags());
        tagsToDelete.removeAll(newTags);

        tagsToDelete.stream().forEach(t -> note.removeTag(t));
        newTags.stream().forEach(t -> note.addTag(t));
    }

    //TODO 2.2 this calls the actual query in the repository. Uncomment the line and delete the return statement of an empty list.
    // Go to: todo2.3
    public List<UserDTO> getUsersRatedByNotes() {
        return userRepository.findUsersRatedByNotes();
    }

    // TODO 3.5 It must add a comment to a note only if the user is the owner of the note.
    public void addNoteComment(long userId, long noteId, CommentDTO commentDTO) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NoteNotFoundException(noteId));
        if (!note.isOwner(userId))
            throw new UserDoesNotOwnNoteException(userId, noteId);
        Comment comment = new Comment(commentDTO.title(), commentDTO.body());
        comment.setNote(note);
        commentRepository.save(comment);
    }

    // TODO 3.6 It must return a list of CommentDTO of the note only if the user is the owner of the note or has permission to view it.
    //    Otherwise it returns an empty list.
    public List<CommentDTO> getNoteComments(long userId, long noteId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new NoteNotFoundException(noteId));

        if (note.isOwner(userId) || permissionService.userCanViewNote(user, note)) {
            return commentRepository.findAllByNote(note).stream().map(CommentMapper::commentToCommentDTO).toList();
        } else return new ArrayList<>();
    }
}

