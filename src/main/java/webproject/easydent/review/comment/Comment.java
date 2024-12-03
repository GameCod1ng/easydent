package webproject.easydent.review.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import webproject.easydent.entities.User;
import webproject.easydent.review.review.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime createDate; // 답글 작성 일시
    
    @ManyToOne
    private Review review; //하나의 리뷰에 여러개의 답글 가능

    @ManyToOne
    private User author;

//    private LocalDateTime modifyDate;
}
