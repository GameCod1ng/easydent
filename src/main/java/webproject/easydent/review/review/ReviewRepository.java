package webproject.easydent.review.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import webproject.easydent.entities.User;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findBySubject(String subject);
    Review findBySubjectAndContent(String subject, String content);
    List<Review> findBySubjectLike(String subject);
    List<Review> findByAuthor(User author);
}
