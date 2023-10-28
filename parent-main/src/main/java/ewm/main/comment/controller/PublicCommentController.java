package ewm.main.comment.controller;

import ewm.main.comment.dto.EventCommentDto;
import ewm.main.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events/{eventId}/comments")
public class PublicCommentController {

    private final CommentService commentService;

    @Autowired
    public PublicCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventCommentDto> getUserComments(@PathVariable Long eventId) {
        log.info("Getting event {} comments...", eventId);
        return commentService.getEventComments(eventId);
    }
}
