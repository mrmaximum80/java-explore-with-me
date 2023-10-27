package ewm.main.comment.controller;

import ewm.main.comment.dto.CommentDto;
import ewm.main.comment.dto.NewCommentDto;
import ewm.main.comment.dto.UserCommentDto;
import ewm.main.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newComment,
                                    @PathVariable Long userId,
                                    @RequestParam(name = "eventId") Long eventId) {
        log.info("Creating new comment for event {}...", eventId);
        return commentService.createComment(newComment, eventId, userId);
    }

    @PatchMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto newComment,
                                    @PathVariable Long userId,
                                    @PathVariable Long comId) {
        log.info("Updating comment {}...", comId);
        return commentService.updateComment(newComment, userId, comId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserCommentDto> getUserComments(@PathVariable Long userId) {
        log.info("Getting user's {} comments...", userId);
        return commentService.getUserComments(userId);
    }
}
