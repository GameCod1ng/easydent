package webproject.easydent.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webproject.easydent.entities.Dentistry;
import webproject.easydent.entities.Reservation;
import webproject.easydent.entities.User;
import webproject.easydent.repositories.ReservationRepository;
import webproject.easydent.service.DentistryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import webproject.easydent.vos.CustomOAuth2User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class DentistryController {

    public final DentistryService dentistryService;

    @GetMapping
    public String reservation(Model model, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            log.info("reservation :: " + oAuth2User.getUserName());
            model.addAttribute("userName", oAuth2User.getUserName());
            model.addAttribute("userEmail", oAuth2User.getName());
            return "reservation";
        } else {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
    }

    //responseEntity -> http 상태코드, 헤더, 본문 데이터를 모두 포함할 수 있는 객체로
    //http 상태코드200(OK)와 함께 results 객체를 json 형태로 반환
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Set<Dentistry>> searchDentistry(
            @RequestParam("keyword") String keyword
            , @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        if(oAuth2User!=null){
            log.info("searchDentistry :: " + oAuth2User.getUserName());
        }
        else{
            log.info("사용자 인증 정보를 찾아올 수 없습니다. ");
        }
        Set<Dentistry> results = dentistryService.searchByKeyword(keyword);
        System.out.println(keyword + " 검색 결과 : " + results);
        return ResponseEntity.ok(results);
    }

    @Autowired
    private ReservationRepository reservationRepository;


    @CrossOrigin(origins = "http://192.168.0.49:9090", allowCredentials = "true")
    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {

        log.info("CreateReservation :: " + oAuth2User.getUserName());
        if (oAuth2User != null) {
            reservation.setCustomerName(oAuth2User.getUserName());
            reservation.setReservedAt(LocalDateTime.now());
            reservation.setUser(oAuth2User.getUser());
            Reservation savedReservation = reservationRepository.save(reservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } else {
            throw new RuntimeException("사용자 인증 정보를 찾을 수 없습니다.");
        }
    }


}
