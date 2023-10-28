package ewm.main.comment.map;

import ewm.main.comment.dto.CommentDto;
import ewm.main.comment.dto.EventCommentDto;
import ewm.main.comment.dto.NewCommentDto;
import ewm.main.comment.dto.UserCommentDto;
import ewm.main.comment.model.Comment;
import ewm.main.event.map.EventMapper;
import ewm.main.event.model.Event;
import ewm.main.user.map.UserMapper;
import ewm.main.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto commentDto, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now().withNano(0));
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                EventMapper.toEventShortDto(comment.getEvent()),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getCreated()
        );
    }

    public UserCommentDto toUserCommentDto(Comment comment) {
        return new UserCommentDto(
                comment.getText(),
                EventMapper.toEventShortDto(comment.getEvent()),
                comment.getCreated()
        );
    }

    public EventCommentDto toEventCommentDto(Comment comment) {
        return new EventCommentDto(
                comment.getText(),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getCreated()
        );
    }
}
