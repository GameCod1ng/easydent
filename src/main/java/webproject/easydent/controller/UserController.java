package webproject.easydent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import webproject.easydent.entities.Product;
import webproject.easydent.entities.User;
import webproject.easydent.service.ProductService;
import webproject.easydent.service.UserService;
import webproject.easydent.vos.CustomOAuth2User;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User != null) {
            User user = customOAuth2User.getUser();
            log.info("Authenticated user: {}", user);
            addUserToModel(model, user);
            return "home";
        }
        else{
            log.info("User is Null");
            return "redirect:/login";
        }
    }


    @GetMapping("/qa/page")
    public String qaPage() {
        return "qa_page";
    }

    @GetMapping("/qa/page1")
    public String qaPage1() {
        return "qa_page1";
    }

    @GetMapping("/qa/page2")
    public String qaPage2() {
        return "qa_page2";
    }


    private void addUserToModel(Model model, User user) {
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userName", user.getName());
        model.addAttribute("accountType", user.getAccountType());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("birthDay", user.getBirthDay());
        model.addAttribute("createdAt", user.getCreatedAt());
        model.addAttribute("address", user.getAddress());
    }

}