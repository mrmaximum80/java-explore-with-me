package ewm.main.comment.model;

import ewm.main.event.model.Event;
import ewm.main.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @PositiveOrZero
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private User author;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    public Comment(String text, Event event, User author, LocalDateTime created) {
        this.text = text;
        this.event = event;
        this.author = author;
        this.created = created;
    }


}
