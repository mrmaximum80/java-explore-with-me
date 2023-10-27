package ewm.main.comment.service;


import ewm.main.comment.dto.CommentDto;
import ewm.main.comment.dto.EventCommentDto;
import ewm.main.comment.dto.NewCommentDto;
import ewm.main.comment.dto.UserCommentDto;
import ewm.main.comment.map.CommentMapper;
import ewm.main.comment.model.Comment;
import ewm.main.comment.repository.CommentRepository;
import ewm.main.error.ConflictException;
import ewm.main.error.NotFoundException;
import ewm.main.event.model.Event;
import ewm.main.event.model.State;
import ewm.main.event.repository.EventRepository;
import ewm.main.user.model.User;
import ewm.main.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto createComment(NewCommentDto newComment, Long eventId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Сannot comment on an unpublished eventEvent " + eventId + ".");
        }
        Comment comment = CommentMapper.toComment(newComment, user, event);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(NewCommentDto newComment, Long userId, Long comId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found"));
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("User with id=" + userId + " is not author of comment " + comId + ".");
        }
        comment.setText(newComment.getText());
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<UserCommentDto> getUserComments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        List<Comment> comments = commentRepository.findAllByAuthor_Id(userId);
        return comments.stream().map(CommentMapper::toUserCommentDto).collect(Collectors.toList());
    }

    @Override
    public List<EventCommentDto> getEventComments(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        return comments.stream().map(CommentMapper::toEventCommentDto).collect(Collectors.toList());
    }

}
