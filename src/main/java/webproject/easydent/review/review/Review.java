package webproject.easydent.review.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import webproject.easydent.entities.User;
import webproject.easydent.review.comment.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 리뷰 아이디

    @Column(length = 200)
    private String subject; // 리뷰 제목

    @Column(columnDefinition = "TEXT")
    private String content; //리뷰 내용

    private LocalDateTime createDate; // 리뷰 작성일시

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> commentList; // 리뷰에 대한 답글 리스트

    @ManyToOne
    private User author; //글쓴이

//    private LocalDateTime modifyDate;
}
