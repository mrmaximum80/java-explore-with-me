package ewm.main.comment.service;

import ewm.main.comment.dto.CommentDto;
import ewm.main.comment.dto.EventCommentDto;
import ewm.main.comment.dto.NewCommentDto;
import ewm.main.comment.dto.UserCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(NewCommentDto newComment, Long eventId, Long userId);

    CommentDto updateComment(NewCommentDto newComment, Long userId, Long comId);

    List<UserCommentDto> getUserComments(Long userId);

    List<EventCommentDto> getEventComments(Long eventId);
}
