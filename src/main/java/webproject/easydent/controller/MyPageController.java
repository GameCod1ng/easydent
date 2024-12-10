package webproject.easydent.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webproject.easydent.entities.FamilyAccount;
import webproject.easydent.entities.User;
import webproject.easydent.repositories.FamilyRepository;
import webproject.easydent.review.review.Review;
import webproject.easydent.review.review.ReviewService;
import webproject.easydent.service.FamilyAccountService;
import webproject.easydent.service.ProductService;
import webproject.easydent.service.UserService;
import webproject.easydent.vos.CustomOAuth2User;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final UserService userService;
    private final FamilyAccountService familyAccountService;
    private final ReviewService reviewService;
    private final FamilyRepository familyRepository;

    @GetMapping
    public String myPage(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        if (customOAuth2User != null) {
//            User user = customOAuth2User.getUser();
//            log.info("Authenticated user: {}", user);
//            addUserToModel(model, user);
//        }
//        return "mypage";
        if (customOAuth2User != null) {
            String email = customOAuth2User.getUser().getEmail(); // 현재 로그인한 사용자의 이메일
            User user = userService.findByEmail(email); // DB에서 사용자 정보 조회
            model.addAttribute("userName", user.getName()); // 모델에 사용자 이름 추가
        }
        return "mypage"; // 마이페이지 템플릿 반환
    }

    //정보 수정
    @GetMapping("/edit-info")
    public String userInfo(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        if (customOAuth2User != null) {
            User user = customOAuth2User.getUser();
            log.info("Authenticated user: {}", user);
            model.addAttribute("user", user);
        } else {
            log.info("user-notFound");
        }
        return "edit-info";
    }

    //가족 계정
    @PostMapping("/family-group/create")
    public String createFamilyGroup(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(name = "memberEmail") String memberEmail,
            @RequestParam(name = "relationship") String relationship,
            HttpSession session,
            Model model) {
        try {
            // 가족 그룹 생성
            FamilyAccount familyAccount = familyAccountService.createFamilyGroup(user.getUser(), memberEmail, relationship);

            // 세션에 데이터 저장
            // TODO : DB에 저장하기
            session.setAttribute("currentUser", user.getUser());
            session.setAttribute("memberEmail", memberEmail);
            session.setAttribute("relationship", relationship);
            session.setAttribute("familyAccount", familyAccount);

            return "family-management";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "family-management";
        }
    }

//    @GetMapping("/family-management")
//    public String familyManagement(Model model, HttpSession session) {
//        // 세션에서 데이터 복원
//        model.addAttribute("currentUser", session.getAttribute("currentUser"));
//        model.addAttribute("memberEmail", session.getAttribute("memberEmail"));
//        model.addAttribute("relationship", session.getAttribute("relationship"));
//        model.addAttribute("familyAccount", session.getAttribute("familyAccount"));
//
//        return "family-management";
//    }
@GetMapping("/family-management")
public String familyManagement(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    if (customOAuth2User != null) {
        User user = customOAuth2User.getUser();
        FamilyAccount familyAccount = familyAccountService.getFamilyAccount(user);

        if (familyAccount != null) {
            model.addAttribute("familyAccount", familyAccount);
            model.addAttribute("hasFamily", true);
        } else {
            model.addAttribute("hasFamily", false);
        }
    }
    return "family-management";
}


    //고객 센터
    @GetMapping("/customer-center")
    public String customerCenter() {
        return "customer-center";
    }

    //공지 사항
    @GetMapping("/notice")
    public String notice() {
        return "notice";
    }

    //진료 내역
    @GetMapping("/medical-history")
    public String medicalHistory() {
        return "medical-history";
    }

    //치과 가입 문의
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/myReview")
    public String myReview(Model model, Authentication authentication) {
        if (authentication != null) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            User user = customOAuth2User.getUser();
            List<Review> myReviewList = this.reviewService.getListById(user);
            model.addAttribute("myReviewList", myReviewList);
            return "my_review";
        }
        return "redirect:/login";
    }
}
