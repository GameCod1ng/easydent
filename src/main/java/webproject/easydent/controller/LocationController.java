package webproject.easydent.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webproject.easydent.entities.Location;
import webproject.easydent.entities.User;
import webproject.easydent.repositories.LocationRepository;
import webproject.easydent.service.LocationService;
import webproject.easydent.service.UserService;
import webproject.easydent.vos.CustomOAuth2User;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    public final LocationService locationService;
    public final UserService userService;
    public final LocationRepository locationRepository;


    //    @CrossOrigin(origins = "http://localhost:9090", allowCredentials = "true")
////    @CrossOrigin(origins = "http://192.168.45.179:9090", allowCredentials = "true")
//    @PostMapping("/home/location")
////    @ResponseBody
//    public String saveLocation(Model model, @RequestBody Location location, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
//        log.info(location.bname);
//        if (customOAuth2User != null) {
//            String email = customOAuth2User.getUser().getEmail();
//            User user = userService.findByEmail(email);
//            location.setUser(user);
//            model.addAttribute("bname", location.bname);
//            locationService.saveLocation(location);
//        }
//        return "redirect:/home";
//    }
    @PostMapping("/home/location")
    @ResponseBody
    public ResponseEntity<?> saveLocation(@RequestBody Location location, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User != null) {
            String email = customOAuth2User.getUser().getEmail();
            User user = userService.findByEmail(email);
            location.setUser(user);
            locationService.saveLocation(location);
            return ResponseEntity.ok(location);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/current")
    @ResponseBody
    public String getCurrentUserEmail(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        if (customOAuth2User != null) {
            return customOAuth2User.getUser().getEmail();
        }
        return "";
    }
}

