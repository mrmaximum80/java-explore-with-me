package ewm.main.comment.repository;

import ewm.main.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthor_Id(Long userId);

    List<Comment> findAllByEvent_Id(Long eventId);
}
