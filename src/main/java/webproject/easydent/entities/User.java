package webproject.easydent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import webproject.easydent.review.comment.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User{
    @Column(name = "user_id")
    @Id
    String email;

    @Column(nullable = true)
    String name;

    @Column(nullable = true)
    String phoneNumber;

    @Column(nullable = true)
    LocalDate birthDay;

    @Column(nullable = true)
    String accountType; // 어떤 계정(구글, 카카오)으로 로그인 했는지

    @Column(nullable = true)
    LocalDate createdAt; // 계정 생성일

    @Column(nullable = true)
    String address; // 주소


    // FamilyAccount 관계 수정
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "family_id", unique = true)
    private FamilyAccount familyAccount;

    private Boolean isFamilyLeader; // 가족 리더인지 여부

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Location> locationList; // 리뷰에 대한 답글 리스트

}
