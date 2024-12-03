package webproject.easydent.review.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import webproject.easydent.entities.User;
import webproject.easydent.review.comment.Comment;
import webproject.easydent.review.comment.CommentForm;
import webproject.easydent.service.UserService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model){
        List<Review> reviewList = this.reviewService.getList();
        model.addAttribute("reviewList", reviewList);
        return "review_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm) {
        Review review = this.reviewService.getReview(id);
        model.addAttribute("review", review);
        return "review_detail";
    }


    @GetMapping("/create")
    public String reviewCreate(Model model) {
        model.addAttribute("reviewForm", new ReviewForm());
        return "review_form";
    }



//    @GetMapping("/create")
//    public String reviewCreate(ReviewForm reviewForm){
//        return "review_form";
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String reviewCreate(@Valid ReviewForm reviewForm, BindingResult
            bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) { //잘못입력했으니 다시 question_form 호출
            return "review_form";
        }
        User user = this.userService.getUser(principal.getName());
        this.reviewService.create(reviewForm.getSubject(), reviewForm.getContent(), user);
        return "redirect:/review/list";
    }

    //12.02

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/modify/{id}")
//    public String reviewModify(ReviewForm reviewForm, @PathVariable("id")
//    Integer id, Principal principal) {
//        Review review = this.reviewService.getReview(id);
//        if(!review.getAuthor().getName().equals(principal.getName())){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
//        reviewForm.setSubject(review.getSubject());
//        reviewForm.setContent(review.getContent());
//        return "review_form";
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/modify/{id}")
//    public String reviewModify(ReviewForm reviewForm, BindingResult bindingResult,
//                                 @PathVariable("id")
//                                 Integer id, Principal principal) {
//        if(bindingResult.hasErrors()){
//            return "review_form";
//        }
//
//        Review review = this.reviewService.getReview(id);
//        if(!review.getAuthor().getName().equals(principal.getName())){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
//        this.reviewService.modify(review, reviewForm.getSubject(), reviewForm.getContent());
//        return String.format("redirect:/review/detail/%s", id);
//    }
}
